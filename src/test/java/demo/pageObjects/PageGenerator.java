package demo.pageObjects;

import org.openqa.selenium.WebDriver;

public class PageGenerator {
	public static DangNhapPO getDangNhapPage(WebDriver driver){
		return new DangNhapPO(driver);
	}

	public static TrangChuPO getTrangChuPage(WebDriver driver){
		return new TrangChuPO(driver);
	}
	
	public static SuVuPO getSuVuPage(WebDriver driver){
		return new SuVuPO(driver);
	}


}

