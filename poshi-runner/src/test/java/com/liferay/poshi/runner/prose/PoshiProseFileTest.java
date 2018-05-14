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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Yi-Chen Tsai
 */
public class PoshiProseFileTest extends PoshiProseTest {

	@Before
	public void setUp() throws Exception {
		String[] includes = {"**/*macro"};

		PoshiRunnerContext.readFiles(includes, testDirName);

		_poshiProseFile = new PoshiProseFile(
			_POSHI_PROSE_FILE_NAME, read(getTestDir(), _POSHI_PROSE_FILE_NAME));
	}

	@After
	public void tearDown() throws Exception {
		PoshiRunnerContext.clear();
	}

	@Test
	public void testGetNameAndContent() throws Exception {
		System.out.println(
			Dom4JUtil.format(_poshiProseFile.getDefinitionElement()));
	}

	private static final String _POSHI_PROSE_FILE_NAME =
		"PoshiProseFileTest.prose";

	private PoshiProseFile _poshiProseFile;

}