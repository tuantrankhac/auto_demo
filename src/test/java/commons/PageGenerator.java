package commons;

import org.openqa.selenium.WebDriver;

import demo.mobile.pageObjects.DangNhapAppPO;
import demo.web.pageObjects.ActionsPagePO;
import demo.web.pageObjects.DangNhapPO;
import demo.web.pageObjects.HandleAlertPO;
import demo.web.pageObjects.HandleDownloadFilePO;
import demo.web.pageObjects.HandleShadowDOMPO;
import demo.web.pageObjects.IframePO;
import demo.web.pageObjects.ReadExcelFilePO;
import demo.web.pageObjects.RetryTestPO;
import demo.web.pageObjects.SuVuPO;
import demo.web.pageObjects.SwitchTabPO;
import demo.web.pageObjects.TestDependencyPO;
import demo.web.pageObjects.TrangChuPO;

public class PageGenerator {
	public static DangNhapPO getDangNhapPage(WebDriver driver) {
		return new DangNhapPO(driver);
	}

	public static TrangChuPO getTrangChuPage(WebDriver driver) {
		return new TrangChuPO(driver);
	}

	public static SuVuPO getSuVuPage(WebDriver driver) {
		return new SuVuPO(driver);
	}

	public static IframePO getIframePO(WebDriver driver) {
		return new IframePO(driver);
	}

	public static HandleAlertPO getHandleAlertPO(WebDriver driver) {
		return new HandleAlertPO(driver);
	}

	public static ActionsPagePO getActionsPagePO(WebDriver driver) {
		return new ActionsPagePO(driver);
	}

	public static SwitchTabPO getSwitchTabPO(WebDriver driver) {
		return new SwitchTabPO(driver);
	}

	public static HandleShadowDOMPO getHandleShadowDOMPO(WebDriver driver) {
		return new HandleShadowDOMPO(driver);
	}

	public static TestDependencyPO getDependencyPO(WebDriver driver) {
		return new TestDependencyPO(driver);
	}

	public static ReadExcelFilePO getExcelFilePO(WebDriver driver) {
		return new ReadExcelFilePO(driver);
	}

	public static RetryTestPO getRetryTestPO(WebDriver driver) {
		return new RetryTestPO(driver);
	}

	public static HandleDownloadFilePO getHandleDownloadFilePO(WebDriver driver){
		return new HandleDownloadFilePO(driver);
	}

	public static DangNhapAppPO getDangNhapAppPO(WebDriver driver){
		return new DangNhapAppPO(driver);
	}

}
