package demo.web.testScripts;

import commons.BaseTest;
import commons.PageGenerator;
import demo.web.pageObjects.ActionsPagePO;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginWithoutAuth extends BaseTest {
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
