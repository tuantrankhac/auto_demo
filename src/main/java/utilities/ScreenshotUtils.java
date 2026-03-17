package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.io.ByteArrayInputStream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

    // Annotation @Attachment tự động lấy giá trị return (mảng byte) đính vào Allure
    @Step("Chụp ảnh khi test fail")
    public static void captureScreenshot(WebDriver driver) {
        try {
            // Chụp ảnh và lấy mảng byte
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            
            // Ép Allure nhận file trực tiếp
            Allure.addAttachment("Screenshot on Failure", "image/png", new ByteArrayInputStream(bytes), ".png");
            Allure.step("Đã đính kèm ảnh chụp màn hình vào Allure thành công!");
        } catch (Exception e) {
           Allure.step("Lỗi khi chụp màn hình: " + e.getMessage());
        }
    }
}