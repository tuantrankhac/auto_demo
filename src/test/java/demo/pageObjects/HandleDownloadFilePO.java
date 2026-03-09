package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.HandleDownloadFilePageUI;

import java.io.File;

import org.openqa.selenium.WebDriver;

public class HandleDownloadFilePO extends BasePage {
    WebDriver driver;

    public HandleDownloadFilePO(WebDriver driver) {
        this.driver = driver;
    }



    public void clickDownloadFileButton(){
        clickToElement(driver, HandleDownloadFilePageUI.DOWNLOAD_FILE_BUTTON);
    }

    public String getDownloadFileName(){
        String fileName = waitAndGetDownloadFileName(driver, 10);
        return fileName;
    }

    public void waitForFileDownloadSuccess(){
        waitForFileDownloadSuccess();
    }

    public String getLatestFileAfterDownloaded(){
        File latestFile = getLatestFileInDir();
        String actualFileNameOnDisk = latestFile.getName();
        return actualFileNameOnDisk;
    }

   

}