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

    public void editProductInCart(String value) {
        clickToElement(driver, TestDependencyPageUI.EDIT_PRODUCT_IN_CART);
        getWebElement(driver, TestDependencyPageUI.QUANTITY_TEXTBOX, value);
        sendkeyToElement(driver, TestDependencyPageUI.NAME_TEXTBOX, value);
        clickToElement(driver, TestDependencyPageUI.UPDATE_CART_BUTTON);
    }

    public void verifyProductAfterEdit(String value) {
        waitForElementVisible(driver, TestDependencyPageUI.SHOPPING_CART_TEXT);
        Assert.assertEquals(getTextElement(driver, TestDependencyPageUI.SHOPPING_CART_TEXT), value);
    }

    public void deleteProductInCart() {
        clickToElement(driver, TestDependencyPageUI.SHOPPING_CART_TEXT);
        clickToElement(driver, TestDependencyPageUI.DELETE_PRODUCT_IN_CART);
    }

    public void backToHomePage() {
        clickToElement(driver, TestDependencyPageUI.BACK_TO_HOME_PAGE);
    }

}