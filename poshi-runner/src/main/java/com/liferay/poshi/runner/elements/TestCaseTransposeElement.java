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

package com.liferay.poshi.runner.elements;

import com.liferay.poshi.runner.util.Validator;

import java.util.List;

import org.dom4j.Element;

/**
 * @author Yi-Chen Tsai
 */
public class TestCaseTransposeElement extends TransposeElement {

	public TestCaseTransposeElement(
		Element baseElement, Element overrideElement,
		String overrideNamespacedClassName) {

		super(baseElement, overrideElement, overrideNamespacedClassName);

		_transpose();
	}

	private void _overrideCommandElements() {
		Element overrideElement = getOverrideElementCopy();

		Element overrideSetUpElement = overrideElement.element("set-up");

		if (Validator.isNotNull(overrideSetUpElement)) {
			Element baseSetUpElement = element("set-up");

			baseSetUpElement.detach();

			add(overrideSetUpElement.createCopy());
		}

		Element overrideTearDownElement = overrideElement.element("tear-down");

		if (Validator.isNotNull(overrideTearDownElement)) {
			Element baseTearDownElement = element("tear-down");

			baseTearDownElement.detach();

			add(overrideTearDownElement.createCopy());
		}

		List<Element> baseCommandElements = elements("command");
		List<Element> overrideCommandElements = overrideElement.elements(
			"command");

		for (Element overrideCommandElement : overrideCommandElements) {
			String overrideCommandName = overrideCommandElement.attributeValue(
				"name");

			for (Element baseCommandElement : baseCommandElements) {
				if (overrideCommandName.equals(
						baseCommandElement.attributeValue("name"))) {

					baseCommandElement.detach();

					break;
				}
			}

			add(overrideCommandElement.createCopy());
		}
	}

	private void _overridePropertyElements() {
		Element overrideElement = getOverrideElementCopy();

		List<Element> basePropertyElements = elements("property");
		List<Element> overridePropertyElements = overrideElement.elements(
			"property");

		for (Element overridePropertyElement : overridePropertyElements) {
			String overridePropertyName =
				overridePropertyElement.attributeValue("name");

			for (Element basePropertyElement : basePropertyElements) {
				if (overridePropertyName.equals(
						basePropertyElement.attributeValue("name"))) {

					basePropertyElement.detach();

					break;
				}
			}

			add(overridePropertyElement.createCopy());
		}
	}

	private void _overrideRootSummaryAttribute() {
		Element overrideElement = getOverrideElementCopy();

		String overrideRootSummary = overrideElement.attributeValue("summary");

		if (Validator.isNotNull(overrideRootSummary)) {
			addAttribute("summary", overrideRootSummary);
		}
	}

	private void _overrideVarElements() {
		Element overrideElement = getOverrideElementCopy();

		List<Element> baseVarElements = elements("var");
		List<Element> overrideVarElements = overrideElement.elements("var");

		for (Element overrideVarElement : overrideVarElements) {
			String overrideVarName = overrideVarElement.attributeValue("name");

			for (Element baseVarElement : baseVarElements) {
				if (overrideVarName.equals(
						baseVarElement.attributeValue("name"))) {

					baseVarElement.detach();

					break;
				}
			}

			add(overrideVarElement.createCopy());
		}
	}

	private void _transpose() {
		Element baseElement = getBaseElementCopy();

		setContent(baseElement.content());

		_overrideCommandElements();
		_overridePropertyElements();
		_overrideRootSummaryAttribute();
		_overrideVarElements();
	}

}