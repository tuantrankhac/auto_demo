package demo.testSuites;

import commons.BrowserFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import demo.pageObjects.ActionsPagePO;
import demo.pageObjects.PageGenerator;

public class ActionsPage extends BrowserFactory {
    @Parameters({ "browser", "url" })
    @BeforeMethod
    public void beforeMethod(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName, url);
        actionsPagePO = PageGenerator.getActionsPagePO(driver);
    }

    @Test(priority = 1)
    public void RefreshPageByNavigate() {
        actionsPagePO.refreshPageByNavigate(driver);
    }

    @Test(priority = 2)
    public void RefreshPageByGetUrl(){
        actionsPagePO.refreshPageByGetUrl(driver);
    }

    @Test(priority = 3)
    public void RefreshPageByJS(){
        actionsPagePO.refreshPageByJS(driver);
    }

    @Test(priority = 4)
    public void RefreshPageByActions(){
        actionsPagePO.refreshPageByActions(driver);
    }

    @Test(priority = 5)
    public void ForwardPageByNavigate(){
        actionsPagePO.actionForward();
        actionsPagePO.forwardToPageByNavigate(driver);
        verifyTrue(actionsPagePO.textVisibleAfterForward());
    }

    @Test(priority = 6)
    public void ForwardPageByJS(){
        actionsPagePO.actionForward();
        actionsPagePO.forwardToPageByJS(driver);
        verifyTrue(actionsPagePO.textVisibleAfterForward());
    }

    @Test(priority = 7)
    public void BackPageByNavigate(){
        actionsPagePO.actionBack();
        actionsPagePO.backToPageByNavigate(driver);
        verifyTrue(actionsPagePO.textVisibleAfterBack());
    }

    @Test(priority = 8)
    public void BackPageByJS(){
        actionsPagePO.actionBack();
        actionsPagePO.backToPageByJS(driver);
        verifyTrue(actionsPagePO.textVisibleAfterBack());
    }

    @Test(priority = 9)
    public void GetPageTitle(){
        String title = "The Internet";
        String pageTitleCurrent = actionsPagePO.getPageTitle(driver);
        verifyEquals(title, pageTitleCurrent);
    }

    @Test(priority = 10)
    public void GetPageTitleByJS(){
        String title = "The Internet";
        String pageTitleCurrent = actionsPagePO.getPageTitleByJS(driver);
        verifyEquals(title, pageTitleCurrent);
    }

    @Test(priority = 11)
    public void GetCookieBySelenium(){
        actionsPagePO.getAllCookies(driver);
    }

    @Test(priority = 12)
    public void GetCookieByJS(){
        actionsPagePO.getCookieByJS(driver);
    }

    @Test(priority = 14)
    public void GetCookieByCDP(){
        actionsPagePO.getAllCookiesByCDP(driver);
    }


    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    ActionsPagePO actionsPagePO;
}
