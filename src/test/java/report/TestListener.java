package report; // Đổi lại package theo cấu trúc thực tế của bạn

import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utilities.ScreenshotUtils;
import utilities.VideoRecorderUtils;
import commons.BaseTest;
import io.qameta.allure.Allure;

public class TestListener implements ITestListener {

    // ==========================================
    // TỰ ĐỘNG BẬT QUAY VIDEO TRƯỚC KHI TEST BẮT ĐẦU
    // ==========================================
    @Override
    public void onTestStart(ITestResult result) {
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            try {
                WebDriver mobileDriver = ((BaseTest) testClass).getMobileDriver();
                if (mobileDriver != null) {
                    Allure.step("Bắt đầu chạy Test: " + result.getName() + " -> Tự động bật máy quay App.");
                    VideoRecorderUtils.startRecording(mobileDriver);
                }
            } catch (Exception e) {
                // Bỏ qua nếu là Web test hoặc chưa khởi tạo driver
            }
        }
    }

    // ==========================================
    // TỰ ĐỘNG CHỤP ẢNH & LƯU VIDEO KHI TEST FAIL
    // ==========================================
    @Override
    public void onTestFailure(ITestResult result) {
        Allure.step("Test Case FAILED: " + result.getName() + " -> Đang xử lý đính kèm Allure...");
        Object testClass = result.getInstance();

        if (testClass instanceof BaseTest) {
            BaseTest baseTest = (BaseTest) testClass;
            
            // Xử lý Mobile (Chụp ảnh + Dừng và Lưu Video)
            try {
                WebDriver mobileDriver = baseTest.getMobileDriver();
                if (mobileDriver != null) {
                    ScreenshotUtils.captureScreenshot(mobileDriver);
                    VideoRecorderUtils.stopAndAttachVideo(mobileDriver);
                }
            } catch (Exception e) {}

            // Xử lý Web (Chỉ chụp ảnh)
            try {
                WebDriver webDriver = baseTest.getDriver(); 
                if (webDriver != null) {
                    ScreenshotUtils.captureScreenshot(webDriver);
                }
            } catch (Exception e) {}
        }
    }

    // ==========================================
    // TỰ ĐỘNG DỪNG QUAY VÀ HUỶ VIDEO KHI TEST PASS/SKIP (CHỐNG TRÀN RAM)
    // ==========================================
    @Override
    public void onTestSuccess(ITestResult result) {
        clearVideoBuffer(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        clearVideoBuffer(result);
    }

    // Hàm dùng chung để dọn dẹp máy quay
    private void clearVideoBuffer(ITestResult result) {
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            try {
                WebDriver mobileDriver = ((BaseTest) testClass).getMobileDriver();
                if (mobileDriver != null) {
                    VideoRecorderUtils.stopRecordingWithoutSaving(mobileDriver);
                }
            } catch (Exception e) {}
        }
    }
}