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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Peter Yoo
 */
public class PoshiProseMatcher {

	public static PoshiProseMatcher getPoshiProseMacro(
		String poshiProse, String macroNamespacedClassCommandName) {

		String key = _toString(poshiProse);

		if (poshiProseMacroMap.containsKey(key)) {
			return poshiProseMacroMap.get(key);
		}

		poshiProseMacroMap.put(
			key,
			new PoshiProseMatcher(poshiProse, macroNamespacedClassCommandName));

		return poshiProseMacroMap.get(key);
	}

	public String getMacroNamespacedClassCommandName() {
		return _macroNamespacedClassCommandName;
	}

	public List<String> getParameterNames() {
		List<String> parameterNames = new ArrayList<>();

		Matcher matcher = _poshiProseParameterPattern.matcher(_poshiProse);

		while (matcher.find()) {
			parameterNames.add(matcher.group(1));
		}

		return parameterNames;
	}

	public String getPoshiProse() {
		return _poshiProse;
	}

	@Override
	public String toString() {
		return _toString(_poshiProse);
	}

	protected static PoshiProseMatcher getPoshiProseMacro(
		String matchingString) {

		return poshiProseMacroMap.get(matchingString);
	}

	protected static final Map<String, PoshiProseMatcher> poshiProseMacroMap =
		new HashMap<>();

	private static String _toString(String matchingString) {
		return matchingString.replaceAll("\\$\\{.+?\\}", "\"\"");
	}

	private PoshiProseMatcher(
		String poshiProse, String macroNamespacedClassCommandName) {

		_poshiProse = poshiProse;
		_macroNamespacedClassCommandName = macroNamespacedClassCommandName;
	}

	private static final Pattern _poshiProseParameterPattern = Pattern.compile(
		"\\$\\{(.+?)\\}");

	private final String _macroNamespacedClassCommandName;
	private final String _poshiProse;

}