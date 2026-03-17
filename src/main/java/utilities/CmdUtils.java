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

	public static void preventDeviceLock(AppiumDriver driver) {
        if (driver == null) return;

        try {
            String udid = driver.getCapabilities().getCapability("appium:udid").toString();
            String platform = driver.getCapabilities().getPlatformName().toString();

            if (platform.equalsIgnoreCase("Android")) {
                Allure.step("Thiết lập chống khoá màn hình cho Android (" + udid + ")...");
                CmdUtils.runCommand("adb -s " + udid + " shell settings put system screen_off_timeout 2147483647");
                CmdUtils.runCommand("adb -s " + udid + " shell input keyevent 26"); 
                CmdUtils.runCommand("adb -s " + udid + " shell input keyevent 82"); 

            } else if (platform.equalsIgnoreCase("iOS")) {
                Allure.step("Thiết lập chống khoá màn hình cho iOS Simulator (" + udid + ")...");
                CmdUtils.runCommand("xcrun simctl spawn " + udid + " defaults write com.apple.springboard SBIdleTimerGlobal -bool true");
            }
        } catch (Exception e) {
            Allure.step("Lỗi khi thiết lập chống khoá thiết bị: " + e.getMessage());
        }
    }

}
