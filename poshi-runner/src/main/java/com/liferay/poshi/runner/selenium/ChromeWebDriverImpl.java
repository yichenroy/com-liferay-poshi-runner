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

package com.liferay.poshi.runner.selenium;

import org.openqa.selenium.WebDriver;

/**
 * @author Brian Wing Shun Chan
 */
public class ChromeWebDriverImpl extends BaseWebDriverImpl {

	public ChromeWebDriverImpl(String browserURL, WebDriver webDriver) {
		super(browserURL, webDriver);
	}

	@Override
	public String getSelectedLabel(String selectLocator, String timeout) {
		String text = super.getSelectedLabel(selectLocator, timeout);

		return text.trim();
	}

	@Override
	public String[] getSelectedLabels(String selectLocator) {
		String[] selectedLabels = super.getSelectedLabels(selectLocator);

		for (int i = 0; i < selectedLabels.length; i++) {
			selectedLabels[i] = selectedLabels[i].trim();
		}

		return selectedLabels;
	}

	@Override
	public String getText(String locator, String timeout) throws Exception {
		String text = super.getText(locator, timeout);

		return text.trim();
	}

}