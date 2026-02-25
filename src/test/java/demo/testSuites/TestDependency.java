package demo.testSuites;

import commons.BrowserFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import demo.pageObjects.PageGenerator;
import demo.pageObjects.TestDependencyPO;

public class TestDependency extends BrowserFactory {
    @Parameters({ "browser", "url"})
    @BeforeClass
    public void BeforeClass(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName, url);
        testDependencyPO = PageGenerator.getDependencyPO(driver);

    }

    @Test
    public void AddProductToCart() {
        String productName = "Custom T-Shirt";
        String nameOnShirt = "Love";
        testDependencyPO.selectAndAddProduct(productName, nameOnShirt);

    }

    @Test(priority = 2)
    public void HandleShadowDOMNested() {
        
    }

    @AfterClass(alwaysRun = true)
    public void AfterClass() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    TestDependencyPO testDependencyPO;
}
