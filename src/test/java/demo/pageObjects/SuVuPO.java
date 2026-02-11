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
        waitLoadingIsInvisible(driver);
        clickToElement(driver, SuVuPageUI.CREATE_TICKET_BUTTON);
        waitForElementClickable(driver, SuVuPageUI.THEM_DIA_CHI_BUTTON);
    }

    public void scrollToUploadButton() {
        scrollToElementCenterByJS(driver, SuVuPageUI.UPLOAD_TEXT);
    }

    public void uploadOneFile(String fileName) {
        uploadFile(driver, SuVuPageUI.UPLOAD_BUTTON, fileName);
    }

    public void uploadMultiFilesSequentially(String... filesName) {
        uploadFilesSequentiallyWithWait(driver, SuVuPageUI.FILE_AFTER_UPLOAD, SuVuPageUI.UPLOAD_BUTTON, filesName);

    }

    public boolean isOneFileUploadedSuccess() {
        return isElementDisplayed(driver, SuVuPageUI.FILE_AFTER_UPLOAD);
    }

    public int getNumberFileUploadedSuccess() {
        return getListWebElement(driver, SuVuPageUI.FILE_AFTER_UPLOAD).size();
    }

    public String creatTicket(){
        return getElementText(driver, SuVuPageUI.THONG_TIN_KHACH_HANG_TEXT);
    }

}