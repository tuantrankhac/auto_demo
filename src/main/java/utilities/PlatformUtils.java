package utilities;


import org.openqa.selenium.WebDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class PlatformUtils {
    public static boolean isAndroid(WebDriver driver) {
		return driver instanceof AndroidDriver;
	}

	public static boolean isIOS(WebDriver driver) {
		return driver instanceof IOSDriver;
	}    
}
