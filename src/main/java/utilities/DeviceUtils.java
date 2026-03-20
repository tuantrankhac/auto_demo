package utilities;

import java.time.Duration;

import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.remote.SupportsRotation;
import io.qameta.allure.Allure;

public class DeviceUtils {
    public static void rotateScreen(WebDriver driver, String orientationName) {
        // 1. Ép chuỗi String (PORTRAIT/LANDSCAPE) về dạng Enum của Selenium
        ScreenOrientation targetOrientation;
        try {
            // toUpperCase() giúp bạn truyền "portrait", "Portrait" hay "PORTRAIT" đều ăn
            // hết
            targetOrientation = ScreenOrientation.valueOf(orientationName.toUpperCase());
        } catch (IllegalArgumentException e) {
            Allure.step("❌ Lỗi: Hướng xoay '" + orientationName
                    + "' không hợp lệ! Vui lòng chỉ dùng 'PORTRAIT' hoặc 'LANDSCAPE'.");
            return;
        }

        // 2. Kiểm tra xem driver có hỗ trợ xoay không
        if (!(driver instanceof SupportsRotation)) {
            Allure.step("⚠️ Driver này không hỗ trợ tính năng xoay màn hình!");
            return;
        }

        SupportsRotation rotatableDriver = (SupportsRotation) driver;
        ScreenOrientation currentOrientation = rotatableDriver.getOrientation();

        // 3. Nếu đang ở đúng hướng rồi thì bỏ qua
        if (currentOrientation == targetOrientation) {
            Allure.step("Màn hình đang ở trạng thái " + targetOrientation + " rồi, không cần xoay.");
            return;
        }

        Allure.step("🔄 Bắt đầu xoay màn hình sang: " + targetOrientation);

        // 4. Thực hiện xoay
        rotatableDriver.rotate(targetOrientation);

        // 5. Explicit Wait - Chờ hệ điều hành xoay xong
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(d -> ((SupportsRotation) d).getOrientation() == targetOrientation);

            // Ép thêm 1 giây ngủ đông để UI App kịp vẽ lại các Element
            Thread.sleep(1000);
            Allure.step("✅ Xoay màn hình thành công và UI đã ổn định!");

        } catch (Exception e) {
            Allure.step("❌ Lỗi timeout khi chờ màn hình xoay!");
        }
    }

}
