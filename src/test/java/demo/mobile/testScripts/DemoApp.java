package demo.mobile.testScripts;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import commons.BaseTest;
import commons.PageGenerator;
import demo.mobile.pageObjects.DangNhapAppPO;

public class DemoApp extends BaseTest{
    @Parameters({ "deviceName", "appiumUrl" })
    @BeforeMethod
    public void beforeMethod(String deviceName, String appiumUrl) {
        driver = getMobileDriver(deviceName, appiumUrl);
        dangNhapAppPO = PageGenerator.getDangNhapAppPO(driver);

    }

    @Test(priority = 1)
    public void LoginKTV() {
        String userName = "0342992916";
        String password = "123456";
        String ticketID = "1118176";
        String orientationDevice = "PORTRAIT";
        dangNhapAppPO.dangNhapApp(userName, password);
        dangNhapAppPO.scrollToTicket(ticketID);
        dangNhapAppPO.openDetailTicket(ticketID);
        dangNhapAppPO.rotateScreen(orientationDevice);
    }

    

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        closeAllMobiles();
    }

    private WebDriver driver;
    DangNhapAppPO dangNhapAppPO;
}
