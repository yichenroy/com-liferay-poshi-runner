/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.poshi.runner.prose;

import com.liferay.poshi.runner.util.Dom4JUtil;
import com.liferay.poshi.runner.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;

/**
 * @author Yi-Chen Tsai
 */
public class PoshiProseStatement {

	public PoshiProseStatement(String proseStatement) {
		for (String proseKeyword : PoshiProseStatement.KEYWORDS) {
			if (proseStatement.startsWith(proseKeyword)) {
				proseStatement = StringUtil.replaceFirst(
					proseStatement, proseKeyword, "");

				break;
			}
		}

		_proseStatement = proseStatement.trim();

		_poshiProseMatcher = PoshiProseMatcher.getPoshiProseMatcher(
			_proseStatement.replaceAll(_varValuePattern.pattern(), "\"\""));

		List<String> parameterNames = _poshiProseMatcher.getParameterNames();

		List<String> parameterValues = new ArrayList<>();

		Matcher matcher = _varValuePattern.matcher(_proseStatement);

		while (matcher.find()) {
			parameterValues.add(matcher.group(1));
		}

		for (int i = 0; i < parameterNames.size(); i++) {
			String parameterName = parameterNames.get(i);
			String parameterValue = parameterValues.get(i);

			if (!_varValueMap.containsKey(parameterName)) {
				_varValueMap.put(parameterName, parameterValue);

				continue;
			}

			String varValue = _varValueMap.get(parameterName);

			if (varValue.equals(parameterValue)) {
				StringBuilder sb = new StringBuilder();

				sb.append("Duplicate variable value assignment: ${");
				sb.append(parameterName);
				sb.append("} already has a value of ");
				sb.append(varValue);
				sb.append("\nProse statement: ");
				sb.append(_proseStatement);
				sb.append("\nMatching macro prose statement: ");
				sb.append(_poshiProseMatcher.getPoshiProse());

				throw new RuntimeException(sb.toString());
			}
		}
	}

	public Element toElement() {
		Element executeElement = Dom4JUtil.getNewElement("execute");

		Dom4JUtil.addToElement(
			executeElement,
			new DefaultAttribute(
				"macro",
				_poshiProseMatcher.getMacroNamespacedClassCommandName()));

		for (Element varElement : getVarElements()) {
			Dom4JUtil.addToElement(executeElement, varElement);
		}

		return executeElement;
	}

	protected List<Element> getVarElements() {
		List<Element> varElements = new ArrayList<>();

		List<String> varNames = _poshiProseMatcher.getVarNames();

		for (String varName : varNames) {
			Element varElement = Dom4JUtil.getNewElement("var");

			Dom4JUtil.addToElement(
				varElement, new DefaultAttribute("name", varName),
				new DefaultAttribute("value", _varValueMap.get(varName)));

			varElements.add(varElement);
		}

		return varElements;
	}

	protected static final String[] KEYWORDS = {"And", "Given", "Then", "When"};

	private static final Pattern _varValuePattern = Pattern.compile(
		"\"(.*?)\"");

	private final PoshiProseMatcher _poshiProseMatcher;
	private final String _proseStatement;
	private final Map<String, String> _varValueMap = new HashMap<>();

}