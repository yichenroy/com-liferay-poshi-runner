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
import java.util.List;

import org.dom4j.Element;

/**
 * @author Yi-Chen Tsai
 */
public class PoshiProseDefinition {

	public PoshiProseDefinition(String fileName, String fileContent) {
		_fileName = fileName;

		List<String> poshiProseScenarioStrings = StringUtil.split(
			fileContent, PoshiProseScenario.KEYWORDS);

		for (String poshiProseScenarioString : poshiProseScenarioStrings) {
			_poshiProseScenarios.add(
				new PoshiProseScenario(poshiProseScenarioString));
		}
	}

	public String getFileName() {
		return _fileName;
	}

	public Element toElement() {
		Element definitionElement = Dom4JUtil.getNewElement("definition");

		for (PoshiProseScenario poshiProseScenario : _poshiProseScenarios) {
			definitionElement.add(poshiProseScenario.toElement());
		}

		return definitionElement;
	}

	private final String _fileName;
	private final List<PoshiProseScenario> _poshiProseScenarios =
		new ArrayList<>();

}