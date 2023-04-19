package com.akatec.aps.testcases;

import com.akatec.aps.pages.PSHUBPage_02;
import com.akatec.aps.reusablefunctions.Driver;
import org.testng.annotations.Test;
import java.io.IOException;
import com.akatec.aps.pages.PSHUBPage;
import com.akatec.aps.reusablefunctions.BaseTest;


public class PS_Hub_links extends BaseTest {
    @Test
    public void find_all_the_hyperkinks() throws IOException, InterruptedException {

       PSHUBPage_02.goTo();
        PSHUBPage_02.FindLinksonPage();
        System.out.println(Driver.Instance.getTitle());
/*
        PSHUBPage.goTo();
        System.out.println(Driver.Instance.getTitle());
        PSHUBPage.FindLinksHomePage();
        PSHUBPage p = new PSHUBPage();
        // p.Getinternallinks();
        p.GetallinksInTheFile();
*/


    }

}
