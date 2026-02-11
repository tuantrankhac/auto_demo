package demo.testSuites;

import commons.BrowserFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import demo.pageObjects.HandleAlertPO;
import demo.pageObjects.PageGenerator;
import utilities.DbConnection;

public class HandleAlert extends BrowserFactory {
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

    @Test(priority = 2)
    public void HandleConfirm() {
        handleAlertPO.clickButtonDisplayConfirm();
        handleAlertPO.acceptAlert(driver);
        String acceptText = "You pressed Ok";
        verifyEquals(acceptText, handleAlertPO.getTextAfterActionConfirm());

        handleAlertPO.clickButtonDisplayConfirm();
        handleAlertPO.cancelAlert(driver);
        String dismissText = "You pressed Ok";
        verifyEquals(dismissText, handleAlertPO.getTextAfterActionConfirm());

    }

    @Test(priority = 3)
    public void HandlePrompt() {
        String textInput = "Automation Testing";
        String textInputDislay = "Hello " + textInput + " How are you today";
        handleAlertPO.clickButtonDisplayPrompt();
        handleAlertPO.sendkeyToAlert(driver, textInput);
        handleAlertPO.acceptAlert(driver);
        verifyEquals(textInputDislay, handleAlertPO.getTextAfterActionPrompt());
    }

    @Test(priority = 4)
    public void HandleUnexpectedAlert() {
        String actionAlert = "accept";
        handleAlertPO.handleUnexpectedAlert(actionAlert);
    }


    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    private DbConnection db;
    HandleAlertPO handleAlertPO;
}
