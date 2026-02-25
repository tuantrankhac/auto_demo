package demo.pageUIs;
import org.openqa.selenium.By;

public class HandleShadowDOMPageUI {
    public static final By SINGLE_SHADOW_DOM = By.id("shadow-host");
    public static final By ELEMENT_IN_SINGLE_SHADOW_DOM = By.cssSelector("button[type='button']");

    public static final By SHADOW_DOM_NESTED_PARENT = By.cssSelector("shop-app[page='home']");
    public static final By SHADOW_DOM_NESTED_CHILD = By.cssSelector("shop-home[name='home']");
    public static final By ELEMENT_IN_SHADOW_DOM_NESTED = By.cssSelector("h2");

}
