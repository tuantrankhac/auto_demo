package demo.pageUIs;

import org.openqa.selenium.By;

public class TestDependencyPageUI {
    public static final By APPAREL_MENU = By.linkText("Apparel");
    public static final By CLOTHING_CATAGORI = By.cssSelector("a[href='/clothing']");
    public static final String PRODUCT_DYNAMIC = "//a[text()='%s']";
    public static final By NAME_TEXTBOX = By.xpath("//label[text()=' Enter your text: ']/parent::dt/following-sibling::dd/input");
    public static final By ADD_TO_CART_BUTTON = By.cssSelector("button[id^='add-to-cart-button-']");
    public static final By MESSAGE_AFTER_ADD_PRODUCT = By.cssSelector("a[href='/cart']");
    public static final By SHOPPING_CART_TEXT = By.cssSelector(".page-title h1");
    
    public static final By DECREASE_QUANTITY_BUTTON = By.cssSelector(".quantity.up");
    public static final By INCREASE_QUANTITY_BUTTON = By.cssSelector(".quantity.down");
    public static final String QUANTITY_TEXTBOX = "//a[text()='%s']/parent::td/following-sibling::td/div/input";
    public static final String PRICE_PRODUCT = "//a[text()='Custom T-Shirt']/parent::td/following-sibling::td/span[@class='product-unit-price']";
    public static final String TOTAL_PRICE_PRODUCT = "//a[text()='Custom T-Shirt']/parent::td/following-sibling::td/span[@class='product-subtotal']";
   
    

    public static final By DELETE_PRODUCT_IN_CART = By.cssSelector(".remove-btn");
    
}
