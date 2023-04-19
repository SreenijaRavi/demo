package com.akatec.aps.reusablefunctions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.akatec.aps.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;



public class UtilityFunctions {
	public static Actions action;
	public static Reportsold report;

	public static SoftAssert softAssert = new SoftAssert();
	private static String SQL2_AGREEGATOR_NOCC = "sql2 --csv -q mega.dev.aggsets.akadns.net";
	private static String queryOutput;
	private static String[] queryOutputArray;
	private static List<String> tableValuesLocal;
	private static By sourceTarget    =  By.xpath("//*[@id=\"k.split('_')[2]\"]/span[1]/a");
	private static List<String> previousFwdIp =  new ArrayList<String>();

	public static void reportInit() {
		report = new Reportsold(BaseTest.getPropertyFromConfig("ReportLocation"));

	}

	public static void click(By element) {

		try {
			Thread.sleep(3000);
			Driver.Instance.findElement(element).click();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
	}



	public static void moveToElement(By element) {
		action = new Actions(Driver.Instance);
		action.moveToElement(Driver.Instance.findElement(element)).build().perform();
	}
	public static void moveToElementandClick(By element) {
		action = new Actions(Driver.Instance);
		action.moveToElement(Driver.Instance.findElement(element)).click().perform();
	}

	public static String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	// public static String getMethodNameTest() {
	// return
	// }
	public static void enterText(By element, String text) throws IOException, InterruptedException {
		if (!text.isEmpty()) {
			waitForElement(element, 30);
			Thread.sleep(5000);
			Driver.Instance.findElement(element).click();
			Driver.Instance.findElement(element).clear();
			Driver.Instance.findElement(element).sendKeys(text);
		}
		System.out.println("Text is empty");
	}

	public static boolean isElementPresent(By element) throws IOException {
		boolean bool;
		if (Driver.Instance.findElement(element).isDisplayed()) {
			bool = true;
			System.out.println("Element is present");
		} else {
			System.out.println("Element is not present");
			bool = false;
		}

		return bool;
	}

	public static boolean isElementDisplayed(By element) throws IOException {
		try {
			Driver.Instance.findElement(element);
			System.out.println("Element is displayed");
			return true;
		} catch (NoSuchElementException e) {
			System.out.println("Element is not displayed");
			return false;
		}
	}

	public static void switchNextWindow() throws InterruptedException {
		Thread.sleep(3000);
		for (String winHandle : Driver.Instance.getWindowHandles()) {
			Driver.Instance.switchTo().window(winHandle);
			System.out.println("Window Switched");
		}
	}



	public static String getText(By element) {
		String text = Driver.Instance.findElement(element).getText();
		return text;
	}

	public static String getAttribute(By element, String attribute) {
		String text = Driver.Instance.findElement(element).getAttribute(attribute);
		return text;
	}

	public static String selectByName(By element, String text) {
		Select select = new Select(Driver.Instance.findElement(element));
		select.selectByVisibleText(text);
		return text;
	}

	public static void selectDropDownManual(By element, By dropDownType) throws InterruptedException {
		click(element);
		Thread.sleep(10000);
		click(dropDownType);
	}

	public static String getByAttribute(By element, String attribute) {
		String attributeValue = Driver.Instance.findElement(element).getAttribute(attribute);
		return attributeValue;
	}



	public static void assertText(By element, String text) throws IOException {
		try {
			Driver.Instance.findElement(element).getText();
			Assert.assertEquals(Driver.Instance.findElement(element).getText(), text);
			// Reports.addStep("PASS", step, text+ " is Verified");
		} catch (Exception e) {
			// Reports.addStep("FAIL", step, " Failed to verify " +text +
			// e.getMessage());
		}

	}

	public static void waitForElement(By element, int time) throws IOException, InterruptedException {
		try {
			WebDriverWait wait = (WebDriverWait) new WebDriverWait(Driver.Instance, time);
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
		} catch (Exception e) {
			System.out.println("Element is not present: waited for 60 secs");
		}
	}
	
	
	
	
	


	public static void verifyTextIntitle(String title) throws InterruptedException, IOException {
		// LoginPage.pubCookieLogin();
		softAssert.assertTrue(Driver.Instance.getTitle().contains(title));
	}

	public static List<String> getHeadersfromTable(By tableLocator) {
		List<String> headers = new ArrayList<String>();
		try {
			WebElement element = Driver.Instance.findElement(tableLocator);
			List<WebElement> headersLocators = element.findElements(By.tagName("th"));

			for (WebElement eachLocator : headersLocators) {
				headers.add(eachLocator.getText());
			}
		} catch (Exception e) {

		}
		return headers;
	}

	public static List<String> getValuesfromTable(By tableLocator) {
		List<String> listOfValues = new ArrayList<String>();
		try {
			WebElement element = Driver.Instance.findElement(tableLocator);
			List<WebElement> row = element.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

			String rowValues = "";
			for (WebElement eachRow : row) {
				List<WebElement> column = eachRow.findElements(By.tagName("td"));
				for (WebElement eachColumn : column) {
					rowValues = rowValues + eachColumn.getText() + ",";

				}
				rowValues = rowValues.replaceAll(",$", "");
				listOfValues.add(rowValues);
				rowValues = "";
			}
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
		}
		return listOfValues;
	}
	
	public static List<String> getValuesfromColumns(By tableLocator , int column_num) throws IOException,InterruptedException {
		List<String> listOfValues = new ArrayList<String>();
		Thread.sleep(10000);
		try {
			WebElement element = Driver.Instance.findElement(tableLocator);
			List<WebElement> row = element.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
			String rowValues = "";

			for (WebElement eachRow : row) {
				WebElement column = eachRow.findElements(By.tagName("td")).get(column_num);
				
					rowValues = rowValues + column.getText() ;				
						
			//	rowValues = rowValues.replaceAll(",$", "");
				listOfValues.add(rowValues);
				rowValues = "";
			}
		
		}catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
		}
		return listOfValues ;
	}
	

	public static String getvalueBasedOnColumnAndRow(By tableLocator, int columnNum, int rowNum) {

		String columnValue = null;
		try {
			WebElement element = Driver.Instance.findElement(tableLocator);
			columnValue = element.findElement(By.xpath("/tbody/tr[" + rowNum + "]/td[" + columnNum + "]")).getText();

		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
		}
		return columnValue;

	}

	public static String convertEpocToNormal(String time) {
		double epocTime = Double.valueOf(time) * 1000;
		Date expiry = new Date((long) epocTime);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		df.format(expiry);
		// Date date = df.parse(expiry);
		String strTime = df.format(expiry);
		System.out.println(strTime);
		return strTime;
	}

	public static void handleAlert() {
		try {
			Driver.Instance.switchTo().alert().accept();
		} catch (Exception e) {
			System.out.println("No alert Message");
		}
	}

	public static void handleDuoPush() {
		try {
			Driver.Instance.switchTo().frame("duo_iframe");
			Driver.Instance.findElement(By.xpath("//button[contains(text(),'Send Me a Push')]")).click();
			Thread.sleep(20000);
				
		} catch (Exception e) {
			System.out.println("No Duo push login");
		}
	}

	public static List<String> getListOfValues(By element) throws InterruptedException {
		List<WebElement> webElements = Driver.Instance.findElements(element);
		List<String> values = new ArrayList<String>();
		for (WebElement webElement : webElements) {
			values.add(webElement.getText().toString());
		}
		return values;
	}
	public static List<WebElement> getListOfElements(By element) throws InterruptedException {
		List<WebElement> webElements = Driver.Instance.findElements(element);
		return webElements;
	}
	
	public static WebElement getElement(By element) throws InterruptedException {
	WebElement Element = Driver.Instance.findElement(element);
		return Element;
	}
	
	
	

	public static String getIPFromResponseHeaders(String responseHeaderstext) {
		String ipaddress = null;
		String ipaddressmatch = responseHeaderstext.split("X-Cache:")[1].split("\n")[0];
		Pattern pattern = Pattern.compile("(\\d{1,3}-\\d{1,3}-\\d{1,3}-\\d{1,3})");

		Matcher matcher = pattern.matcher(ipaddressmatch);
		if (matcher.find()) {
			ipaddress = matcher.group();
			System.out.println(matcher.group());
		}
		return ipaddress.replaceAll("-", ".");
	}

	public static String getTextFromAlertAndAcceptAlert(By element) {
		String alertText = null;
		try {
			click(element);
			Thread.sleep(10000);
			alertText = Driver.Instance.switchTo().alert().getText();
			Driver.Instance.switchTo().alert().accept();
			Driver.Instance.switchTo().defaultContent();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alertText;
	}

	public static String getTextFromAlertAndAcceptAlert() throws InterruptedException {
		String alertText = null;
		alertText = Driver.Instance.switchTo().alert().getText();
		Driver.Instance.switchTo().alert().accept();
		Driver.Instance.switchTo().defaultContent();
		return alertText;
	}

	public static void enableorDisableCheckBox(By element, String boo) {
		if (boo.equalsIgnoreCase("TRUE")) {
			click(element);
		}
	}

	public static void focusonElement(By element) {
		WebElement Location = Driver.Instance.findElement(element);

		((JavascriptExecutor) Driver.Instance).executeScript("arguments[0].scrollIntoView(true);", Location);
	}
	
	protected static void HandleSSLPage() throws IOException, InterruptedException
	{
	if(Driver.Instance.getTitle().contains("Warning: Potential Security Risk Ahead"))
	{
	By AdvanceButton = By.id("advancedButton");
	//waitForElement(AdvanceButton, 20);
	click(AdvanceButton);
	By ExceptionAccept = By.id("exceptionDialogButton");
	click(ExceptionAccept);

	}
	}
	
	public static void scrollIntoViewUsingJSExecutor(WebElement element) throws InterruptedException {
		
		 JavascriptExecutor js = ((JavascriptExecutor) Driver.Instance);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		}

	public static void switchHomepage() {
		Driver.Instance.switchTo().window(LoginPage.parentWindow);

	}



//	
	}

