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

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import junit.framework.TestCase;

/**
 * @author Yi-Chen Tsai
 */
public class PoshiProseTest extends TestCase {

	protected PoshiProseTest() {
		testDir = new File(testDirName);

		if (!testDir.exists()) {
			throw new RuntimeException(
				"Test directory does not exist: " + testDirName);
		}
	}

	protected File getTestDir() {
		return testDir;
	}

	protected String read(File file) throws IOException {
		return new String(Files.readAllBytes(Paths.get(file.toURI())));
	}

	protected String read(File dir, String fileName) throws IOException {
		return read(new File(dir, fileName));
	}

	protected File testDir;
	protected String testDirName =
		"src/test/resources/com/liferay/poshi/runner/dependencies/prose/";

}