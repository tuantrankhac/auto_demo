package demo.mobile.pageObjects;

import commons.BasePage;
import demo.mobile.pageUIs.DangNhapAppPageUI;
import utilities.DeviceUtils;

import org.openqa.selenium.WebDriver;


public class DangNhapAppPO extends BasePage {
    WebDriver driver;

    public DangNhapAppPO(WebDriver driver) {
        this.driver = driver;
    }
    
    public void dangNhapApp(String userName, String password){
        clickToElement(driver, DangNhapAppPageUI.LOGIN_BUTTON);
        clickToElement(driver, DangNhapAppPageUI.ALLOW_MEDIA);
        clickToElement(driver, DangNhapAppPageUI.ALLOW_ACCESS);
        clickAndSendkeyToElement(driver, DangNhapAppPageUI.USERNAME_TEXTBOX, userName);
        clickAndSendkeyToElement(driver, DangNhapAppPageUI.PASSWORD_TEXTBOX, password);
        clickToElement(driver, DangNhapAppPageUI.NEXT_BUTTON);
        waitForElementVisible(driver, DangNhapAppPageUI.MANAGE_TICKET_SCREEN);
        sleepInMiliSecond(3);
        waitForElementInvisible(driver, DangNhapAppPageUI.PROCESS_TAB_ZERO);
    }

    public void scrollToTicket(String ticketID){
        smartScrollToElement(driver, DangNhapAppPageUI.DYNAMIC_TICKET, DangNhapAppPageUI.SCROLL_CONDITION_DESC, DangNhapAppPageUI.SCROLL_LABEL_CONTAINS, ticketID);
    }
    
    public void openDetailTicket(String ticketID){
        clickToElement(driver, DangNhapAppPageUI.DYNAMIC_TICKET, ticketID);
        waitForElementVisible(driver, DangNhapAppPageUI.DETAIL_DYNAMIC_TICKET, ticketID);
    }

    public void rotateScreen(String orientationName){
        DeviceUtils.rotateScreen(driver, orientationName);
    }

}   