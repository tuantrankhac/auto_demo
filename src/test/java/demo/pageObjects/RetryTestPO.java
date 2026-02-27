package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.ReadExcelFilePageUI;

import org.openqa.selenium.WebDriver;

public class RetryTestPO extends BasePage {
    WebDriver driver;

    public RetryTestPO(WebDriver driver) {
        this.driver = driver;
    }

    public void loginPage(String userName, String password) {
       waitForElementStaleness(driver, ReadExcelFilePageUI.EMAIL_TEXTBOX);
       sendkeyToElement(driver, ReadExcelFilePageUI.EMAIL_TEXTBOX, userName);
       sendkeyToElement(driver, ReadExcelFilePageUI.PASSWORD_TEXTBOX, password);
       clickToElement(driver, ReadExcelFilePageUI.LOGIN_BUTTON);
       waitForElementVisible(driver, ReadExcelFilePageUI.FIRST_ELEMENT);
    }


}   