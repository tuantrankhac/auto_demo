package utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class CmdUtils {
    // Hàm thực thi lệnh Terminal và trả về kết quả (nếu có)
    @Step("Thực hiện gọi lệnh cmd")
    public static String runCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Allure.step("Đang chạy lệnh System: " + command);

            // Xử lý đặc thù cho máy Mac/Linux và Windows
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                processBuilder.command("cmd.exe", "/c", command);
            } else {
                processBuilder.command("bash", "-c", command);
            }

            Process process = processBuilder.start();
            process.waitFor(); // Chờ Terminal chạy xong lệnh

            // Đọc log từ Terminal để biết lệnh chạy thành công hay báo lỗi
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Đọc cả luồng lỗi (nếu gõ sai lệnh)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                Allure.step("Lỗi Terminal: " + line);
            }

        } catch (Exception e) {
            Allure.step("Lỗi khi gọi Runtime: " + e.getMessage());
        }
        return output.toString();
    }

    public static void keepScreenOn(AppiumDriver driver) {
        if (driver == null)
            return;
        try {
            String udid = driver.getCapabilities().getCapability("appium:udid").toString();
            String platform = driver.getCapabilities().getPlatformName().toString();

            if (platform.equalsIgnoreCase("Android")) {
                Allure.step("Cài đặt không bao giờ tắt màn hình cho Android (" + udid + ")");
                CmdUtils.runCommand("adb -s " + udid + " shell settings put system screen_off_timeout 2147483647");
            } else if (platform.equalsIgnoreCase("iOS")) {
                Allure.step("Cài đặt không bao giờ tắt màn hình cho iOS Simulator (" + udid + ")");
                CmdUtils.runCommand("xcrun simctl spawn " + udid
                        + " defaults write com.apple.springboard SBIdleTimerGlobal -bool true");
            }
        } catch (Exception e) {
            Allure.step("Lỗi thiết lập Keep Screen On: " + e.getMessage());
        }
    }

    public static void wakeUpAndUnlockAndroid(AppiumDriver driver) {
        if (driver == null)
            return;
        try {
            String udid = driver.getCapabilities().getCapability("appium:udid").toString();
            Allure.step("Đánh thức và mở khoá màn hình Android (" + udid + ")");

            // Bật sáng màn hình
            CmdUtils.runCommand("adb -s " + udid + " shell input keyevent 26");

            // Vuốt mở khoá (Chỉ tác dụng với máy không có mã PIN)
            CmdUtils.runCommand("adb -s " + udid + " shell input keyevent 82");

        } catch (Exception e) {
            Allure.step("Lỗi Wake up thiết bị: " + e.getMessage());
        }
    }

    public static void wakeUpAndUnlockAndroid(AppiumDriver driver, String pin) {
        if (driver == null)
            return;
        try {
            String udid = driver.getCapabilities().getCapability("appium:udid").toString();

            if (pin != null && !pin.isEmpty()) {
                Allure.step("Đánh thức và mở khoá màn hình Android (" + udid + ") với PIN: " + pin);
            } else {
                Allure.step("Đánh thức và mở khoá vuốt màn hình Android (" + udid + ")");
            }

            // 1. BẬT SÁNG MÀN HÌNH (WAKE UP)
            // 💡 PRO-TIP: Dùng keyevent 224 (WAKEUP) thay vì 26 (POWER)
            // Lệnh 26 là nút Nguồn (Nếu màn hình đang sáng mà bấm 26 thì nó lại TẮT mất).
            // Lệnh 224 đảm bảo: Nếu tối thì bật sáng, nếu sáng rồi thì vẫn giữ nguyên!
            CmdUtils.runCommand("adb -s " + udid + " shell input keyevent 224");

            // 2. VUỐT LÊN (Gọi bàn phím PIN xuất hiện hoặc Mở khoá thường)
            CmdUtils.runCommand("adb -s " + udid + " shell input keyevent 82");

            // 3. XỬ LÝ NHẬP PIN (Nếu có)
            if (pin != null && !pin.isEmpty()) {
                // Đợi khoảng 1 giây để animation hệ thống vuốt xong và bàn phím PIN hiện lên
                Thread.sleep(1000);

                // Gõ từng số của mã PIN
                CmdUtils.runCommand("adb -s " + udid + " shell input text " + pin);

                // Bấm phím Enter (Keyevent 66) để xác nhận mở khoá
                CmdUtils.runCommand("adb -s " + udid + " shell input keyevent 66");
            }

        } catch (Exception e) {
            Allure.step("Lỗi Wake up thiết bị: " + e.getMessage());
        }
    }
}
