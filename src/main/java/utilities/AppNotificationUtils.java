package utilities;

import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Allure;

public class AppNotificationUtils {
    // ==========================================
    // 1. MỞ THANH THÔNG BÁO (NOTIFICATION SHADE)
    // ==========================================
    public static void openNotificationShade(WebDriver driver) {
        if (driver instanceof AndroidDriver) {
            Allure.step("Mở thanh thông báo trên Android...");
            ((AndroidDriver) driver).openNotifications();
            
        } else if (driver instanceof IOSDriver) {
            Allure.step("Vuốt mở Notification Center trên iOS...");
            // Dùng W3C Actions để vuốt từ mép trên cùng (Top Edge) xuống giữa màn hình
            int screenWidth = driver.manage().window().getSize().getWidth();
            int screenHeight = driver.manage().window().getSize().getHeight();
            
            int startX = screenWidth / 2;
            int startY = 5; // Mép trên cùng
            int endY = screenHeight / 2; // Kéo xuống giữa màn hình

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            ((IOSDriver) driver).perform(Collections.singletonList(swipe));
        }
    }

    // ==========================================
    // 2. LẤY TEXT, VERIFY VÀ CLICK VÀO THÔNG BÁO
    // ==========================================
    public static void clickNotificationByTitle(WebDriver driver, String titleToFind) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By notiLocator = null;

        if (driver instanceof AndroidDriver) {
            // Android: Tìm element chứa text của title
            notiLocator = AppiumBy.androidUIAutomator("new UiSelector().textContains(\"" + titleToFind + "\")");
        } else if (driver instanceof IOSDriver) {
            // iOS: Tìm Cell có chứa label là title
            notiLocator = AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeCell' AND label CONTAINS '" + titleToFind + "'");
        }

        // Đợi thông báo xuất hiện trên màn hình
        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(notiLocator));
        
        // Bạn có thể lấy text ra để ghi log hoặc Assert bổ sung nếu cần
        Allure.step("Đã tìm thấy thông báo: " + notification.getText());
        
        // Click vào thông báo để mở App
        notification.click();
        Allure.step("Đã click vào thông báo để mở app.");
    }

}
