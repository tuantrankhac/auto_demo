package demo.web.testScripts;

import commons.BaseTest;
import demo.web.pageObjects.PageGenerator;
import demo.web.pageObjects.SwitchTabPO;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SwitchTab extends BaseTest {
    @Parameters({ "browser", "url" })
    @BeforeMethod
    public void beforeMethod(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        getBrowserDriver(browserName, url);
        switchTabPO = PageGenerator.getSwitchTabPO(getDriver());

    }

    @Test(priority = 1)
    public void SwtichTabByTitle() {
        String titleNewTab = "New Window";
        String parentHandle = switchTabPO.getWindowParentHandle();
        switchTabPO.clickLinkToNewTab();
        switchTabPO.switchWindowByTitle(titleNewTab);
        switchTabPO.getTextInNewTab();
        switchTabPO.closeCurrentWindowAndSwitchToParent(driver, parentHandle);
        switchTabPO.getTextInInitTab();
        sleepInMiliSecond(3000);
    }

    @Test(priority = 2)
    public void SwtichTabByUrl() {
        String urlNewTab = "new";
        String parentHandle = switchTabPO.getWindowParentHandle();
        switchTabPO.clickLinkToNewTab();
        switchTabPO.switchWindowByUrl(driver, urlNewTab);
        switchTabPO.getTextInNewTab();
        switchTabPO.closeCurrentWindowAndSwitchToParent(driver, parentHandle);
        switchTabPO.getTextInInitTab();
    }

    @Test(priority = 3)
    public void SwtichTabByIndex() {
        int indexNewTab = 1;
        String parentHandle = switchTabPO.getWindowParentHandle();
        switchTabPO.clickLinkToNewTab();
        switchTabPO.switchWindowByIndex(driver, indexNewTab);
        switchTabPO.getTextInNewTab();
        switchTabPO.closeCurrentWindowAndSwitchToParent(driver, parentHandle);
        switchTabPO.getTextInInitTab();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    SwitchTabPO switchTabPO;

}
