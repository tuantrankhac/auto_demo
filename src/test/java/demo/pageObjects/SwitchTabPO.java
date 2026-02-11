package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.SwitchTabPageUI;

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

}