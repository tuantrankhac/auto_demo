package demo.pageUIs;
import org.openqa.selenium.By;

public class ReadExcelFilePageUI {
    public static final By LOGIN_SCREEN = By.tagName("h1");
    public static final By EMAIL_TEXTBOX = By.cssSelector("input[formcontrolname='email']");
    public static final By PASSWORD_TEXTBOX = By.cssSelector("input[formcontrolname='password']");
    public static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    public static final By FIRST_ELEMENT = By.cssSelector("tbody tr:first-child");
    
}
