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

package com.liferay.poshi.runner.script;

import com.liferay.poshi.runner.elements.PoshiNode;
import com.liferay.poshi.runner.util.StringUtil;

/**
 * @author Kenji Heigel
 */
public class PoshiScriptParserException extends Exception {

	public PoshiScriptParserException() {
	}

	public PoshiScriptParserException(String msg) {
		super(msg);
	}

	public PoshiScriptParserException(String msg, PoshiNode poshiNode) {
		super(_formatMessage(msg, poshiNode));

		_poshiNode = poshiNode;
	}

	public PoshiScriptParserException(
		String msg, String poshiScript, PoshiNode parentPoshiNode) {

		super(_formatMessage(msg, poshiScript, parentPoshiNode));

		_poshiNode = parentPoshiNode;
	}

	public PoshiScriptParserException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public PoshiScriptParserException(Throwable cause) {
		super(cause);
	}

	public PoshiNode getPoshiNode() {
		return _poshiNode;
	}

	private static String _formatMessage(String msg, PoshiNode poshiNode) {
		return _formatMessage(
			msg, poshiNode.getFilePath(), poshiNode.getPoshiScriptLineNumber(),
			poshiNode.getPoshiScript());
	}

	private static String _formatMessage(
		String msg, String filePath, int lineNumber, String poshiScript) {

		StringBuilder sb = new StringBuilder();

		sb.append(msg);
		sb.append(" at:\n\t");
		sb.append(filePath);
		sb.append(":");
		sb.append(lineNumber);
		sb.append("\n\t[");
		sb.append(poshiScript.trim());

		sb.append("]");

		return sb.toString();
	}

	private static String _formatMessage(
		String msg, String poshiScript, PoshiNode parentPoshiNode) {

		String parentPoshiScript = parentPoshiNode.getPoshiScript();

		parentPoshiScript = parentPoshiScript.trim();

		int index = parentPoshiScript.indexOf(poshiScript.trim());

		int lineNumber =
			parentPoshiNode.getPoshiScriptLineNumber() +
				StringUtil.count(parentPoshiScript, "\n", index);

		return _formatMessage(
			msg, parentPoshiNode.getFilePath(), lineNumber, poshiScript);
	}

	private PoshiNode _poshiNode;

}