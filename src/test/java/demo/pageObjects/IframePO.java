package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.IframePageUI;

import org.openqa.selenium.WebDriver;


public class IframePO extends BasePage {
    WebDriver driver;

    public IframePO(WebDriver driver) {
        this.driver = driver;
    }

    public String SwitchIframeByWebElement() {
        // waitForElementVisible(driver, IframePageUI.MAIN_FRAME);
        switchToFrameByWebElement(driver, IframePageUI.PARENT_FRAME);
        switchToFrameByWebElement(driver, IframePageUI.CHILD_FRAME);
        return getElementText(driver, IframePageUI.ELEMENT_IN_CHILD_FRAME_TEXT);

    }

    public String SwitchIframeByNameOrID(String nameOrIdIfr){
        // waitForElementVisible(driver, IframePageUI.MAIN_FRAME);
        switchToFrameByNameOrId(driver, nameOrIdIfr);
        return getElementText(driver, IframePageUI.ELEMTN_IN_PARENT_FRAME_TEXT);
    }

    public String SwitchIframeByIndex(int index){
        waitForElementVisible(driver, IframePageUI.PARENT_FRAME_N);
        switchToFrameByIndex(driver, index);
        return getElementText(driver,IframePageUI.CHILD_FRAME_N);
    }


    public void SwitchDefaultContent(){
        switchToDefaultContent(driver);

    }

}