package utilities;


import org.openqa.selenium.WebDriver;
import io.appium.java_client.InteractsWithApps;
import io.qameta.allure.Allure;

import java.time.Duration;



public class AppStateUtils {
    public static void putAppInBackground(WebDriver driver) {
		if (driver instanceof InteractsWithApps) {
			Allure.step("Đưa app xuống Background (chờ lệnh Resume)...");
			// Truyền Duration âm (-1) để app nằm yên dưới background
			((InteractsWithApps) driver).runAppInBackground(Duration.ofSeconds(-1));
		} else {
			System.out.println("Cảnh báo: Driver không hỗ trợ Appium InteractsWithApps.");
		}
	}

	public static void activateAppToForeground(WebDriver driver, String appId) {
        if (driver instanceof InteractsWithApps) {
			// appId chính là Package Name và Bundl ID
            Allure.step("Đang Resume app [" + appId + "] lên lại Foreground...");
            ((InteractsWithApps) driver).activateApp(appId);
        }
    }

	public static void putBackgroundAndAutoResume(WebDriver driver, int seconds){
		if (driver instanceof InteractsWithApps) {
            Allure.step("Đưa app xuống Background " + seconds + "s rồi tự động kéo lên...");
            ((InteractsWithApps) driver).runAppInBackground(Duration.ofSeconds(seconds));
        }
	}

}
