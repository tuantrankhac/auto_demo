package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.TestDependencyPageUI;

import org.openqa.selenium.WebDriver;

public class TestDependencyPO extends BasePage {
    WebDriver driver;

    public TestDependencyPO(WebDriver driver) {
        this.driver = driver;
    }

    public void selectAndAddProductShirt(String productName, String value) {
        clickToElement(driver, TestDependencyPageUI.APPAREL_MENU);
        clickToElement(driver, TestDependencyPageUI.CLOTHING_CATAGORI);
        clickToElement(driver, TestDependencyPageUI.PRODUCT_DYNAMIC, productName);
        sendkeyToElement(driver, TestDependencyPageUI.NAME_TEXTBOX, value);
        clickToElement(driver, TestDependencyPageUI.ADD_TO_CART_BUTTON);
        clickToElement(driver, TestDependencyPageUI.MESSAGE_AFTER_ADD_PRODUCT);
        waitForElementVisible(driver, TestDependencyPageUI.SHOPPING_CART_TEXT);
    }

    public void selectAndAddProduct(String productName) {
        clickToElement(driver, TestDependencyPageUI.APPAREL_MENU);
        clickToElement(driver, TestDependencyPageUI.ACCESSORIES_CATAGORI);
        clickToElement(driver, TestDependencyPageUI.PRODUCT_DYNAMIC, productName);
        clickToElement(driver, TestDependencyPageUI.ADD_TO_CART_BUTTON);
        clickToElement(driver, TestDependencyPageUI.MESSAGE_AFTER_ADD_PRODUCT);
        waitForElementVisible(driver, TestDependencyPageUI.SHOPPING_CART_TEXT);
    }

    public void increaseQuantityProductInCart(String nameProduct) {
        clickToElement(driver, TestDependencyPageUI.INCREASE_QUANTITY_BUTTON, nameProduct);
        waitForElementInvisible(driver, TestDependencyPageUI.LOADING_ICON, 2);
    }

    public void decreaseQuantityProductInCart(String nameProduct) {
        clickToElement(driver, TestDependencyPageUI.DECREASE_QUANTITY_BUTTON, nameProduct);
        waitForElementInvisible(driver, TestDependencyPageUI.LOADING_ICON, 2);
    }

    public double getPriceUnitProduct(String nameProduct) {
        String priceText = getElementText(driver, TestDependencyPageUI.PRICE_PRODUCT, nameProduct);
        String totalPriceValue = priceText.replaceAll("[^0-9.]", "").trim();
        try {
            return Double.parseDouble(totalPriceValue);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot parse product price: " + priceText, e);
        }
    }

    public double getTotalPriceProduct(String nameProduct) {
        String totalPriceText = getElementText(driver, TestDependencyPageUI.TOTAL_PRICE_PRODUCT, nameProduct);
        String totalPriceValue = totalPriceText.replaceAll("[^0-9.]", "").trim();
        try {
            return Double.parseDouble(totalPriceValue);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot parse product price: " + totalPriceValue, e);
        }
    }

    public int getQuantityProduct(String nameProduct, String attribute) {
        String quantityText = getElementAttribute(driver, TestDependencyPageUI.QUANTITY_TEXTBOX, attribute,
                nameProduct);
        String quantity = quantityText.trim();
        try {
            return Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot parse product price: " + quantity, e);
        }
    }

    public void deleteProductInCart(String nameProduct) {
        clickToElement(driver, TestDependencyPageUI.DELETE_BUTTON, nameProduct);
        waitForElementVisible(driver, TestDependencyPageUI.EMPTY_CART);
    }

    public void goToHomePage() {
        clickToElement(driver, TestDependencyPageUI.HOME_PAGE);
        waitForElementVisible(driver, TestDependencyPageUI.WELCOME);
    }

    public void openCartPage(){
        clickToElement(driver, TestDependencyPageUI.CART_PAGE);
        waitForElementVisible(driver, TestDependencyPageUI.SHOPPING_CART_TEXT);
    }

    public void goToCheckoutPage(){
        clickToElement(driver, TestDependencyPageUI.CHECK_BOX_TERM);
        clickToElement(driver, TestDependencyPageUI.CHECKOUT_BUTTON);
    }

}   