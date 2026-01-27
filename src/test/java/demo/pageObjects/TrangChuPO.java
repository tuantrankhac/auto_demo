package demo.pageObjects;

import commons.BasePage;
import demo.pageUIs.TrangChuPageUI;

import org.openqa.selenium.WebDriver;

public class TrangChuPO extends BasePage {
    WebDriver driver;

    public TrangChuPO(WebDriver driver) {
        this.driver = driver;
    }

    public SuVuPO openMenuByName(String nameMenu) {
        // Bước 1: Thử click vào sidebar để mở
        try {
            clickToElement(driver, TrangChuPageUI.SIDEBAR_MENU);
            sleepInMiliSecond(2000);
        } catch (Exception e) {
            System.out.println("Không thể click toggle sidebar: " + e.getMessage());
        }
        // Bước 2: Kiểm tra menu mong muốn có thể click
        try {
            waitForElementClickable(driver, TrangChuPageUI.DYNAMIC_MENU_BY_NAME, nameMenu);
        } catch (Exception e) {
            // Menu vẫn không xuất hiện sau khi thử mở sidebar → thử click toggle lần nữa (để quay lại trạng thái cũ)
            try {
                clickToElement(driver, TrangChuPageUI.SIDEBAR_MENU);
                sleepInMiliSecond(2000);
            } catch (Exception ignored) {
                // Không làm gì nếu click lần 2 cũng fail
            }
            // Thử chờ menu lần cuối
            waitForElementClickable(driver, TrangChuPageUI.DYNAMIC_MENU_BY_NAME, nameMenu);
        }
        //Bước 3: Scroll đến menu

        //Bước 4: Click vào menu
        clickToElement(driver, TrangChuPageUI.DYNAMIC_MENU_BY_NAME, nameMenu);
        waitForElementVisible(driver, TrangChuPageUI.PAGING_BUTTON);
    
        return PageGenerator.getSuVuPage(driver);
    }

}