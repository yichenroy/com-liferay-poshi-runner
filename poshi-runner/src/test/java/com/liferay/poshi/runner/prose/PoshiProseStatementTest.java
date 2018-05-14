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

import com.liferay.poshi.runner.PoshiRunnerContext;
import com.liferay.poshi.runner.util.Dom4JUtil;
import com.liferay.poshi.runner.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Yi-Chen Tsai
 */
public class PoshiProseStatementTest extends PoshiProseTest {

	@Before
	public void setUp() throws Exception {
		String[] includes = {"**/*macro"};

		PoshiRunnerContext.readFiles(includes, testDirName);

		String poshiProseStatementTestContent = read(
			getTestDir(), "/PoshiProseStatementTest.prose");

		List<String> poshiProseStatementStrings = StringUtil.splitByKeys(
			poshiProseStatementTestContent,
			PoshiProseStatement.PROSE_STATEMENT_KEYWORDS);

		for (String poshiProseStatementString : poshiProseStatementStrings) {
			_poshiProseStatements.add(
				new PoshiProseStatement(poshiProseStatementString));
		}
	}

	@After
	public void tearDown() throws Exception {
		PoshiRunnerContext.clear();
	}

	@Test
	public void testGetExecuteElement() throws Exception {
		for (PoshiProseStatement poshiProseStatement : _poshiProseStatements) {
			System.out.println(
				Dom4JUtil.format(poshiProseStatement.getExecuteElement()));
		}
	}

	private final List<PoshiProseStatement> _poshiProseStatements =
		new ArrayList<>();

}