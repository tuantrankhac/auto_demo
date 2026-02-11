package demo.testSuites;

import commons.BrowserFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import demo.pageObjects.PageGenerator;
import demo.pageObjects.SwitchTabPO;

public class SwitchTab extends BrowserFactory {
    @Parameters({ "browser", "url" })
    @BeforeMethod
    public void beforeMethod(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName, url);
        switchTabPO = PageGenerator.getSwitchTabPO(driver);

    }

    @Test(priority = 1)
    public void SwtichTabByTitle() {
        String titleNewTab = "New Window";
        String parentHandle = switchTabPO.getWindowParentHandle();
        switchTabPO.clickLinkToNewTab();
        switchWindowByTitle(driver, titleNewTab);
        switchTabPO.getTextInNewTab();
        closeCurrentWindowAndSwitchToParent(driver, parentHandle);
        switchTabPO.getTextInInitTab();
        sleepInMiliSecond(3000);
    }

    @Test(priority = 2)
    public void SwtichTabByUrl() {
        String urlNewTab = "new";
        String parentHandle = switchTabPO.getWindowParentHandle();
        switchTabPO.clickLinkToNewTab();
        switchWindowByUrl(driver, urlNewTab);
        switchTabPO.getTextInNewTab();
        closeCurrentWindowAndSwitchToParent(driver, parentHandle);
        switchTabPO.getTextInInitTab();
    }

    @Test(priority = 3)
    public void SwtichTabByIndex() {
        int indexNewTab = 1;
        String parentHandle = switchTabPO.getWindowParentHandle();
        switchTabPO.clickLinkToNewTab();
        switchWindowByIndex(driver, indexNewTab);
        switchTabPO.getTextInNewTab();
        closeCurrentWindowAndSwitchToParent(driver, parentHandle);
        switchTabPO.getTextInInitTab();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    SwitchTabPO switchTabPO;

}
