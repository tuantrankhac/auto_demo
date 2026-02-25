package demo.pageObjects;

import org.openqa.selenium.WebDriver;

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
}
