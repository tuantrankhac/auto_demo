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
    @Parameters({ "browser", "url" })
    @BeforeClass
    public void BeforeClass(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName, url);
        testDependencyPO = PageGenerator.getDependencyPO(driver);

    }

    @Test
    public void TC01_AddProductToCart() {
        String productName = "Custom T-Shirt";
        String nameOnShirt = "Love";
        testDependencyPO.selectAndAddProductShirt(productName, nameOnShirt);
    }

    @Test(dependsOnMethods = "TC01_AddProductToCart")
    public void TC02_EditProductInCart() {
        String productName = "Custom T-Shirt";
        testDependencyPO.increaseQuantityProductInCart(productName);
        testDependencyPO.increaseQuantityProductInCart(productName);
        double priceUnit = testDependencyPO.getPriceUnitProduct(productName);
        int quantityAfterIncrease = testDependencyPO.getQuantityProduct(productName, "value");
        double totalPriceActualIncrease = priceUnit * quantityAfterIncrease;
        double totalPrictExpectIncrease = testDependencyPO.getTotalPriceProduct(productName);
        verifyEquals(totalPriceActualIncrease, totalPrictExpectIncrease);

        testDependencyPO.decreaseQuantityProductInCart(productName);
        int quantityAfterDecrease = testDependencyPO.getQuantityProduct(productName, "value");
        double totalPriceActualDecrease = priceUnit * quantityAfterDecrease;
        double totalPrictExpectDecrease = testDependencyPO.getTotalPriceProduct(productName);
        verifyEquals(totalPriceActualDecrease, totalPrictExpectDecrease);
    }

    @Test(dependsOnMethods = "TC02_EditProductInCart")
    public void TC03_DeleteProductInCart() {
        String productName = "Custom T-Shirt";
        testDependencyPO.deleteProductInCart(productName);
    }

    @Test(groups = "Product Added")
    public void TC04_AddNewProductToCart1() {
        String productName = "Ray Ban Aviator Sunglasses";
        testDependencyPO.goToHomePage();
        testDependencyPO.selectAndAddProduct(productName);
    }

    @Test(dependsOnMethods = "TC04_AddNewProductToCart1", groups = "Product Added")
    public void TC05_AddNewProductToCart2() {
        String productName = "Reversible Horseferry Check Belt";
        testDependencyPO.goToHomePage();
        testDependencyPO.selectAndAddProduct(productName);
    }

    @Test(dependsOnGroups = "Product Added")
    public void TC06_CheckoutProduct() {
        testDependencyPO.goToHomePage();
        testDependencyPO.openCartPage();

    }

    @AfterClass(alwaysRun = true)
    public void AfterClass() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    TestDependencyPO testDependencyPO;
}
