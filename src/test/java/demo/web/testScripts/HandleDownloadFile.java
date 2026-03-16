package demo.web.testScripts;

import commons.BaseTest;
import commons.PageGenerator;
import demo.web.pageObjects.HandleDownloadFilePO;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class HandleDownloadFile extends BaseTest {
    @Parameters({ "browser", "url" })
    @BeforeMethod
    public void BeforeMethod(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName, url);
        handleDownloadFilePO = PageGenerator.getHandleDownloadFilePO(driver);
    }

    @Test
    public void TC01_verifyFileAfterDownloadViaCDP() {
        handleDownloadFilePO.clickDownloadFileButton();
        String fileName = handleDownloadFilePO.getDownloadFileName();
        handleDownloadFilePO.waitForFileDownloadSuccess();
        String fileNameAfterDownload = handleDownloadFilePO.getLatestFileAfterDownloaded();
        verifyEquals(fileName, fileNameAfterDownload);
    }
    

    @AfterMethod(alwaysRun = true)
    public void AfterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    HandleDownloadFilePO handleDownloadFilePO;
}
