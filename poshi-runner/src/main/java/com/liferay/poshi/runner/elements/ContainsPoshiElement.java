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

import org.dom4j.Element;

/**
 * @author Kenji Heigel
 */
public class ContainsPoshiElement extends PoshiElement {
	@Override
	public PoshiElement clone(Element element) {
		return null;
	}

	@Override
	public PoshiElement clone(
	PoshiElement parentPoshiElement, String poshiScript) {
		return null;
	}

	@Override
	public void parsePoshiScript(String poshiScript) {
	}


	@Override
	public String toPoshiScript() {
		return null;
	}

	protected ContainsPoshiElement() {
	}

	protected ContainsPoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected ContainsPoshiElement(
		PoshiElement parentPoshiElement, String poshiScript) {

		super(_ELEMENT_NAME, parentPoshiElement, poshiScript);
	}

	@Override
	protected String getBlockName() {
		return _ELEMENT_NAME;
	}

	private static final String _ELEMENT_NAME = "contains";

}