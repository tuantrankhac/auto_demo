package demo.testSuites;

import commons.BrowserFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import demo.pageObjects.DangNhapPO;
import demo.pageObjects.PageGenerator;
import demo.pageObjects.SuVuPO;
import demo.pageObjects.TrangChuPO;
import utilities.DbConnection;


public class ValidataDataOnUIAndDB extends BrowserFactory {
    String user = "tongdai3@gmail.com";
    String password = "123456";
    String fileUpload1 = "picture_1.jpg";
    String[] multiFileUpload = {"picture_1.jpg", "picture_2.jpg"};
    @Parameters({"browser", "url"})
    @BeforeClass
    public void beforeClass(String browserName, String appUrl) throws SQLException {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName + ", URL: " + appUrl);
        driver = getBrowserDriver(browserName,appUrl);
        db = new DbConnection();
        db.connect();

        dangNhapPO = PageGenerator.getDangNhapPage(driver);
        trangChuPO = dangNhapPO.loginWithAccount(user, password);
    }
    
    @Test(priority = 1)
    public void CreateIncidentSuccess() throws SQLException{
        suVuPO = trangChuPO.openMenuByName("Dịch vụ bảo hành");
        suVuPO.openNewTicketScreen();
        String idTicket = suVuPO.creatTicket().trim();

        Map<String, Object> params = new HashMap<>();
        // params.put("selectColumns", Arrays.asList("t.id", "t.status", "u.username"));
        params.put("selectColumns", Arrays.asList("t.id"));
        params.put("fromTable", "tickets t");
        // params.put("joins", "JOIN users u ON t.created_by = u.id");
        params.put("whereCondition", "t.business_key = ?");
        params.put("orderBy", "t.created_at DESC");
        // params.put("parameters", Arrays.asList("INC-20260201"));

        Map<String, Object> latestTicket = DbConnection.getValueRecord(params);
        verifyEquals(latestTicket, idTicket);
    }

    
    @AfterClass(alwaysRun = true)
    public void afterClass() throws SQLException {
        if (db != null) {
            db.disconnect();
        }
        closeAllBrowsers();
    }


    private WebDriver driver;
    private DbConnection db;
    DangNhapPO dangNhapPO;
    TrangChuPO trangChuPO;
    SuVuPO suVuPO;
}
