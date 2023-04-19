package com.akatec.aps.pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.akatec.aps.reusablefunctions.BaseTest;
import com.akatec.aps.reusablefunctions.Driver;
import com.akatec.aps.reusablefunctions.Reports;
import com.akatec.aps.reusablefunctions.UtilityFunctions;

public class Pages extends UtilityFunctions {

    private static By userGuideLink = By.xpath("//*[contains(text(),'User Guide')]");
    private static By enhancementLink = By.xpath("//*[contains(text(),'Enhancement')]");
    private static By bugLink = By.xpath("//*[contains(text(),'Bug')]");
    private static By APSLink = By.xpath("//*[contains(text(),'APS')]");
    private static By apsLogo = By.xpath("//*[contains(@alt,'(aps)')]");
    private static By issueType = By.id("issue-create-issue-type");
    private static By homeMenu = By.xpath("//*[text()='Home']");
    private static By contactsMenu = By.xpath("//*[contains(text(),'Contact')]");
    private static By askAQuestionOption = By.xpath("//*[contains(text(),'Ask a question')]");
    private static By startAdiscussionOption = By.xpath("//*[contains(text(),'Start a discussion')]");
    private static By submitAnIdea = By.xpath("//*[contains(text(),'Submit an idea')]");
    private static By followTeam = By.xpath("//*[contains(text(),'Follow the team on Aloha')]");

    private static By userGuide = By.xpath(".//*[contains(text(),'User Guide')]");
    private static final String APSPAGETITLE = "APS - AkaTec Productivity Systems | Aloha";
    private static final String NS4UserGuide = "NS4 Diagnostics User Guide | Aloha";
    private static final String Region360UserGuide = "Region 360";
    static By identityProviderBox = By.name("j_username");
    static By identityProviderButton = By.xpath("//button[text()='Login']");

    public static void goTo(String appName) throws IOException {
        By element = By.xpath("//span[@class = 'tn_title clearfix' and text()='" + appName + "']");
        try {
            moveToElement(element);
            Thread.sleep(3000);
            click(element);
            UtilityFunctions.switchNextWindow();
            Reports.addStepNew("PASS", appName + " Application is launched successfully");
        } catch (Exception e) {
            Reports.addStepNew("FAIL", " Failed to launch " + appName + " " + e.getMessage());
        }
    }

    public static void verifyTitle(String pageTitle, int step) throws IOException, InterruptedException {

        if (Driver.Instance.getTitle().equals(pageTitle)) {
            Reports.addStepNew("PASS",
                    "PageTitle is verfied :Expected : " + pageTitle + " Actual: " + Driver.Instance.getTitle());
        } else
            Reports.addStepNew("FAIL",
                    "PageTitle Mismatch :Expected : " + pageTitle + " Actual: " + Driver.Instance.getTitle());

    }

    public static void verifyUserGuideLink(String appName) throws IOException, InterruptedException {
        try {
            Driver.Instance.findElement(userGuideLink).click();
            switchNextWindow();
            LoginPage.loginAs(BaseTest.getPropertyFromConfig("userName"))
                    .withPassword(BaseTest.getPropertyFromConfig("password")).login();
            if (Driver.Instance.getTitle().equals(appName + " User Guide | Aloha")) {
                Reports.addStepNew("PASS", "Navigated to Page " + Driver.Instance.getTitle());
            } else {
                Reports.addStepNew("FAIL", "Navigated to Page " + Driver.Instance.getTitle());
            }
            Driver.Instance.close();
            switchNextWindow();
        } catch (Exception e) {
            Reports.addStepNew("WARN", "There is no UserGuide Link");
        }
        UtilityFunctions.switchHomepage();
    }

    public static void verifyAPSteamLink() throws IOException, InterruptedException {
        try {
            Driver.Instance.findElement(APSLink).click();
            switchNextWindow();
            verifyTextIntitle(APSPAGETITLE);
            Driver.Instance.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void verifyBugLink() throws InterruptedException, IOException {
        try {
            Driver.Instance.findElement(bugLink).click();
            switchNextWindow();
            softAssert.assertTrue(Driver.Instance.findElement(issueType).getText().equals("Bug"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Driver.Instance.close();
        }
    }

    public static void verifyEnhancementLink() throws InterruptedException, IOException {
        try {
            Driver.Instance.findElement(enhancementLink).click();
            switchNextWindow();
            softAssert.assertTrue(Driver.Instance.findElement(issueType).getText().equals("Enhancements"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Driver.Instance.close();
        }

    }

    public static void verifyAPSLogoLink(int step) throws InterruptedException, IOException {
        try {
            Driver.Instance.findElement(apsLogo).click();
            switchNextWindow();
            LoginPage.loginAs(BaseTest.getPropertyFromConfig("userName"))
                    .withPassword(BaseTest.getPropertyFromConfig("password")).login();
            if (Driver.Instance.getTitle().equals(APSPAGETITLE)) {
                // Reports.addStep("PASS", step, "Navigated to Page " +
                // Driver.Instance.getTitle());
                Reports.addStepNew("PASS", "Navigated to Page " + Driver.Instance.getTitle());
            } else {
                Reports.addStepNew("FAIL", "Navigated to Wrong Page " + Driver.Instance.getTitle());
            }
            Driver.Instance.close();
            switchNextWindow();
        } catch (Exception e) {
            Reports.addStepNew("WARN", "There is no aps Logo Link");
        }

    }

//	public static void verifyAllHomeLinks(String appName, int step) throws IOException, InterruptedException {
//		Driver.Instance.findElement(userGuideLink).click();
//		switchNextWindow();
//		if (appName.equalsIgnoreCase("NS4 Diagnostics") && Driver.Instance.getTitle().equals(NS4UserGuide)) {
//			// Reports.addStep("PASS", step, "Navigated to Page " +
//			// Driver.Instance.getTitle());
//			Reports.addStepNew("PASS", "Navigated to Page " + Driver.Instance.getTitle());
//		} else {
//			// Reports.addStep("FAIL", step, "Navigated to Wrong Page " +
//			// Driver.Instance.getTitle());
//			Reports.addStepNew("FAIL", "Navigated to Wrong Page " + Driver.Instance.getTitle());
//		}
//		Driver.Instance.close();
//		switchHomepage();
//		switchNextWindow();
//		Driver.Instance.findElement(APSLink).click();
//		if (Driver.Instance.getTitle().equals(APSPAGETITLE)) {
//			// Reports.addStep("PASS", ++step, "Navigated to Page " +
//			// Driver.Instance.getTitle());
//			Reports.addStepNew("PASS", "Navigated to Page " + Driver.Instance.getTitle());
//		} else {
//			// Reports.addStep("FAIL", ++step, "Navigated to Wrong Page " +
//			// Driver.Instance.getTitle());
//			Reports.addStepNew("FAIL", "Navigated to Wrong Page " + Driver.Instance.getTitle());
//		}
//		Driver.Instance.close();
//		switchHomepage();
//		switchNextWindow();
//		Driver.Instance.findElement(apsLogo).click();
//		if (Driver.Instance.getTitle().equals(APSPAGETITLE)) {
//			Reports.addStepNew("PASS", ++step, "Navigated to Page " + Driver.Instance.getTitle());
//		} else {
//			Reports.addStep("FAIL", ++step, "Navigated to Wrong Page " + Driver.Instance.getTitle());
//		}
//
//	}

    public static void verifyHeaderMenuOptions(String userGuideName) throws IOException {
        try {
            Driver.Instance.findElement(homeMenu).click();
            verifyUserLinkGuide(userGuideName);
            verifyHeaderLinks(askAQuestionOption, "Ask a Question");
            verifyHeaderLinks(startAdiscussionOption, "Start a discussion");
            verifyHeaderLinks(submitAnIdea, "Create a new idea");
            verifyHeaderLinks(followTeam, APSPAGETITLE);
        } catch (Exception e) {

        }

    }


    public static void verifyHeaderLinks(By element, String title) throws IOException {
        try {
            Reports.assertType = "soft";
            click(contactsMenu);
            click(element);
            switchNextWindow();
            waitForElement(homeMenu, 60);
            verifyTextIntitle(title);
            Reports.addStepNew("PASS", title + " page is verified succesfully");
            Driver.Instance.close();
            switchHomepage();
        } catch (Exception e) {
            switchHomepage();
            Reports.addStepNew("FAIL", "Failed to verify the " + title + " :" + e.getMessage());
        }

    }

    private static void verifyUserLinkGuide(String userGuideName) throws InterruptedException, IOException {

        try {
            Reports.assertType = "soft";
            Driver.Instance.findElement(userGuide).click();
            switchNextWindow();
            handleDuoPush();
            By element = By.xpath("//*[text()='Home']");
            waitForElement(element, 60);
            verifyTextIntitle(userGuideName);
            Reports.addStepNew("PASS", "User Guide link is verified");
            Driver.Instance.close();
            switchHomepage();
        } catch (Exception e) {
            switchHomepage();
            Reports.addStepNew("FAIL", "Failed to verify user guide link " + e.getMessage());
        }

    }
    public static void VerifyAlohaLink(By element, String title) throws IOException, InterruptedException {

        try {
            click(element);
            switchNextWindow();
            try {
                Driver.Instance.findElement(By.linkText("log in")).isDisplayed();
                click(By.linkText("log in"));
                Assert.assertTrue(Driver.Instance.getTitle().equals(title));
            } catch (Exception e) {
                Assert.assertTrue(Driver.Instance.getTitle().equals(title));
            }

        } catch (Exception e) {
            System.out.println(Driver.Instance.getTitle());
            Reports.addStepNew("FAIL", Driver.Instance.getTitle() + " :: Failed to verify the Link" + e.getMessage());
        } finally {
            System.out.println(Driver.Instance.getTitle());
            Driver.Instance.close();
            switchHomepage();
        }
    }

    public static void verifyFooterLinks(By element, String title) throws IOException {
        try {
            Reports.assertType = "soft";
            Driver.Instance.findElement(element).click();
            switchNextWindow();
            try {
                Driver.Instance.findElement(By.linkText("log in")).isDisplayed();
                click(By.linkText("log in"));
                softAssert.assertTrue(Driver.Instance.findElement(issueType).getText().equals(title));
            } catch (Exception e) {
                softAssert.assertTrue(Driver.Instance.findElement(issueType).getText().equals(title));
            }
            Reports.addStepNew("PASS", title + " link page is verified");
        } catch (Exception e) {
            Reports.addStepNew("FAIL", title + " :: Failed to verify the Link" + e.getMessage());
        } finally {
            Driver.Instance.close();
            switchHomepage();
        }
    }

    public static void verifyfooter() throws IOException {
        try {
            verifyFooterLinks(bugLink, "Bug");
            verifyFooterLinks(enhancementLink, "Enhancement");
            VerifyAlohaLink(APSLink,"APS - AkaTec Productivity Systems | Aloha");
        } catch (Exception e) {
        }
    }

}
