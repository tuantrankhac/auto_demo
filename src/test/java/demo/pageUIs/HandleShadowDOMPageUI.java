package demo.pageUIs;
import org.openqa.selenium.By;

public class HandleShadowDOMPageUI {
    public static final By SINGLE_SHADOW_DOM = By.id("shadow-host");
    public static final By ELEMENT_IN_SINGLE_SHADOW_DOM = By.xpath("//input[@type='email']");

    public static final By SHADOW_DOM_NESTED_PARENT = By.xpath("//input[@type='email']");
    public static final By SHADOW_DOM_NESTED_CHILD = By.xpath("//input[@type='email']");
    public static final By ELEMENT_IN_SHADOW_DOM_NESTED = By.xpath("//input[@type='email']");

}
