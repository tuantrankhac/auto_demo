package demo.web.testScripts;

import commons.BaseTest;
import commons.PageGenerator;
import demo.web.pageObjects.HandleShadowDOMPO;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class HandleShadowDOM extends BaseTest {
    @Parameters({ "browser" })
    @BeforeMethod
    public void beforeMethod(String browserName) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName);
        handleShadowDOMPO = PageGenerator.getHandleShadowDOMPO(driver);

    }

    @Test(priority = 1)
    public void HandleShadowDOMSingle() {
        driver.get("https://practice.expandtesting.com/shadowdom");
        handleShadowDOMPO.getTextElementInShadow();
    }

    @Test(priority = 2)
    public void HandleShadowDOMNested() {
        driver.get("https://shop.polymer-project.org/");
        handleShadowDOMPO.getTextElementInNestedShadow();
        
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    HandleShadowDOMPO handleShadowDOMPO;
}
