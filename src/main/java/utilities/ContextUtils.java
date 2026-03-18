package utilities;

import java.util.Set;

import org.openqa.selenium.WebDriver;

import io.appium.java_client.remote.SupportsContextSwitching;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class ContextUtils {
    // ==========================================
    // 1. CHUYỂN SANG WEBVIEW (Có cơ chế chờ thông minh)
    // ==========================================
    @Step("Chuyển Context sang WebView")
    public static boolean switchToWebView(WebDriver driver) {
        if (!(driver instanceof SupportsContextSwitching)) {
            Allure.step("Driver này không hỗ trợ Switch Context!");
            return false;
        }

        SupportsContextSwitching contextDriver = (SupportsContextSwitching) driver;
        
        // Vòng lặp chờ WebView xuất hiện (Tối đa 10 giây)
        // Vì nhiều khi Appium cần vài giây để detect được WebView sau khi màn hình load
        for (int i = 0; i < 10; i++) {
            Set<String> contexts = contextDriver.getContextHandles();
            for (String context : contexts) {
                // iOS thường có dạng: WEBVIEW_1234. Android: WEBVIEW_com.oxii.crm
                if (context.contains("WEBVIEW") || context.contains("chromium")) {
                    contextDriver.context(context);
                    Allure.step("Đã switch thành công sang: " + context);
                    return true;
                }
            }
            // Nếu chưa thấy, chờ 1 giây rồi quét lại list Context
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
        
        Allure.step("Lỗi: Không tìm thấy bất kỳ WebView Context nào sau 10 giây!");
        return false;
    }

    // ==========================================
    // 2. TRỞ VỀ NATIVE APP
    // ==========================================
    @Step("Chuyển Context về Native App")
    public static void switchToNative(WebDriver driver) {
        if (driver instanceof SupportsContextSwitching) {
            ((SupportsContextSwitching) driver).context("NATIVE_APP");
            Allure.step("Đã switch về NATIVE_APP.");
        }
    }

}
