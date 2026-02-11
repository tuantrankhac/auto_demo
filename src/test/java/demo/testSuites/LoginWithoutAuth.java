package demo.testSuites;

import commons.BrowserFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import demo.pageObjects.ActionsPagePO;
import demo.pageObjects.PageGenerator;

public class LoginWithoutAuth extends BrowserFactory {
    @Parameters({ "browser", "url" })
    @BeforeMethod
    public void beforeMethod(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriverWithInjectHeader(browserName, url);
        driver = getBrowserDriverWithCredentials(browserName, url);
        driver = getBrowserDriver(browserName, url);
        actionsPagePO = PageGenerator.getActionsPagePO(driver);
    }

    @Test(priority = 1)
    public void LoginPage() {
    }



    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    ActionsPagePO actionsPagePO;
}
