package com.akatec.aps.reusablefunctions;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import java.util.Properties;

import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.akatec.aps.pages.LoginPage;
import com.relevantcodes.extentreports.ExtentTest;

public class BaseTest2 {
	protected static Properties prop;
	protected static FileInputStream input;
	protected static String strTestName;
	public static String workingDir;
	public static ExtentTest parentTest;
	public static ExtentTest childTest;
	public static String testName;
	public static boolean executionTypeWithPortal = false;

	@BeforeSuite
	public void beforeSuite() throws Exception {
		System.out.println("Suited started");
		UtilityFunctions.reportInit();
		if (BaseTest2.getPropertyFromConfig("fromPortal").equalsIgnoreCase("true")) {
			executionTypeWithPortal = true;
			// TestData.prepareTestData();
		} else {
			// ExcelUtils.setExcelFile("/TestData/TestData.xlsx", "watchtower");
			// System.out.println("Test Data loaded :");
			executionTypeWithPortal = false;
		}

	}

	@Parameters("browser")
	@BeforeTest
	public void init(ITestContext test, String browser) throws Exception {

		if (executionTypeWithPortal) {
			Driver.initialize(browser);
			testName = test.getName();
			// Reportsold.startParentTest(testName.toUpperCase(),
			// System.getProperty("user.name"));
			if (this.getClass().getSimpleName().contains("Watchtower")) {
				System.out.println("Do nothing");
				LoginPage.parentWindow = Driver.Instance.getWindowHandle();
			} else {
				LoginPage.goTo();
			}
		} else {
			testName = test.getName();
			ExcelUtils.setExcelFile("/TestData/TestData.xlsx", this.getClass().getSimpleName());
			System.out.println("Test Data loaded :");
			executionTypeWithPortal = false;
			// Reportsold.startParentTest(testName.toUpperCase(),
			// System.getProperty("user.name"));
			// if (this.getClass().getSimpleName().contains("Watchtower")) {
			// System.out.println("Do nothing");
			// } else {
			// LoginPage.goTo();
			// }
		}
	}

	@Parameters("browser")
	@BeforeMethod
	public void beforeEveryMethod(Method method, String browser) throws Exception {
		if (executionTypeWithPortal) {
			System.out.println("Entered Before Method");
			strTestName = method.getName();
			// Reportsold.startChildTest(this.getClass().getSimpleName().toUpperCase()
			// + " :: " + strTestName,
			// System.getProperty("user.name"));
		} else {
			Driver.initialize(browser);
			LoginPage.parentWindow = Driver.Instance.getWindowHandle();
			strTestName = method.getName();
			ExcelUtils.getDataBasedOnTestCaseNew(strTestName);
			// Reportsold.startChildTest(this.getClass().getSimpleName().toUpperCase()
			// + " :: " + strTestName,
			// System.getProperty("user.name"));
		}

	}

	@AfterMethod
	public void cleanUpMethod() {
		if (executionTypeWithPortal) {
			if (Driver.Instance.getWindowHandle().equals(LoginPage.parentWindow))
				Driver.close();
			UtilityFunctions.switchHomepage();
		} else {
			//Driver.Instance.close();
		}
		// Reportsold.appendChildTest();
		// Reportsold.endChildTest();
		// Reportsold.stepCount = 0;
	}

	@AfterTest
	public void cleanUp() {
		if (executionTypeWithPortal) {
			if (Driver.Instance.getWindowHandle().equals(LoginPage.parentWindow))
				Driver.close();
			UtilityFunctions.switchHomepage();
		} else {
			// Driver.Instance.close();
		}
		// Reportsold.endParentTest();
		// Reportsold.writeToReport();
	}

	@AfterSuite
	public void afterSuite() {
		try {
			Driver.quit();
		} catch (Exception e) {
			System.out.println("No driver found");
		} finally {
			// Reportsold.close();
			System.out.println("Suited Endend");
		}
	}

	public static String getPropertyFromConfig(String propertyName) {
		try {
			workingDir = System.getProperty("user.dir");
			input = new FileInputStream(workingDir + "//config.properties");
			prop = new Properties();
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty(propertyName);
	}

}
