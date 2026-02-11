package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.ActionsPagePUI;

import org.openqa.selenium.WebDriver;

public class ActionsPagePO extends BasePage {
    WebDriver driver;

    public ActionsPagePO(WebDriver driver) {
        this.driver = driver;
    }

    public void actionForward(){
        waitForElementVisible(driver, ActionsPagePUI.ACTION_1);
        clickToElement(driver, ActionsPagePUI.ACTION_1);
        waitForElementVisible(driver, ActionsPagePUI.TEXT);
        backToPageByNavigate(driver);
    }

    public void actionBack(){
        waitForElementVisible(driver, ActionsPagePUI.ACTION_1);
        clickToElement(driver, ActionsPagePUI.ACTION_1);
        waitForElementVisible(driver, ActionsPagePUI.TEXT);
    }

    public boolean textVisibleAfterForward(){
        return isElementDisplayed(driver, ActionsPagePUI.TEXT);
    }

    public boolean textVisibleAfterBack(){
        return isElementDisplayed(driver, ActionsPagePUI.ACTION_1);
    }

}