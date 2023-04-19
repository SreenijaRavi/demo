package com.akatec.aps.reusablefunctions;

import java.awt.AWTException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

/**
 * @author sivenkat
 *
 */
public class Driver {

	public static WebDriver Instance;

	// ThreadLocal<WebDriver> Instance = new ThreadLocal<WebDriver>();
	/**
	 * @author sivenkat This method is to initialize and launch the browser
	 * @throws MalformedURLException
	 */
	public static WebDriver initialize(String browser)
			throws InterruptedException, AWTException, MalformedURLException {

		// switch (BaseTest.getPropertyFromConfig("browser")) {
		// ThreadLocal<WebDriver> Instance = new ThreadLocal<WebDriver>();
		// Instance.se
		switch (browser) {
		case "firefox":
			
			System.out.println("Starting f driver");
			
			ProfilesIni profile = new ProfilesIni();
			FirefoxProfile ffProfile = profile.getProfile("ant");
		//	ffProfile.setAcceptUntrustedCertificates(true);
			//ffProfile.setPreference("dom.disable_beforeunload", true); -- To DISABLE POPUP

			
			
			
		//	System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/Drivers/geckodriver");
			//Download Preferences
			//ffProfile.setPreference("browser.download.dir", System.getProperty("user.dir") + "/Download");
		//	ffProfile.setPreference("browser.download.folderList", 2);

			//Set Preference to not show file download confirmation dialogue using MIME types Of different file extension types.
			//ffProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
			//"text/csv,application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml");

			//ffProfile.setPreference( "browser.download.manager.showWhenStarting", false );
			//ffProfile.setPreference( "pdfjs.disabled", true );


			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/Drivers/geckodriver");
			Instance = new FirefoxDriver(ffProfile);
			break;

		case "chrome":

			System.out.println("Starting chrome driver");
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--defaults");
			capabilities.acceptInsecureCerts();
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/Drivers/chromedriver");
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			Instance = new ChromeDriver(capabilities);
			break;

		case "safari":

			System.out.println("Starting safari driver");
			Instance = new SafariDriver();
			break;

		case "Rfirefox":
			System.out.println("Starting Firefox driver in remote ");
			// DesiredCapabilities cap = DesiredCapabilities.chrome();
			ProfilesIni profile2 = new ProfilesIni();
			FirefoxProfile ffProfile2 = profile2.getProfile("default");
			ffProfile2.setAcceptUntrustedCertificates(true);
			DesiredCapabilities cap = DesiredCapabilities.firefox();
			cap.setCapability(FirefoxDriver.PROFILE, ffProfile2);
			// cap.setBrowserName("firefox");
			// cap.setAcceptInsecureCerts(false);
			// cap.acceptInsecureCerts();
			Instance = new RemoteWebDriver(new URL("http://172.19.220.80:4042/wd/hub"), cap);
			break;

		case "Rchrome":
			System.out.println("Starting Chrome driver in Remote");
			DesiredCapabilities rCapabilities = DesiredCapabilities.chrome();
			ChromeOptions rOptions = new ChromeOptions();
			rOptions.addArguments("--defaults");
			rCapabilities.acceptInsecureCerts();
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/Drivers/chromedriver");
			rCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			rCapabilities.setCapability(ChromeOptions.CAPABILITY, rOptions);
			Instance = new RemoteWebDriver(new URL("http://172.19.220.80:4042/wd/hub"), rCapabilities);
			break;

		default:
			System.out.println("No driver has been assigned");
			break;

		}

		turnOnWait();
		// Driver.Instance.manage().window().maximize();
		return Instance;
	}

	// public WebDriver getDriver() {
	// return this.Instance;
	// }

	/**
	 * @author sivenkat This method is to turn on the implicit wait.
	 */
	public static void turnOnWait() {
		Instance.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	/**
	 * @author sivenkat This method is to turn off the implicit wait.
	 */
	public static void turnOffWait() {
		Instance.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	/**
	 * @author sivenkat This method is to close the one instance of the browser.
	 */
	public static void close() {
		Instance.close();
	}

	/**
	 * @author sivenkat This method is to quit the all instance of the browser.
	 */
	public static void quit() {
		Instance.quit();

	}

}
