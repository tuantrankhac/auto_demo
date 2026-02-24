package demo.pageUIs;

import org.openqa.selenium.By;

public class SuVuPageUI {
    public static final By CREATE_TICKET_BUTTON = By.xpath("//button[text()=' Thêm sự vụ ']");
    public static final By THONG_TIN_KHACH_HANG_TEXT = By.xpath("//span[text()=' Thông tin khách hàng ']");
    public static final By UPLOAD_TEXT = By.xpath("//label[text()=' Ảnh sản phẩm ']");
    public static final By UPLOAD_BUTTON = By.xpath("//label[text()=' Ảnh sản phẩm ']/following-sibling::input");
    public static final By FILE_AFTER_UPLOAD = By.xpath("//label[text()=' Ảnh sản phẩm ']/following-sibling::div//img");
    public static final By THEM_DIA_CHI_BUTTON = By.xpath("//span[text()='Thêm địa chỉ']/ancestor::button");
}
