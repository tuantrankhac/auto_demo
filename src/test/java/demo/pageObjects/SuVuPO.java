package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.SuVuPageUI;

import org.openqa.selenium.WebDriver;

public class SuVuPO extends BasePage {
    WebDriver driver;

    public SuVuPO(WebDriver driver) {
        this.driver = driver;
    }

    public void openNewTicketScreen() {
        clickToElement(driver, SuVuPageUI.CREATE_TICKET_BUTTON);
        waitForElementVisible(driver, SuVuPageUI.THONG_TIN_KHACH_HANG_TEXT);
    }


}