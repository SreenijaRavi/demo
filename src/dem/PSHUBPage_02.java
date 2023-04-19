package com.akatec.aps.pages;

import com.akatec.aps.reusablefunctions.BaseTest;
import com.akatec.aps.reusablefunctions.Driver;
import com.akatec.aps.reusablefunctions.Reports;
import com.akatec.aps.reusablefunctions.UtilityFunctions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextSelectionDelegate;
import com.opencsv.CSVReader;

import com.opencsv.CSVWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PSHUBPage_02 extends UtilityFunctions {
    private static By submit = By.id("submit");
    private static String eol = System.getProperty("line.separator");
    private static String title;
    int i =0;
    public static Map<String, String> InternalLinkMap = new ConcurrentHashMap<>();
    public static  Map<String, String>VisitedlLinkMap = new ConcurrentHashMap<>();
    private static Map<String, String> fileNames = new ConcurrentHashMap<String, String>();
    private static String fileName = "AllLinks.csv";
    private static HashSet<WebElement> links = new HashSet<WebElement>();
    public static  Map<String, String>TrackedHrefLinkMap = new ConcurrentHashMap<>();
    public static  Map<String, String>ExternalLinkMap = new ConcurrentHashMap<>();


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


    /*--------------------------- Excel File Write -------------------------------------*/


    public static void  FindLinksonPage()
    {

        try {

            Thread.sleep(10000);
            List<WebElement> List_links = Driver.Instance.findElements(By.tagName("a"));
            for (WebElement link : List_links) {
                if (!(links.contains(link)))
                    links.add(link);
            }
            System.out.println("unique_links: " + links.size());
            title = Driver.Instance.getTitle();
            System.out.println("Number of Links in the Page " + title + " is " + links.size());
            List<WebElement> AllSlugLinks = Driver.Instance.findElements(By.xpath(".//a[contains(@target,'_self') and contains(@class,'jive-link-wiki')]"));


            for (WebElement Sluglink : AllSlugLinks) {

                System.out.println(Sluglink.getText() + " - " + Sluglink.getAttribute("data-url"));
                String SlugURL = getSlugUrl(Sluglink.getAttribute("data-url"));

                InternalLinkMap.putIfAbsent(Sluglink.getText(),SlugURL);

            }


            for (WebElement link : links)
            {
                if ( (link.getAttribute("target").contains("_blank")) && !(link.getAttribute("href").isEmpty()))
                {
                    if(link.getAttribute("href").contains("global-ps-hub"))
                        InternalLinkMap.putIfAbsent(link.getAttribute("href"),link.getText());

                    else{
                        ExternalLinkMap.putIfAbsent(link.getText(), link.getAttribute("href"));
                        System.out.println(link.getText() + " - " + link.getAttribute("href"));
                        if(!(ExternalLinkMap.containsValue(link.getAttribute("href")))) {
                            WriteInCSV(title, link.getText(), link.getAttribute("href"));
                            TrackedHrefLinkMap.putIfAbsent(link.getText(), link.getAttribute("href"));
                            ExternalLinkMap.remove(link.getText(), link.getAttribute("href"));
                        }

                    }

//                    if(link.getAttribute("href").contains("global-ps-hub"))
//                        InternalLinkMap.putIfAbsent(link.getText(),link.getAttribute("href"));
//
                }
                else
                {
                    System.out.println( "The link has not been included for :" + link.getText() + " , Link : " +link.getAttribute("href"));
                }

            }

            for (Map.Entry<String, String> entry : InternalLinkMap.entrySet()) {
                String Link = entry.getKey();
                String LinkLabel = entry.getValue();
                System.out.println("<-------------Printing Internal Links ------------------------>");
                System.out.println("LinkLabel -  " + LinkLabel + " ,Link- " + Link);

                if(!checkVisitedlLink(Link)) {
                    WriteInCSV(Driver.Instance.getTitle(),  LinkLabel,  Link);
                    Driver.Instance.get(Link);
                    VisitedlLinkMap.putIfAbsent(LinkLabel, Link);
                    InternalLinkMap.remove(LinkLabel, Link);
                    System.out.println("Visted List Size : " + VisitedlLinkMap.size());
                    System.out.println("Internal List Size : " + InternalLinkMap.size());
                    Thread.sleep(1000);
                    System.out.println("Currently on Page : " + Driver.Instance.getTitle());
                    FindLinksonPage();
                }
                else
                {
                    System.out.println("Already visited : Link label - "+LinkLabel + " ,Link : "+Link);
                    InternalLinkMap.remove(LinkLabel, Link);
                    System.out.println("Visted List Size : " + VisitedlLinkMap.size());
                    System.out.println("Internal List Size : " + InternalLinkMap.size());

                }



            }

        }
        catch (Exception e)
        {
            System.out.println("----------------Exception in FindLinksonPage func---------------");
            e.printStackTrace();
        }



    }

    public static boolean checkVisitedlLink(String Link)
    {
        String UrltoCheck =  Link;
        boolean flag = false;
        try
        {
            if(VisitedlLinkMap.containsKey(UrltoCheck))
                flag = true;
            else
                flag = false;

        }catch(Exception e)
        {
            System.out.println("----------------Exception in checkInternalLink func---------------");
            e.printStackTrace();
        }
        return flag;
    }


    public static String getSlugUrl(String dataUrl)
    {
        String baseUrl = Driver.Instance.getCurrentUrl().concat("/");
        String finaSluglUrl = null;

        String data_Url = dataUrl;

        try {

            String SlugEn = dataUrl.split("\"en\":")[1].split("}")[0].split("\"")[1];
            System.out.println(SlugEn);

            System.out.println(SlugEn.substring(0, SlugEn.length()));
            finaSluglUrl = baseUrl.concat(SlugEn.substring(0, SlugEn.length()));
            System.out.println(finaSluglUrl);

        } catch (Exception e) {
            System.out.println("----------------Exception in getSlugUrl func---------------");
            e.printStackTrace();
        }
        return finaSluglUrl;
    }

    public static void WriteInCSV(String PageName, String LinkLabel, String Link)
    {
        try{

            File file = new File("TestData/AllLink.csv");
            FileWriter outputfile = new FileWriter(file, true);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
           /* String[] header = { "PageName", "LinkLabel", "Link" };
            writer.writeNext(header);*/
            System.out.println("---------Started with file write---------------");
            // add data to csv
            String[] data = { PageName, LinkLabel, Link };
            writer.writeNext(data);


            // closing writer connection
            writer.flush();
            writer.close();
            System.out.println("---------Completed with file write---------------");


        }catch(Exception e)
        {
            System.out.println("----------------Exception in CSV Write func---------------");
            e.printStackTrace();
        }
    }
}

