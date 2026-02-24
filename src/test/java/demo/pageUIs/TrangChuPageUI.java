package demo.pageUIs;

import org.openqa.selenium.By;

public class TrangChuPageUI {
    public static final By SIDEBAR_MENU = By.xpath("//mat-drawer-content//div[contains(@class,'sidebar-container')]/div/img");
    public static final By DYNAMIC_MENU_BY_NAME = By.xpath("//mat-drawer-content//a[contains(text(),'%s')]");
    public static final By PAGING_BUTTON = By.xpath("//app-pagination//div/span[contains(text(),'Tổng cộng')]/following-sibling::div/button[1]");
}
