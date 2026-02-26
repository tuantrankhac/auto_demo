package demo.pageUIs;

import org.openqa.selenium.By;

public class TestDependencyPageUI {
    public static final By APPAREL_MENU = By.linkText("Apparel");
    public static final By CLOTHING_CATAGORI = By.cssSelector(".title a[href='/clothing']");
    public static final By ACCESSORIES_CATAGORI = By.cssSelector(".title a[href='/accessories']");
    public static final String PRODUCT_DYNAMIC = "//a[text()='%s']";
    public static final By NAME_TEXTBOX = By.xpath("//label[text()=' Enter your text: ']/parent::dt/following-sibling::dd/input");
    public static final By ADD_TO_CART_BUTTON = By.cssSelector("button[id^='add-to-cart-button-']");
    public static final By MESSAGE_AFTER_ADD_PRODUCT = By.cssSelector("a[href='/cart']");
    public static final By SHOPPING_CART_TEXT = By.cssSelector(".page-title h1");
    


    public static final By LOADING_ICON = By.cssSelector("div[class*='ajax-loading']");
    public static final String DECREASE_QUANTITY_BUTTON = "//a[text()='%s']/parent::td/following-sibling::td//div[@class='quantity down']";
    public static final String INCREASE_QUANTITY_BUTTON = "//a[text()='%s']/parent::td/following-sibling::td//div[@class='quantity up']";
    public static final String QUANTITY_TEXTBOX = "//a[text()='%s']/parent::td/following-sibling::td/div/input";
    public static final String PRICE_PRODUCT = "//a[text()='%s']/parent::td/following-sibling::td/span[@class='product-unit-price']";
    public static final String TOTAL_PRICE_PRODUCT = "//a[text()='%s']/parent::td/following-sibling::td/span[@class='product-subtotal']";
    public static final String DELETE_BUTTON = "//a[text()='Custom T-Shirt']/parent::td/following-sibling::td/button";
    public static final By EMPTY_CART = By.className("no-data");
    

    public static final By HOME_PAGE = By.cssSelector(".header-logo a");
    public static final By WELCOME = By.cssSelector(".topic-block-title h2");
    public static final By CART_PAGE = By.cssSelector(".cart-label");
    

    public static final By CHECK_BOX_TERM = By.cssSelector("input#termsofservice");
    public static final By CHECKOUT_BUTTON = By.cssSelector("button#checkout");
    public static final By CHECKOUT_AS_GUEST_BUTTON = By.cssSelector("button.checkout-as-guest-button");
    // public static final By CHECKOUT_AS_GUEST_BUTTON = By.cssSelector("button[class*='checkout-as-guest-button']");
    
}
