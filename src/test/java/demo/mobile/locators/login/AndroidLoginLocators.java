package demo.mobile.locators.login;

import org.openqa.selenium.By;

import io.appium.java_client.AppiumBy;

public class AndroidLoginLocators implements LoginLocators{
    @Override
    public By usernameField() {
        return AppiumBy.accessibilityId("username-input");
    }

    @Override
    public By passwordField() {
        return AppiumBy.accessibilityId("password-input");
    }   

    @Override
    public By loginButton() {
        return AppiumBy.accessibilityId("login-button");
    }

    @Override
    public By loadingIndicator() {
        return AppiumBy.accessibilityId("loading-indicator");
    }
}