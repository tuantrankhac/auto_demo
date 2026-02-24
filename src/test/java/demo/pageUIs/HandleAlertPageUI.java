package demo.pageUIs;

import org.openqa.selenium.By;

public class HandleAlertPageUI {
    public static final By ALERT = By.xpath("//a[text()='Alert with OK ']");
    public static final By ALERT_DISPLAY_BUTTON = By.xpath("//button[@class='btn btn-danger']");
    
    public static final By CONFIRM = By.xpath("//a[text()='Alert with OK & Cancel ']");
    public static final By CONFIRM_DISPLAY_BUTTON = By.xpath("//button[@class='btn btn-primary']");
    public static final By TEXT_AFTER_ACTION_CONFIRM = By.xpath("//div[@id='CancelTab']/p");
    
    public static final By PROMPT = By.xpath("//a[text()='Alert with Textbox ']");
    public static final By PROMPT_DISPLAY_BUTTON = By.xpath("//button[@class='btn btn-info']");
    public static final By TEXT_AFTER_ACTION_PROMPT = By.xpath("//div[@id='Textbox']/p");



}
