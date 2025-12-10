package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.DangNhapPageUI;

import org.openqa.selenium.WebDriver;


public class TrangChuPO extends BasePage {
    WebDriver driver;

    public TrangChuPO(WebDriver driver) {
        this.driver = driver;
    }
    public void enterToUserTextbox(String user){
        sendkeyToElement(driver, DangNhapPageUI.USERNAME_TEXTBOX, user);
    }

    public void enterToPasswordTextbox(String password){
        sendkeyToElement(driver, DangNhapPageUI.PASSWORD_TEXTBOX, password);
    }

    public void clickToLoginButton(){
        clickToElement(driver, DangNhapPageUI.LOGIN_BUTTON);
    }
}   