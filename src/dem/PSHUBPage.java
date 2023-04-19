package com.akatec.aps.pages;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.io.FileReader;
import java.util.concurrent.ConcurrentHashMap;

import com.akatec.aps.reusablefunctions.BaseTest;


import com.akatec.aps.reusablefunctions.Driver;
import com.akatec.aps.pages.DataURL;
import com.akatec.aps.pages.Slug;
import com.akatec.aps.reusablefunctions.ExcelUtils;
import com.akatec.aps.reusablefunctions.Reports;
import com.akatec.aps.reusablefunctions.UtilityFunctions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.testng.Assert;

public class PSHUBPage extends UtilityFunctions {
    private static By submit = By.id("submit");
    private static String eol = System.getProperty("line.separator");
    private static String title;
    int i =0;

    private static Map<String, String> fileNames = new ConcurrentHashMap<String, String>();


    public static void goTo() throws IOException {
        try {
            Driver.Instance.get(BaseTest.getPropertyFromConfig("PS_HUB_URL"));
            handleDuoPush();
            //LoginPage.pubCookieLogin();
            //  waitForElement(submit, 10000);
            // Reports.addStepNew("PASS",
            //         "Successfully Navigated to URL " + BaseTest.getPropertyFromConfig("PS_HUB_URL"));
        } catch (Exception e) {
            Reports.addStepNew("FAIL", "Failed to Navigate to " + BaseTest.getPropertyFromConfig("PS_HUB_URL")
                    + " " + e.getMessage());
        } catch (AssertionError e) {
            Reports.addStepNew("FAIL",
                    "Failed to Navigate to " + BaseTest.getPropertyFromConfig("PS_HUB_URL") + e.getMessage());

        }
    }

    public static void FindLinksHomePage() throws InterruptedException, JsonProcessingException {
        Thread.sleep(10000);

        // String value= Driver.Instance.findElement(By.xpath("//*[text() ='SLM / Director']")).getAttribute("data-url");
        //   System.out.println(value);
        List<WebElement> links = Driver.Instance.findElements(By.tagName("a"));
        title= Driver.Instance.getTitle();

        Map<String, String> directUrl = new HashMap<String, String>();
        Map<String, String> slugUrls = new HashMap<String, String>();

        System.out.println("Number of Links in the Page is " + links.size());


        for (int i = 1; i <= links.size() - 1; i = i + 1) {
            System.out.println(links.get(i).getText() + " - " + links.get(i).getAttribute("href"));

            if (links.get(i).getAttribute("href") != null) {
                directUrl.put(links.get(i).getAttribute("href"), links.get(i).getText());
            } else {
                if (links.get(i).getText() != null || !(links.get(i).getText()).isEmpty()) {
                    slugUrls.put(links.get(i).getText(), links.get(i).getAttribute("href"));
                } else {
                    System.out.println("null slug");
                }
            }

        }


       try (Writer writer = new FileWriter("TestData/hrefUrlList"+title+".csv")) {
            for (Map.Entry<String, String> entry : directUrl.entrySet()) {
                writer.append(entry.getValue())
                        .append(',')
                        .append(entry.getKey())
                        .append(eol);
            }
           fileNames.put("TestData/hrefUrlList"+title+".csv",title );
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

        PSHUBPage p = new PSHUBPage();
        p.getSlugUrls(slugUrls);
       // p.Getinternallinks();

    }

    public void getSlugUrls(Map<String, String> slugUrls) throws JsonMappingException, JsonProcessingException {
        Map<String, String> slugUrlLinks = new HashMap<String, String>();
        ObjectMapper objectMapper = new ObjectMapper();
        String baseUrl = Driver.Instance.getCurrentUrl().concat("/");
        String finalUrl = null;
        for (Map.Entry<String, String> slugUrl : slugUrls.entrySet()) {

            String slugKey = slugUrl.getKey();
            if (slugKey != null && !slugKey.isEmpty()) {
                try {
                    // Xpath=//*[contains(text(),'here')]
                    //String jsonSlug = Driver.Instance.findElement(By.xpath("//*[contains(text(),'" + slugKey + "']")).getAttribute("data-url");

                    String jsonSlug = Driver.Instance.findElement(By.xpath("//*[text() ='" + slugKey + "']")).getAttribute("data-url");
                    System.out.println(jsonSlug);
                    DataURL dataUrl = objectMapper.readValue(jsonSlug, DataURL.class);
                    finalUrl = baseUrl.concat(dataUrl.getSlug().getEn());
                    slugUrlLinks.put(finalUrl, slugKey);
                } catch (Exception e) {
                    // To handle contains for special chars in Xpath
                   /* String jsonSlug = Driver.Instance.findElement(By.xpath("//*[contains(text(),'" + slugKey + "']")).getAttribute("data-url");
                    System.out.println(jsonSlug);
                    DataURL dataUrl = objectMapper.readValue(jsonSlug, DataURL.class);
                    finalUrl = baseUrl.concat(dataUrl.getSlug().getEn());
                    slugUrlLinks.put(finalUrl, slugKey);*/
                    System.out.println(slugKey + ": Exception while parsing xPath");

                }
            }
        }

        try (Writer writer = new FileWriter("TestData/slugUrlList"+title+".csv")) {
            for (Map.Entry<String, String> entry : slugUrlLinks.entrySet()) {
                writer.append(entry.getValue())
                        .append(',')
                        .append(entry.getKey())
                        .append(eol);
            }
            fileNames.put("TestData/slugUrlList"+title+".csv",title );
        } catch (IOException ex) {

            ex.printStackTrace(System.err);
        }
    }

    public void Getinternallinks(String fileName){
        try {



            FileReader filereader = new FileReader(fileName);

            //create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

                // we are going to read data line by line
                while ((nextRecord = csvReader.readNext()) != null) {

                    String newURL = nextRecord[1];
                    System.out.println(newURL);
                    if (newURL.contains("global-ps-hub")) {
                        System.out.println();
                        Driver.Instance.get(nextRecord[1]);
                        i++;
                        Thread.sleep(1000);
                        PSHUBPage.FindLinksHomePage();
                    }

            }
        }
    catch (Exception e) {
        e.printStackTrace();
    }


}

    public void GetallinksInTheFile()
    {
        for(String fileNmae:fileNames.keySet()) {

            System.out.println(fileNmae);
            Getinternallinks(fileNmae);
        }
    }
    }














