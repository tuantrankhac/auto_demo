package demo.testSuites;

import commons.BrowserFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import commons.retry.*;
import demo.pageObjects.PageGenerator;
import demo.pageObjects.RetryTestPO;;

public class HandleTestRetry extends BrowserFactory {
    @Parameters({ "browser", "url" })
    @BeforeMethod
    public void BeforeMethod(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName, url);
        retryTestPO = PageGenerator.getRetryTestPO(driver);
    }

    @Test(retryAnalyzer = RetryTest.class)
    public void TC01_testLogin() {
        retryTestPO.loginPage("username", "password");
    }
    

    @AfterMethod(alwaysRun = true)
    public void AfterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    RetryTestPO retryTestPO;
}
