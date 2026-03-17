package utilities;

import io.appium.java_client.screenrecording.CanRecordScreen;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.WebDriver;
import java.util.Base64;

public class VideoRecorderUtils {

    // 1. Bật quay video
    @Step("Bật quay video")
    public static void startRecording(WebDriver driver) {
        
        if (driver instanceof CanRecordScreen) {
            ((CanRecordScreen) driver).startRecordingScreen();
        }
    }

    // 2. Dừng và Lưu video (Dùng khi test FAIL)
    @Step("Dừng và lưu video")
    public static void stopAndAttachVideo(WebDriver driver) {
        if (driver instanceof CanRecordScreen) {
            try {
                // Dừng quay và lấy chuỗi base64
                String base64Video = ((CanRecordScreen) driver).stopRecordingScreen();
                
                // Giải mã thành mảng byte
                byte[] decodedVideo = Base64.getDecoder().decode(base64Video);
                
                // Ép Allure nhận file trực tiếp
                Allure.addAttachment("Test Execution Video", "video/mp4", new ByteArrayInputStream(decodedVideo), ".mp4");
                Allure.step("Đã đính kèm video quay màn hình vào Allure thành công!");
                
            } catch (Exception e) {
                Allure.step("Lỗi khi đính kèm video: " + e.getMessage());
            }
        }
    }

    // 3. CHỈ Dừng quay và xoá bỏ (Dùng khi test PASS/SKIP để tránh tràn RAM)
    @Step("Xoá video sau khi test pass")
    public static void stopRecordingWithoutSaving(WebDriver driver) {
        if (driver instanceof CanRecordScreen) {
            try {
                ((CanRecordScreen) driver).stopRecordingScreen(); 
            } catch (Exception e) {
                // Bỏ qua lỗi
            }
        }
    }

    @Attachment(value = "Test Execution Video", type = "video/mp4")
    private static byte[] attachVideoToAllure(String base64Video) {
        return Base64.getDecoder().decode(base64Video);
    }
}