package demo.web.pageObjects;

import commons.BasePage;
import commons.PageGenerator;
import demo.web.pageUIs.LoginPageUI;

import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
	WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}

	public void enterToUsernameTextbox(String username) {
		waitForElementVisible(driver, LoginPageUI.USERNAME_TEXTBOX);
		sendkeyToElement(driver, LoginPageUI.USERNAME_TEXTBOX, username);
	}

	public void enterToPasswordTextbox(String password) {
		waitForElementVisible(driver, LoginPageUI.PASSWORD_TEXTBOX);
		sendkeyToElement(driver, LoginPageUI.PASSWORD_TEXTBOX, password);
	}

	public void checkToRememberMeCheckbox() {
		waitForElementClickable(driver, LoginPageUI.REMEMBER_ME_CHECKBOX);
		checkToDefaultCheckboxRadio(driver, LoginPageUI.REMEMBER_ME_CHECKBOX);
	}

	public void uncheckToRememberMeCheckbox() {
		waitForElementClickable(driver, LoginPageUI.REMEMBER_ME_CHECKBOX);
		uncheckToDefaultCheckbox(driver, LoginPageUI.REMEMBER_ME_CHECKBOX);
	}

	public void clickToForgotPasswordLink() {
		waitForElementClickable(driver, LoginPageUI.FORGOT_PASSWORD_LINK);
		clickToElement(driver, LoginPageUI.FORGOT_PASSWORD_LINK);
	}

	public void clickToLoginButton() {
		waitForElementClickable(driver, LoginPageUI.LOGIN_BUTTON);
		clickToElement(driver, LoginPageUI.LOGIN_BUTTON);
	}

	public TrangChuPO loginWithAccount(String username, String password) {
		enterToUsernameTextbox(username);
		enterToPasswordTextbox(password);
		clickToLoginButton();
		waitForElementInvisible(driver, LoginPageUI.LOGIN_BUTTON);
		return PageGenerator.getTrangChuPage(driver);
	}

	public TrangChuPO loginWithAccount(String username, String password, boolean rememberMe) {
		enterToUsernameTextbox(username);
		enterToPasswordTextbox(password);
		if (rememberMe) {
			checkToRememberMeCheckbox();
		} else {
			uncheckToRememberMeCheckbox();
		}
		clickToLoginButton();
		waitForElementInvisible(driver, LoginPageUI.LOGIN_BUTTON);
		return PageGenerator.getTrangChuPage(driver);
	}

	public boolean isLoginPageDisplayed() {
		return !isElementUndisplayed(driver, LoginPageUI.LOGIN_BUTTON);
	}

	public boolean isRememberMeChecked() {
		return isElementSelected(driver, LoginPageUI.REMEMBER_ME_CHECKBOX);
	}
}
