package commons;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MobileFactory {
    // ThreadLocal để quản lý driver riêng biệt cho từng luồng
    private static ThreadLocal<AppiumDriver> threadDriver = new ThreadLocal<>();

    public static AppiumDriver getDriver() {
        return threadDriver.get();
    }

    // Hàm thực hiện khởi tạo driver (được gọi bởi BaseTest)
    public static AppiumDriver createMobileDriver(String deviceName, String appiumUrl) {
        try {
            // 1. Đọc file JSON từ resources
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new FileInputStream("src/test/resources/devices.json");
            JsonNode devices = mapper.readTree(is);
            JsonNode deviceConfig = devices.get(deviceName);

            if (deviceConfig == null) {
                throw new RuntimeException("Không tìm thấy cấu hình cho máy: " + deviceName);
            }

            String platform = deviceConfig.get("platform").asText();
            AppiumDriver driverInstance;

            // 2. Cấu hình dựa trên Platform
            if (platform.equalsIgnoreCase("android")) {
                UiAutomator2Options options = new UiAutomator2Options();
                options.setDeviceName(deviceConfig.get("udid").asText());
                options.setPlatformVersion(deviceConfig.get("version").asText());
                // Accept/Allow permission
                options.setAutoGrantPermissions(true);


                String appPath = deviceConfig.get("appPath").asText();
                if (!appPath.isEmpty()) {
                    options.setApp(appPath);
                } else {
                    options.setAppPackage(deviceConfig.get("appPackage").asText());
                    options.setAppActivity(deviceConfig.get("appActivity").asText());
                }
                driverInstance = new AndroidDriver(new URL(appiumUrl), options);

            } else {
                XCUITestOptions options = new XCUITestOptions();
                options.setDeviceName(deviceConfig.get("udid").asText());
                options.setBundleId(deviceConfig.get("bundleId").asText());
                // Accept/Allow permission
                options.setAutoAcceptAlerts(true);
                driverInstance = new IOSDriver(new URL(appiumUrl), options);
            }

            threadDriver.set(driverInstance);
            return getDriver();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khởi tạo driver cho " + deviceName + ": " + e.getMessage());
        }
    }

    public static void quitMobileDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            threadDriver.remove();
        }
    }
}