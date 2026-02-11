// package commons;

// import io.appium.java_client.android.AndroidDriver;
// import io.appium.java_client.android.options.UiAutomator2Options;
// import io.appium.java_client.ios.IOSDriver;
// import io.appium.java_client.ios.options.XCUITestOptions;
// import io.appium.java_client.AppiumDriver;
// import java.net.URL;

// public class MobileDriverFactory {


//     public class MobileDriverFactory {

//     public WebDriver createAndroidDriver(String appPath) {
//         try {
//             // Các câu lệnh phải nằm TRONG Method
//             UiAutomator2Options options = new UiAutomator2Options();
//             options.setPlatformName("Android");
//             options.setDeviceName("emulator-5554");
//             options.setPlatformVersion("14");
//             options.setApp(appPath);
//             options.setAutomationName("UiAutomator2");
//             options.setNoReset(true);
//             options.setFullReset(false);
//             options.setNewCommandTimeout(Duration.ofSeconds(300));

//             // Trả về instance của AndroidDriver
//             return new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
            
//         } catch (MalformedURLException e) {
//             throw new RuntimeException("Appium Server URL không hợp lệ!", e);
//         }
//     }

//     public WebDriver createIOSDriver(String appPath) {
//         try {
//             XCUITestOptions iosOptions = new XCUITestOptions();
//             iosOptions.setPlatformName("iOS");
//             iosOptions.setDeviceName("iPhone 15");
//             iosOptions.setPlatformVersion("17.0");
//             iosOptions.setApp(appPath);
//             iosOptions.setAutomationName("XCUITest");
//             iosOptions.setUsePrebuiltWDA(true);

//             return new IOSDriver(new URL("http://127.0.0.1:4723"), iosOptions);
            
//         } catch (MalformedURLException e) {
//             throw new RuntimeException("Appium Server URL không hợp lệ!", e);
//         }
//     }
// }
// }
