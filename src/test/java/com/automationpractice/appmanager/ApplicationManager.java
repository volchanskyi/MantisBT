package com.automationpractice.appmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationManager {
	private final Properties properties;
	private WebDriver wd;
	private String browser;
	private RegistrationHelper registrationHelper;

	// Init Logger for ApplicationManager.class
	final private Logger appManagerlogger = LoggerFactory.getLogger(ApplicationManager.class);

	public ApplicationManager(String browser) {
		this.browser = browser;
		properties = new Properties();

	}

	public void init() {
		String target = System.getProperty("target", "local");
		try {
			properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
		} catch (FileNotFoundException e) {
			appManagerlogger.error(e.toString());
		} catch (IOException e) {
			appManagerlogger.error(e.toString());
		}
	}

	public void stop() {
		// Lazy init
		if (wd != null) {
			wd.quit();
		}

	}

	public HttpSession newSession() {
		return new HttpSession(this);
	}

	public String getProperty(String key) {
		return properties.getProperty(key);

	}

	public RegistrationHelper registration() throws MalformedURLException {
		// Lazy init
		if (registrationHelper == null) {
			registrationHelper = new RegistrationHelper(this);
		}
		return registrationHelper;
	}

	public WebDriver getDriver() {
		// Load locators
		try {
			properties.load(new FileReader(new File(String.format("src/test/resources/locator.properties"))));
		} catch (FileNotFoundException e) {
			// If the the property file cant be found
			appManagerlogger.error(e.toString());
		} catch (IOException e) {
			// FS access error
			appManagerlogger.error(e.toString());
		}

		System.setProperty("webdriver.chrome.driver", "src/test/resources/webdrivers/pc/chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", "src/test/resources/webdrivers/pc/geckodriver.exe");
		System.setProperty("webdriver.edge.driver", "src/test/resources/webdrivers/pc/MicrosoftWebDriver.exe");
		System.setProperty("webdriver.ie.driver", "src/test/resources/webdrivers/pc/IEDriverServer.exe");

		// If we dont use selenium server then run local browser
		if ("".equals(properties.getProperty("selenium.server"))) {
			// Lazy init
			if (wd == null) {
				if (browser.equals(BrowserType.FIREFOX)) {
					wd = new FirefoxDriver();
				} else if (browser.equals(BrowserType.CHROME)) {
					wd = new ChromeDriver();
				} else if (browser.equals(BrowserType.IE)) {
					InternetExplorerOptions options = new InternetExplorerOptions();
					options.ignoreZoomSettings();
					options.destructivelyEnsureCleanSession();
					options.introduceFlakinessByIgnoringSecurityDomains();
					wd = new InternetExplorerDriver(options);
				} else if (browser.equals(BrowserType.EDGE)) {
					EdgeOptions options = new EdgeOptions();
					options.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
					options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					options.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
					options.setCapability("InPrivate", true);
					wd = new EdgeDriver(options);
				}
				wd.get(properties.getProperty("web.baseUrl"));
			}
		} else {
			// Run tests remotely
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setBrowserName(browser);
			capabilities.setPlatform(Platform.fromString(System.getProperty("platform", "win10")));
			try {
				wd = new RemoteWebDriver(new URL(properties.getProperty("selenium.server")), capabilities);
			} catch (MalformedURLException e) {
				// If loading property failed
				appManagerlogger.error(e.toString());
			}
		}
		return wd;
	}

	public byte[] takeScreenshot() {
		// Lazy init
		// no need to take screenshot if there`s no UI involved
		if (wd != null) {
			return ((TakesScreenshot) wd).getScreenshotAs(OutputType.BYTES);
		} else
			return null;
	}

}
