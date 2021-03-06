package com.automationpractice.appmanager;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelperBase {

	private ApplicationManager app;
	private WebDriver wd;
	private WebDriverWait wait;

	// Init Logger for ApplicationManager.class
	protected static final Logger HELPER_BASE_LOGGER = LoggerFactory.getLogger(HelperBase.class);

	HelperBase(ApplicationManager app) throws MalformedURLException {
		try {
			this.app = app;
			this.setWd(app.initWebDriver());
			// Maximize browser session window
			this.getWd().manage().window().maximize();
			// set EXPLICIT timeouts
			this.wait = new WebDriverWait(this.getWd(), 15);
			// Set timeout for Async Java Script
			this.getWd().manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
			this.getWd().manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		} catch (WebDriverException e) {
			HELPER_BASE_LOGGER.error(e.toString()
					+ "Driver settings set issue. Check driver management section. Screen resolution settings: "
					+ this.getWd().manage().window().getPosition() + this.getWd().manage().window().getSize());
		}
	}

	protected void navigateTo(String url) {
		this.getWd().navigate().to(url);
		wait.until(ExpectedConditions.urlToBe(url));
	}

	protected void click(By locator) {
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	protected void type(By locator, String text) {
		click(locator);
		if (text != null) {
			String existingText = wait.until(ExpectedConditions.visibilityOfElementLocated(locator))
					.getAttribute("value");
			// wd.findElement(locator).getAttribute("value");
			if (!text.equals(existingText)) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).clear();
				wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(text);
			}
		}
	}

	// select from dd menu
	protected void selectFromDDM(By locator, String value) {
		// WebElement ddMenu = waitForPresenceOfElement(p, objectName, objectType);

		Select choose = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(locator)));
		// Selecting value
		choose.selectByVisibleText(value);

	}

	// Get title by using embedded method
	protected String getPageTitle(String title) {
		wait.until(ExpectedConditions.titleIs(title));
		return getWd().getTitle();
	}

	protected String getActiveOverlay(By locator, String overlay) {
		wait.until(ExpectedConditions.textToBe(locator, overlay));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getAttribute("value");
	}

	public String useProperty(String property) {
		return app.getProperty(property);
	}

	public WebDriver getWd() {
		return wd;
	}

	public void setWd(WebDriver wd) {
		this.wd = wd;
	}

}
