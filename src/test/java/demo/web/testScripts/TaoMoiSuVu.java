package demo.web.testScripts;

import commons.BaseTest;
import demo.web.pageObjects.DangNhapPO;
import demo.web.pageObjects.PageGenerator;
import demo.web.pageObjects.SuVuPO;
import demo.web.pageObjects.TrangChuPO;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TaoMoiSuVu extends BaseTest {
    String user = "tongdai3@gmail.com";
    String password = "123456";
    @Parameters({"browser", "url"})
    @BeforeClass
    public void beforeClass(String browserName, String appUrl) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName + ", URL: " + appUrl);
        driver = getBrowserDriver(browserName,appUrl);
        dangNhapPO = PageGenerator.getDangNhapPage(driver);
    }

    @Test (priority = 1)
    public void DangNhapThanhCong() {
        trangChuPO = dangNhapPO.loginWithAccount(user, password);
        suVuPO = trangChuPO.openMenuByName("Dịch vụ bảo hành");
        suVuPO.openNewTicketScreen();
        


        
    }


    @AfterClass(alwaysRun = true)
    public void afterClass() {
        closeAllBrowsers();
    }
    private WebDriver driver;
    DangNhapPO dangNhapPO;
    TrangChuPO trangChuPO;
    SuVuPO suVuPO;
}
