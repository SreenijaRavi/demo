package com.akatec.aps.reusablefunctions;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import com.akatec.aps.pages.LoginPage;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;



import com.relevantcodes.extentreports.ExtentTest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;



public class BaseTest extends Driver {
	protected static Properties prop;
	protected static FileInputStream input;
	protected static String strTestName;
	public static String workingDir;
	public static ExtentTest parentTest;
	public static ExtentTest childTest;
	public static String testName;
	public static boolean executionTypeWithPortal = false;

	@BeforeSuite
	public synchronized void beforeSuite() throws Exception {
		System.out.println("Suited started");
		// UtilityFunctions.reportInit();
		if (getPropertyFromConfig("fromPortal").equalsIgnoreCase("true")) {
			executionTypeWithPortal = true;
		//	TestData.prepareTestData();
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
			initialize(browser);

			testName = test.getName();
			Reports.startParentTest(testName.toUpperCase(), System.getProperty("user.name"));
			if (this.getClass().getSimpleName().contains("Watchtower")) {
				System.out.println("Do nothing");
				LoginPage.parentWindow = Driver.Instance.getWindowHandle();
			} else {
				LoginPage.goTo();
			}
		}
		else if (executionTypeWithPortal) {
			initialize(browser);
			testName = test.getName();
			Reports.startParentTest(testName.toUpperCase(), System.getProperty("user.name"));
			if (this.getClass().getSimpleName().contains("Antarctica")) {
				System.out.println("Do nothing");
				LoginPage.parentWindow = Driver.Instance.getWindowHandle();
			} else {
				LoginPage.goTo();
			}
		}else {
			testName = test.getName();
			ExcelUtils.setExcelFile("/TestData/TestData.xlsx", this.getClass().getSimpleName());
			System.out.println("Test Data loaded :");
			executionTypeWithPortal = false;
			Reports.startParentTest(testName.toUpperCase(), System.getProperty("user.name"));
//			if (this.getClass().getSimpleName().contains("Watchtower")) {
//				System.out.println("Do nothing");
//			} else {
//				LoginPage.goTo();
//			}
		}
	}

	@Parameters("browser")
	@BeforeMethod
	public synchronized void beforeEveryMethod(Method method, String browser) throws Exception {

			initialize(browser);
			LoginPage.parentWindow = Driver.Instance.getWindowHandle();
			strTestName = method.getName();
			System.out.println("strTestName: " + strTestName);
			Reports.startChildTest(this.getClass().getSimpleName().toUpperCase() + " :: " + strTestName,
					System.getProperty("user.name"));


	}

	@AfterMethod
	public void cleanUpMethod() {
		if (executionTypeWithPortal) {
			if (Driver.Instance.getWindowHandle().equals(LoginPage.parentWindow))
				Driver.Instance.quit();
			UtilityFunctions.switchHomepage();
		} else {
			Driver.Instance.quit();
		}
		Reports.appendChildTest();
		Reports.extent.endTest(Reports.getChildTest());
		Reports.stepCount = 0;
	}

	@AfterTest
	public synchronized void cleanUp() {
		if (executionTypeWithPortal) {
			if (Driver.Instance.getWindowHandle().equals(LoginPage.parentWindow)) ;
			//	Driver.Instance.close();
			//UtilityFunctions.switchHomepage();
		} else {
			// Driver.Instance.close();
		}
		Reports.extent.endTest(Reports.getParentTest());
		Reports.extent.flush();
	}

	@AfterSuite
	public synchronized void afterSuite() {	
		
//System.out.println("Deleting Index");
////	String elasticIndexForm = "https://198.18.105.150:9200/antarctica-akamill-availability*";
//String elasticIndexForm = "https://198.18.105.150:9200/antarctica-akamill-*";
// JsonPath jsonPathEvaluator;
//	Utils util = new Utils();
//	   RestAssured.baseURI = elasticIndexForm ;
//    jsonPathEvaluator = RestAssured.given().header( "Content-Type", "application/json").delete().jsonPath();
//	 util.checkIndexDeletion(jsonPathEvaluator);

		try {
		//	Driver.Instance.quit();
		} catch (Exception e) {
			System.out.println("No driver found");
		} finally {
//			Reports.close();
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
	
	public static String getTodayDate() {
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/hh:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String strDate = dateFormat.format(date);
		System.out.println("Converted String: " + strDate);
		return strDate;
	}
	
	public static String oneHourBackTime() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/hh:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// remove next line if you're always using the current time.
		Date currentDate = Calendar.getInstance().getTime();
		cal.setTime(currentDate);
		cal.add(Calendar.HOUR, -1);
		Date oneHourBack = cal.getTime();
		String strDate = dateFormat.format(oneHourBack);
		return strDate;
	}

	
	

}
