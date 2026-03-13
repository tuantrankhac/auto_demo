package demo.mobile.testScripts;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import commons.BaseTest;
import demo.web.pageObjects.HandleAlertPO;
import demo.web.pageObjects.PageGenerator;

public class DemoApp extends BaseTest{
    @Parameters({ "browser", "url" })
    @BeforeMethod
    public void beforeMethod(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName, url);
        handleAlertPO = PageGenerator.getHandleAlertPO(driver);

    }

    @Test(priority = 1)
    public void HandleAlertBasic() {
        handleAlertPO.clickButtonDisplayAlert();
        handleAlertPO.acceptAlert(driver);
    }

    

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeAllMobiles();
    }

    private WebDriver driver;
    HandleAlertPO handleAlertPO;
}
