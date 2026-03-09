package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.HandleShadowDOMPageUI;

import org.openqa.selenium.WebDriver;

public class HandleDownloadFilePO extends BasePage {
    WebDriver driver;

    public HandleDownloadFilePO(WebDriver driver) {
        this.driver = driver;
    }

    public void getTextElementInShadow(){
        getTextElementInShadowRoot(driver, HandleShadowDOMPageUI.SINGLE_SHADOW_DOM, HandleShadowDOMPageUI.ELEMENT_IN_SINGLE_SHADOW_DOM);
    }

    public void getTextElementInNestedShadow(){
        getTextElementInNestedShadowRoot(driver,HandleShadowDOMPageUI.ELEMENT_IN_SHADOW_DOM_NESTED, HandleShadowDOMPageUI.SHADOW_DOM_NESTED_PARENT, HandleShadowDOMPageUI.SHADOW_DOM_NESTED_CHILD);
    }

}