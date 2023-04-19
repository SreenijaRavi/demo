package com.akatec.aps.pages;

import java.io.IOException;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.akatec.aps.reusablefunctions.BaseTest;

import com.akatec.aps.reusablefunctions.Driver;
import com.akatec.aps.reusablefunctions.UtilityFunctions;

public class LoginPage extends UtilityFunctions {
    public static String parentWindow;
    private static By pubCookie = By.xpath("//div[contains(text(),'Akamai Single Sign-On')]");

    public static void goTo() throws InterruptedException, IOException {
        try {
            // Driver.Instance.get("https://tools.aps.akamai.com/crawly/results.html?id1=200efcc");
            Driver.Instance.get(BaseTest.getPropertyFromConfig("URL"));
//			LoginPage.loginAs(BaseTest.getPropertyFromConfig("userName"))
//					.withPassword(BaseTest.getPropertyFromConfig("password")).login();
            LoginPage.pubCookieLogin();
            parentWindow = Driver.Instance.getWindowHandle();
            System.out.println("Parent Window " + parentWindow);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LoginCommand loginAs(String userName) {
        return new LoginCommand(userName);
    }



    public static void pubCookieLogin() throws InterruptedException, IOException {

        //try {
        //waitForElement(pubCookie, 30);
        LoginPage.loginAs(BaseTest.getPropertyFromConfig("userName"))
                .withPassword(BaseTest.getPropertyFromConfig("password")).login();
        //} catch (Exception e) {
        //	System.out.println("No Akamai Single sign on");
        //}

    }
}

class LoginCommand {
    private String userName;
    private String password;

    public LoginCommand(String userName) {
        this.userName = userName;
    }

    public LoginCommand withPassword(String password) {
        this.password = password;
        return this;
    }

    public void login() throws InterruptedException {
        // WebElement loginName = Driver.Instance.findElement(By.name(""));
        // loginName.sendKeys(this.userName);
        WebElement passwordName = Driver.Instance.findElement(By.xpath("//input[@name='pass']"));
        passwordName.sendKeys(this.password);
        Thread.sleep(1000);
        WebElement loginButton = Driver.Instance.findElement(By.xpath("//input[@value='Log in ¬ª']"));
        loginButton.click();

    }
}

