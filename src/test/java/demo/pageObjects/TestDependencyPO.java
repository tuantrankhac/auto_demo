package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.TestDependencyPageUI;

import org.openqa.selenium.WebDriver;

public class TestDependencyPO extends BasePage {
    WebDriver driver;

    public TestDependencyPO(WebDriver driver) {
        this.driver = driver;
    }

    public void selectAndAddProduct(String productName, String value) {
        clickToElement(driver, TestDependencyPageUI.APPAREL_MENU);
        clickToElement(driver, TestDependencyPageUI.CLOTHING_CATAGORI);
        clickToElement(driver, TestDependencyPageUI.PRODUCT_DYNAMIC, productName);
        sendkeyToElement(driver, TestDependencyPageUI.NAME_TEXTBOX, value);
        clickToElement(driver, TestDependencyPageUI.ADD_TO_CART_BUTTON);
        clickToElement(driver, TestDependencyPageUI.MESSAGE_AFTER_ADD_PRODUCT);
        waitForElementVisible(driver, TestDependencyPageUI.SHOPPING_CART_TEXT);
    }

}