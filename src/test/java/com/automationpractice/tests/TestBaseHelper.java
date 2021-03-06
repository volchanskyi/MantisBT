package com.automationpractice.tests;

import java.security.InvalidParameterException;

import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automationpractice.appmanager.ApplicationManager;

abstract class TestBaseHelper {

	// Inti App Manager and set browser type in local runs
	protected static final ApplicationManager APP = new ApplicationManager(
			// for local GUI tests
			System.getProperty("browser", BrowserType.CHROME));

	// Init Logger for TestBase.class
	 protected static final Logger TEST_BASE_LOGGER = LoggerFactory.getLogger(TestBase.class);

	// Check "Debug" property value
	protected boolean isDebugTestsEnabled() {
		try {
			if (System.getProperty("debug") == null || System.getProperty("debug").isEmpty()
					|| System.getProperty("debug").contains("disabled")) {
				return false;
			} else if (System.getProperty("debug").equalsIgnoreCase("enabled"))
				return true;
			else
				return false;
		} catch (InvalidParameterException e) {
			TEST_BASE_LOGGER.error(e.toString());
		} catch (IllegalArgumentException e) {
			TEST_BASE_LOGGER.error(e.toString());
		}
		return false;

	}

}
