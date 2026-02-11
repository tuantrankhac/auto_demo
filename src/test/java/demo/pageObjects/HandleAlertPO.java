package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.HandleAlertPageUI;

import org.openqa.selenium.WebDriver;

public class HandleAlertPO extends BasePage {
    WebDriver driver;

    public HandleAlertPO(WebDriver driver) {
        this.driver = driver;
    }

    public void clickButtonDisplayAlert(){
        clickToElement(driver, HandleAlertPageUI.ALERT);
        clickToElement(driver, HandleAlertPageUI.ALERT_DISPLAY_BUTTON);
    }

    public String getTextAfterActionConfirm(){
        return getElementText(driver, HandleAlertPageUI.TEXT_AFTER_ACTION_CONFIRM);
    }

    public void clickButtonDisplayConfirm(){
        clickToElement(driver, HandleAlertPageUI.CONFIRM);
        clickToElement(driver, HandleAlertPageUI.CONFIRM_DISPLAY_BUTTON);
    }

    public void clickButtonDisplayPrompt(){
        clickToElement(driver, HandleAlertPageUI.PROMPT);
        clickToElement(driver, HandleAlertPageUI.PROMPT_DISPLAY_BUTTON);
    }

    public String getTextAfterActionPrompt(){
        return getElementText(driver, HandleAlertPageUI.TEXT_AFTER_ACTION_PROMPT);
    }

    public void handleUnexpectedAlert(String actionAlert){
        handleUnexpectedAlert(driver, actionAlert);
    }
}