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
import java.util.LinkedHashMap;
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

		_proseStatement = formatProseStatement(proseStatement);

		_poshiProseMatcher = PoshiProseMatcher.getPoshiProseMatcher(
			getProseStatementMatchingString());

		List<String> varNames = _poshiProseMatcher.getVarNames();

		List<String> varValues = new ArrayList<>();

		Matcher varValueMatcher = _varValuePattern.matcher(_proseStatement);

		while (varValueMatcher.find()) {
			varValues.add(varValueMatcher.group(1));
		}

		for (int i = 0; i < varNames.size(); i++) {
			String varName = varNames.get(i);

			if (_varMap.containsKey(varName)) {
				StringBuilder sb = new StringBuilder();

				sb.append("Duplicate variable value assignment: ${");
				sb.append(varName);
				sb.append("} already has a value of ");
				sb.append(_varMap.get(varName));
				sb.append("\nProse statement: ");
				sb.append(_proseStatement);
				sb.append("\nMatching macro prose statement: ");
				sb.append(_poshiProseMatcher.getPoshiProse());

				throw new RuntimeException(sb.toString());
			}

			String varValue = varValues.get(i);

			if ((i + 1) == varNames.size()) {
				Matcher multiLineStringMatcher =
					_multiLineStringPattern.matcher(_proseStatement);

				if (multiLineStringMatcher.find()) {
					varValue = multiLineStringMatcher.group(1);
				}
			}

			_varMap.put(varName, varValue);
		}
	}

	public Element toElement() {
		Element element = Dom4JUtil.getNewElement(
			"execute", null,
			new DefaultAttribute(
				"macro",
				_poshiProseMatcher.getMacroNamespacedClassCommandName()));

		for (Map.Entry<String, String> varMapEntry : _varMap.entrySet()) {
			Element varElement = Dom4JUtil.getNewElement(
				"var", null,
				new DefaultAttribute("name", varMapEntry.getKey()));

			String value = varMapEntry.getValue();

			if (value.contains(_LINE_SEPARATOR)) {
				varElement.addCDATA(value);
			}
			else {
				varElement.addAttribute("value", value);
			}

			Dom4JUtil.addToElement(element, varElement);
		}

		return element;
	}

	protected String formatProseStatement(String proseStatement) {
		String formattedProseStatement = proseStatement.trim();

		formattedProseStatement = formattedProseStatement.replaceAll(
			_LINE_SEPARATOR + "\t\t", _LINE_SEPARATOR);

		return formattedProseStatement;
	}

	protected String getProseStatementMatchingString() {
		String proseStatementMatchingString = _proseStatement.replaceAll(
			_multiLineStringPattern.pattern(), " \"\"");

		proseStatementMatchingString = proseStatementMatchingString.replaceAll(
			_varValuePattern.pattern(), "\"\"");

		return proseStatementMatchingString;
	}

	protected static final String[] KEYWORDS =
		{"*", "And", "Given", "Then", "When"};

	private static final String _LINE_SEPARATOR = System.lineSeparator();

	private static final Pattern _multiLineStringPattern = Pattern.compile(
		"(?s)\\s*\"\"\".*?\\R(.*?)\\s*\"\"\"");
	private static final Pattern _varValuePattern = Pattern.compile(
		"\"(.*?)\"");

	private final PoshiProseMatcher _poshiProseMatcher;
	private final String _proseStatement;
	private final Map<String, String> _varMap = new LinkedHashMap<>();

}