package demo.testSuites;

import commons.BrowserFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import demo.pageObjects.DangNhapPO;
import demo.pageObjects.PageGenerator;
import demo.pageObjects.SuVuPO;
import demo.pageObjects.TrangChuPO;

public class TaoMoiSuVu extends BrowserFactory {
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
