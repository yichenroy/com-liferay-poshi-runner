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

package com.liferay.poshi.runner.logger;

import com.liferay.poshi.runner.PoshiRunnerVariablesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.dom4j.Element;

/**
 * @author Leslie Wong
 */
public class PoshiElementLogger {

	public static void emptyExecutionStack() {
		_executionStack = new Stack<>();
	}

	public static void fail(Element element, Exception e) {
		PoshiLogEntry poshiLogEntry = new PoshiLogEntry(
			element, null, "fail",
			PoshiRunnerVariablesUtil.getCommandMapVariables());

		poshiLogEntry.setExecutionException(e);

		_addPoshiLogEntry(poshiLogEntry);
	}

	public static void pass(Element element) {
		pass(element, null);
	}

	public static void pass(Element element, String event) {
		PoshiLogEntry poshiLogEntry = new PoshiLogEntry(
			element, event, "pass",
			PoshiRunnerVariablesUtil.getCommandMapVariables());

		_addPoshiLogEntry(poshiLogEntry);
	}

	public static void popExecutionStack() {
		if (_executionStack.empty()) {
			return;
		}

		PoshiLogEntry poshiLogEntry = _executionStack.pop();

		PoshiLogEntry lastChildLogEntry = poshiLogEntry.getLastChildLogEntry();

		if (lastChildLogEntry != null) {
			String status = lastChildLogEntry.getStatus();

			if (!status.equals("fail")) {
				poshiLogEntry.setStatus("pass");
			}
		}
	}

	public static void pushExecutionStack(Element element) {
		PoshiLogEntry poshiLogEntry = new PoshiLogEntry(
			element, null, "pending",
			PoshiRunnerVariablesUtil.getCommandMapVariables());

		_addPoshiLogEntry(poshiLogEntry);

		_executionStack.push(poshiLogEntry);
	}

	public static void startLog() {
		_executionStack = new Stack<>();
		_poshiLogEntries = new ArrayList<>();
	}

	public static void updateCurrentPoshiLogEntryEvent(String event) {
		if (_executionStack.empty()) {
			throw new RuntimeException(
				"Failed to update execution stack with event, the execution " +
					"stack is empty");
		}

		PoshiLogEntry currentPoshiLogEntry = _executionStack.peek();

		PoshiLogEntry lastChildLogEntry =
			currentPoshiLogEntry.getLastChildLogEntry();

		lastChildLogEntry.setEvent(event);
	}

	public static void warn(Element element, Exception e) {
		PoshiLogEntry poshiLogEntry = new PoshiLogEntry(
			element, e.getMessage(), "warn",
			PoshiRunnerVariablesUtil.getCommandMapVariables());

		poshiLogEntry.setExecutionException(e);

		_addPoshiLogEntry(poshiLogEntry);
	}

	private static void _addPoshiLogEntry(PoshiLogEntry poshiLogEntry) {
		if (!_executionStack.empty()) {
			PoshiLogEntry executingPoshiLogEntry = _executionStack.peek();

			executingPoshiLogEntry.addToChildPoshiLogEntries(poshiLogEntry);
		}
		else {
			_poshiLogEntries.add(poshiLogEntry);
		}
	}

	private static Stack<PoshiLogEntry> _executionStack;
	private static List<PoshiLogEntry> _poshiLogEntries;

}