package demo.web.pageObjects;

import commons.BasePage;
import demo.web.pageUIs.SwitchTabPageUI;

import org.openqa.selenium.WebDriver;

public class SwitchTabPO extends BasePage {
    WebDriver driver;

    public SwitchTabPO(WebDriver driver) {
        this.driver = driver;
    }

    public String getWindowParentHandle() {
        return driver.getWindowHandle();
    }

    public void clickLinkToNewTab() {
        int currentWindowHandlesCount = driver.getWindowHandles().size();
        clickToElement(driver, SwitchTabPageUI.CLICK_HERE);
        waitForNumberOfWindowsToIncrease(driver, currentWindowHandlesCount);
    }

    public String getTextInNewTab() {
        return getElementText(driver, SwitchTabPageUI.TEXT_IN_NEW_TAB);
    }

    public String getTextInInitTab() {
        return getElementText(driver, SwitchTabPageUI.TEXT_IN_INIT_TAB);
    }

    public void switchWindowByTitle(String title){
        switchWindowByTitle(driver, title);
    }

    public void switchWindowByUrl(String url){
        switchWindowByUrl(driver, url);
    }

    public void switchWindowByIndex(String index){
        switchWindowByUrl(driver, index);
    }

    public void closeWindowAndSwitchToParent(String windowParent){
        closeCurrentWindowAndSwitchToParent(driver, windowParent);
    }

}