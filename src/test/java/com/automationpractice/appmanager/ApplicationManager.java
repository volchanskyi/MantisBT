package com.automationpractice.appmanager;

import java.io.File;
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

public class ApplicationManager {
	private final Properties properties;
	private WebDriver wd;

	private String browser;
	private RegistrationHelper registrationHelper;
	private PdpHelper pdpHelper;

	public ApplicationManager(String browser) {
		this.browser = browser;
		properties = new Properties();

	}

	// Refactor (add params to choose browser types)
	public void init() throws IOException {
		String target = System.getProperty("target", "local");		
		properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
		System.setProperty("ui", "enabled");
		if (System.getProperty("ui").equalsIgnoreCase("enabled")) {
			properties.load(new FileReader(new File(String.format("src/test/resources/locator.properties"))));
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
	
	public PdpHelper pdp() throws MalformedURLException {
		// Lazy init
		if (pdpHelper == null) {
			pdpHelper = new PdpHelper(this);
		}
		return pdpHelper;
	}

	public WebDriver getDriver() throws MalformedURLException {
		// Set path to the drivers
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
			wd = new RemoteWebDriver(new URL(properties.getProperty("selenium.server")), capabilities);
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
