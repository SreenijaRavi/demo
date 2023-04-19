package com.akatec.aps.reusablefunctions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Reports extends BaseTest {
    static Map<Integer, ExtentTest> extentTestMapChild = new HashMap<Integer, ExtentTest>();
    static Map<Integer, ExtentTest> extentTestMapParent = new HashMap<Integer, ExtentTest>();
    static ExtentReports extent = ExtentManager.getReporter();
    protected static ExtentTest parentTestEx;
    private static ExtentTest childTestEx;
    static int stepCount = 0;
    public static String assertType;
    private static int parentThreadId;
    private static int childThreadId;

    // public static synchronized ExtentTest getTest() {
    // return (ExtentTest) extentTestMap.get((int) (long)
    // (Thread.currentThread().getId()));
    // }


    public static synchronized ExtentTest getParentTest() {

        return extentTestMapParent.get(parentThreadId);
    }

    public static synchronized ExtentTest getChildTest() {
        return extentTestMapChild.get(childThreadId);
    }

    public static synchronized ExtentTest startParentTest(String testName, String desc) {
        parentTestEx = extent.startTest(testName, desc);
        parentThreadId = (int) Thread.currentThread().getId();
        extentTestMapParent.put(parentThreadId, parentTestEx);
        System.out.println("Started Parent Thread id:" + parentThreadId);
        return parentTestEx;
    }

    public static synchronized ExtentTest startChildTest(String testName, String desc) {
        childTestEx = extent.startTest(testName, desc);
        childThreadId = (int) Thread.currentThread().getId();
        extentTestMapChild.put(childThreadId, childTestEx);
        System.out.println("Started Child Thread id:" + childThreadId);
        return childTestEx;
    }

    public static synchronized void appendChildTest() {
        getParentTest().appendChild(getChildTest());
    }

    public  synchronized static void addStepNew(String Status, String stepDetails) throws IOException {

        stepCount++;

        if (Status.contains("PASS")) {
            getChildTest().log(LogStatus.PASS, "Step " + stepCount, stepDetails
                    + getChildTest().addScreenCapture(Screen_shot_Loc(getPropertyFromConfig("screenshotLocation"))));
            Assert.assertTrue(true);
        }
        if (Status.contains("FAIL")) {
            getChildTest().log(LogStatus.FAIL, "Step " + stepCount, stepDetails
                    + getChildTest().addScreenCapture(Screen_shot_Loc(getPropertyFromConfig("screenshotLocation"))));
            if (assertType.equalsIgnoreCase("hard")) {
                Assert.assertTrue(false);
            }

        }

        if (Status.contains("INFO")) {
            getChildTest().log(LogStatus.INFO, "Step " + stepCount, stepDetails);
            attachScreenShots(getPropertyFromConfig("screenshotLocation"));
        }

        if (Status.contains("WARN")) {
            getChildTest().log(LogStatus.WARNING, "Step " + stepCount, stepDetails);
            attachScreenShots(getPropertyFromConfig("screenshotLocation"));
        }

        System.out.println("step count is " + stepCount);
    }

    /**
     *
     * @param strScreenshotLocation
     */
    public static void attachScreenShots(String strScreenshotLocation) {
        try {
            childTestEx.addScreenCapture(Screen_shot_Loc(strScreenshotLocation));
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
}
