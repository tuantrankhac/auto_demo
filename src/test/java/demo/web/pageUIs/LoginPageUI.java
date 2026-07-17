package demo.web.pageUIs;

import org.openqa.selenium.By;

/**
 * Locators for Login form at /login
 * Priority: id → name → css → xpath
 */
public class LoginPageUI {

	public static final By USERNAME_TEXTBOX = By.cssSelector("input[formcontrolname='email']");
	public static final By PASSWORD_TEXTBOX = By.id("crmPassword");
	public static final By REMEMBER_ME_CHECKBOX = By.cssSelector("form input[type='checkbox']");
	public static final By FORGOT_PASSWORD_LINK = By.cssSelector("form a.no-underline");
	public static final By LOGIN_BUTTON = By.cssSelector("form button[type='submit']");

}
