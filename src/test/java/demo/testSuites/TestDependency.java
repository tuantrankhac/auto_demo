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

    @Test(dependsOnMethods = "AddProductToCart")
    public void EditProductInCart() {
        testDependencyPO.editQuantityProductInCart();
        testDependencyPO.editContentProductInCart();
    }

    @Test(dependsOnMethods = "EditProductInCart")
    public void DeleteProductInCart() {
        testDependencyPO.deleteProductInCart();
    }


    @Test(groups = "Product Added")
    public void AddNewProductToCart1() {
        String productName = "Custom T-Shirt";
        String nameOnShirt = "Love5";
        testDependencyPO.selectAndAddProduct(productName, nameOnShirt);
    }

    @Test(groups = "Product Added")
    public void AddNewProductToCart2() {
        String productName = "Custom T-Shirt";
        String nameOnShirt = "Love7";
        testDependencyPO.selectAndAddProduct(productName, nameOnShirt);
    }

    @Test(dependsOnGroups = "Product Added")
    public void CheckoutProduct() {
        testDependencyPO.checkoutProduct();
    }


    @AfterClass(alwaysRun = true)
    public void AfterClass() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    TestDependencyPO testDependencyPO;
}
