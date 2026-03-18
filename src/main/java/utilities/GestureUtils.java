package utilities;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.interactions.Pause;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class GestureUtils {
    private static Point getCenter(WebElement element) {
        Rectangle rect = element.getRect();
        int centerX = rect.getX() + (rect.getWidth() / 2);
        int centerY = rect.getY() + (rect.getHeight() / 2);
        return new Point(centerX, centerY);
    }

    // ==========================================
    // 1. DOUBLE TAP (CHẠM KÉP)
    // ==========================================
    @Step("Double Tap (Chạm kép) vào element")
    public static void doubleTap(WebDriver driver, WebElement element) {
        if (driver instanceof AppiumDriver) {
            Point center = getCenter(element);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence doubleTapSeq = new Sequence(finger, 1);

            doubleTapSeq.addAction(
                    finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y));
            doubleTapSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            doubleTapSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            doubleTapSeq.addAction(new Pause(finger, Duration.ofMillis(120)));
            doubleTapSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            doubleTapSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            ((AppiumDriver) driver).perform(Collections.singletonList(doubleTapSeq));
            Allure.step("Đã Double Tap tại toạ độ: " + center.x + ", " + center.y);
        }
    }

    // ==========================================
    // 2. LONG PRESS (NHẤN GIỮ)
    // ==========================================
    @Step("Nhấn giữ (Long Press 1.5s) vào element")
    public static void longPress(WebDriver driver, WebElement element) {
        if (driver instanceof AppiumDriver) {
            Point center = getCenter(element);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence longPressSeq = new Sequence(finger, 1);

            longPressSeq.addAction(
                    finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y));
            longPressSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            longPressSeq.addAction(new Pause(finger, Duration.ofMillis(1500)));
            longPressSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            ((AppiumDriver) driver).perform(Collections.singletonList(longPressSeq));
            Allure.step("Đã Long Press (1.5s) tại toạ độ: " + center.x + ", " + center.y);
        }
    }

    // ==========================================
    // 3. ZOOM IN (PINCH OUT - Phóng to ảnh/bản đồ)
    // ==========================================
    @Step("Phóng to (Zoom In / Pinch Out) trên element")
    public static void zoomIn(WebDriver driver, WebElement element) {
        if (driver instanceof AppiumDriver) {
            Point center = getCenter(element);
            int centerX = center.getX();
            int centerY = center.getY();

            // Khoảng cách ngón tay (tính từ tâm)
            int startDist = 30; // Bắt đầu gần nhau
            int endDist = 250; // Vuốt xa ra ngoài

            // Thời gian 700ms theo chuẩn của bạn để mượt mà
            Duration duration = Duration.ofMillis(700);

            // Khởi tạo 2 ngón tay
            PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
            PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

            // Ngón 1: Vuốt sang TRÁI (hoặc LÊN trên)
            Sequence seq1 = new Sequence(finger1, 1);
            seq1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX - startDist,centerY));
            seq1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            seq1.addAction(new Pause(finger1, Duration.ofMillis(100))); // Pause nhẹ trước khi vuốt
            seq1.addAction(finger1.createPointerMove(duration, PointerInput.Origin.viewport(), centerX - endDist, centerY));
            seq1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            // Ngón 2: Vuốt sang PHẢI (hoặc XUỐNG dưới)
            Sequence seq2 = new Sequence(finger2, 2);
            seq2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX + startDist, centerY));
            seq2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            seq2.addAction(new Pause(finger2, Duration.ofMillis(100)));
            seq2.addAction(finger2.createPointerMove(duration, PointerInput.Origin.viewport(), centerX + endDist, centerY));
            seq2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            // THỰC THI ĐỒNG THỜI 2 NGÓN TAY
            ((AppiumDriver) driver).perform(Arrays.asList(seq1, seq2));
            Allure.step("Đã thực hiện Zoom In (Pinch Out) mượt mà.");
        }
    }

    // ==========================================
    // 4. ZOOM OUT (PINCH IN - Thu nhỏ ảnh/bản đồ)
    // ==========================================
    @Step("Thu nhỏ (Zoom Out / Pinch In) trên element")
    public static void zoomOut(WebDriver driver, WebElement element) {
        if (driver instanceof AppiumDriver) {
            Point center = getCenter(element);
            int centerX = center.getX();
            int centerY = center.getY();

            // Khoảng cách: Bắt đầu từ xa, kéo gần lại tâm
            int startDist = 250;
            int endDist = 30;

            Duration duration = Duration.ofMillis(700);

            PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
            PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

            // Ngón 1: Từ bên ngoài kéo vào giữa (bên Trái tâm)
            Sequence seq1 = new Sequence(finger1, 1);
            seq1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX - startDist, centerY));
            seq1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            seq1.addAction(new Pause(finger1, Duration.ofMillis(100)));
            seq1.addAction(finger1.createPointerMove(duration, PointerInput.Origin.viewport(), centerX - endDist, centerY));
            seq1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            // Ngón 2: Từ bên ngoài kéo vào giữa (bên Phải tâm)
            Sequence seq2 = new Sequence(finger2, 2);
            seq2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX + startDist,centerY));
            seq2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            seq2.addAction(new Pause(finger2, Duration.ofMillis(100)));
            seq2.addAction(finger2.createPointerMove(duration, PointerInput.Origin.viewport(), centerX + endDist, centerY));
            seq2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            // THỰC THI ĐỒNG THỜI
            ((AppiumDriver) driver).perform(Arrays.asList(seq1, seq2));
            Allure.step("Đã thực hiện Zoom Out (Pinch In) mượt mà.");
        }

    }
}