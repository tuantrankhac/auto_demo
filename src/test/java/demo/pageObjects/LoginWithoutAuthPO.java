package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.ActionsPagePUI;

import org.openqa.selenium.WebDriver;

public class LoginWithoutAuthPO extends BasePage {
    WebDriver driver;

    public LoginWithoutAuthPO(WebDriver driver) {
        this.driver = driver;
    }

    public void actionForward(){
        waitForElementVisible(driver, ActionsPagePUI.ACTION_1);
        clickToElement(driver, ActionsPagePUI.ACTION_1);
        waitForElementVisible(driver, ActionsPagePUI.TEXT);
        backToPageByNavigate(driver);
    }



}