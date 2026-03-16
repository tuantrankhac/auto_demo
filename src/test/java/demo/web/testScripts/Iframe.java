package demo.web.testScripts;

import commons.BaseTest;
import commons.PageGenerator;
import demo.web.pageObjects.IframePO;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class Iframe extends BaseTest {
    @Parameters({"browser"})
    @BeforeMethod
    public void beforeMethod(String browserName){
        log.info("Pre-Condition: Step 01: Open Browser: " + browserName);
        driver = getBrowserDriver(browserName);
        iframePO = PageGenerator.getIframePO(driver);

    }
    
    @Test(priority = 1)
    public void SwitchIframeByWebElement() {
        driver.get("https://www.w3schools.com/html/tryit.asp?filename=tryhtml_iframe");
        iframePO.SwitchIframeByWebElement();
        iframePO.switchToDefaultContent(driver);
    }

    @Test(priority = 2)
    public void SwitchIframeByNameOrId() {
        String nameOrId = "iframeResult";
        driver.get("https://www.w3schools.com/html/tryit.asp?filename=tryhtml_iframe");
        iframePO.SwitchIframeByNameOrID(nameOrId);
        iframePO.switchToDefaultContent(driver);
    }

    @Test(priority = 3)
    public void SwitchIframeByIndex() {
        int index = 0;
        driver.get("https://demo.automationtesting.in/Frames.html");
        iframePO.SwitchIframeByIndex(index);
    }



    @AfterMethod(alwaysRun = true)
    public void afterMethod(){
        closeAllBrowsers();
    }


    private WebDriver driver;
    IframePO iframePO;
}
