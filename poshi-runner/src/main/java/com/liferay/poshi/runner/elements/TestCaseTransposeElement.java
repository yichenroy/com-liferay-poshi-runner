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

			add(overridePropertyElement);
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

			add(overrideVarElement);
		}
	}

	private void _transpose() {
		Element baseElement = getBaseElementCopy();

		setContent(baseElement.content());

		_overridePropertyElements();
		_overrideVarElements();
	}

}