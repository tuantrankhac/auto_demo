package demo.testSuites;

import commons.BrowserFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import demo.pageObjects.HandleShadowDOMPO;
import demo.pageObjects.PageGenerator;

public class HandleShadowDOM extends BrowserFactory {
    @Parameters({ "browser" })
    @BeforeMethod
    public void beforeMethod(String browserName) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName);
        handleShadowDOMPO = PageGenerator.getHandleShadowDOMPO(driver);

    }

    @Test(priority = 1)
    public void HandleShadowDOMSingle() {
        driver.get("https://practice.expandtesting.com/shadowdoms");
        handleShadowDOMPO.getTextElementInShadow();

    }

    @Test(priority = 2)
    public void HandleShadowDOMNested() {
        driver.get("https://selectorshub.com/shadow-dom-in-iframe/");
        handleShadowDOMPO.getTextElementInNestedShadow();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    HandleShadowDOMPO handleShadowDOMPO;
}
