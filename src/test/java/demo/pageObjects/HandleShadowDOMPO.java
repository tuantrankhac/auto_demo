package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.HandleShadowDOMPageUI;

import org.openqa.selenium.WebDriver;

public class HandleShadowDOMPO extends BasePage {
    WebDriver driver;

    public HandleShadowDOMPO(WebDriver driver) {
        this.driver = driver;
    }

    public void getTextElementInShadow(){
        getTextElementInShadowRoot(driver, HandleShadowDOMPageUI.SINGLE_SHADOW_DOM, HandleShadowDOMPageUI.ELEMENT_IN_SINGLE_SHADOW_DOM);
    }

    public void getTextElementInNestedShadow(){
        getElementInNestedShadowRoot(driver, HandleShadowDOMPageUI.SINGLE_SHADOW_DOM, HandleShadowDOMPageUI.ELEMENT_IN_SINGLE_SHADOW_DOM);
    }

}