package com.akatec.aps.reusablefunctions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Reportsold extends BaseTest {
    public static ExtentReports extent;
    public static ExtentTest test;
    public static ExtentTest parentTest;
    public static ExtentTest childTest;
    public static int stepCount = 0;
    public static String assertType;

    public Reportsold(String path) {
        extent = new ExtentReports(path, true, DisplayOrder.OLDEST_FIRST);
        extent.loadConfig(new File(System.getProperty("user.dir") + "/report-config.xml"));

    }

    public static ExtentTest startTest(String testName, String authorName) {
        test = extent.startTest(testName).assignAuthor(authorName);
        return test;
    }

    public static ExtentTest startParentTest(String testName, String authorName) {
        parentTest = extent.startTest(testName).assignAuthor(authorName);
        return parentTest;
    }

    public static ExtentTest startChildTest(String testName, String authorName) {
        childTest = extent.startTest(testName).assignAuthor(authorName);
        return childTest;
    }

    public static void appendChildTest() {
        parentTest.appendChild(childTest);
    }

    /**
     *
     * @param strScreenshotLocation
     */
    public static void attachScreenShots(String strScreenshotLocation) {
        try {
            childTest.addScreenCapture(Screen_shot_Loc(strScreenshotLocation));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     //* @param Loc_Name
     * @return Loc_Name
     * @throws IOException
     */
    public static String Screen_shot_Loc(String strScreenshotLocation) throws IOException {
        java.io.File scrFile = ((TakesScreenshot) Driver.Instance).getScreenshotAs(OutputType.FILE);
        // String LOC = Screen_shot_Loc(strScreenshotLocation);

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String dateNow = formatter.format(currentDate.getTime());

        String pathScreenshot = BaseTest.workingDir + strScreenshotLocation + "/screenshot_" + dateNow
                + UtilityFunctions.getMethodName() + ".jpeg";
        FileUtils.copyFile(scrFile, new File(pathScreenshot));
        System.out.println(pathScreenshot);
        // System.out.println(scrFile.g);
        return pathScreenshot;
    }

    public static void addStep(String Status, int Step, String stepDetails) throws IOException {

        if (Status.contains("PASS")) {
            childTest.log(LogStatus.PASS, "Step " + Step, stepDetails
                    + childTest.addScreenCapture(Screen_shot_Loc(getPropertyFromConfig("screenshotLocation"))));
            Assert.assertTrue(true);
        }
        if (Status.contains("FAIL")) {
            childTest.log(LogStatus.FAIL, "Step " + Step, stepDetails
                    + childTest.addScreenCapture(Screen_shot_Loc(getPropertyFromConfig("screenshotLocation"))));
            // Assert.assertTrue(false);
        }

        if (Status.contains("INFO")) {
            childTest.log(LogStatus.INFO, "Step " + Step, stepDetails);
            attachScreenShots(getPropertyFromConfig("screenshotLocation"));
        }

        if (Status.contains("WARN")) {
            childTest.log(LogStatus.WARNING, "Step " + Step, stepDetails);
            attachScreenShots(getPropertyFromConfig("screenshotLocation"));
        }

    }

    public static void addStepNew(String Status, String stepDetails) throws IOException {

        stepCount++;

        if (Status.contains("PASS")) {
            childTest.log(LogStatus.PASS, "Step " + stepCount, stepDetails
                    + childTest.addScreenCapture(Screen_shot_Loc(getPropertyFromConfig("screenshotLocation"))));
            Assert.assertTrue(true);
        }
        if (Status.contains("FAIL")) {
            childTest.log(LogStatus.FAIL, "Step " + stepCount, stepDetails
                    + childTest.addScreenCapture(Screen_shot_Loc(getPropertyFromConfig("screenshotLocation"))));
            if (assertType.equalsIgnoreCase("hard")) {
                Assert.assertTrue(false);
            }

        }

        if (Status.contains("INFO")) {
            childTest.log(LogStatus.INFO, "Step " + stepCount, stepDetails);
            attachScreenShots(getPropertyFromConfig("screenshotLocation"));
        }

        if (Status.contains("WARN")) {
            childTest.log(LogStatus.WARNING, "Step " + stepCount, stepDetails);
            attachScreenShots(getPropertyFromConfig("screenshotLocation"));
        }

        System.out.println("step count is " + stepCount);
    }

    public void endTest(ExtentTest test) {
        extent.endTest(test);
    }

    public static void endChildTest() {
        extent.endTest(childTest);
    }

    public static void endParentTest() {
        extent.endTest(parentTest);
    }

    public static void writeToReport() {
        extent.flush();
    }

    public static void close() {
        extent.close();
    }
}
