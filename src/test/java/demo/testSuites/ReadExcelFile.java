package demo.testSuites;

import commons.BrowserFactory;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import demo.pageObjects.PageGenerator;
import demo.pageObjects.ReadExcelFilePO;
import utilities.ExcelUtils;

public class ReadExcelFile extends BrowserFactory {
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        // Gọi hàm đọc từ ExcelUtils
        String fileName = "LoginData.xlsx";
        String sheetName = "Login Data";
        List<Map<String, String>> data = ExcelUtils.getSheetData(fileName, sheetName);
        
        // Chuyển List thành mảng Object[][] cho TestNG
        Object[][] obj = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            obj[i][0] = data.get(i);
        }
        return obj;
    }
    
    @Parameters({ "browser", "url" })
    @BeforeMethod
    public void BeforeMethod(String browserName, String url) {
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName, url);
        readExcelFilePO = PageGenerator.getExcelFilePO(driver);
    }

    @Test (dataProvider = "loginData")
    public void TC01_testLogin(Map<String, String> data) {
        // Kiểm tra Runmode trước khi chạy
        // Nếu cột RunMode trong excel có giá trị = 0 thì return
        if (data.get("RunMode").equalsIgnoreCase("No")) return;

        String userName = data.get("User");
        String password = data.get("Password");
        readExcelFilePO.loginPage(userName, password);
    }
    

    @AfterMethod(alwaysRun = true)
    public void AfterMethod() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    ReadExcelFilePO readExcelFilePO;
}
