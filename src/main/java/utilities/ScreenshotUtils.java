package utilities;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotUtils {
    private static final String SCREENSHOT_DIR = "target/screenshots/";

    /**
     * Chụp screenshot và attach vào Allure
     */
    public static void captureAndAttach(WebDriver driver, String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(name,
                    new ByteArrayInputStream(screenshot));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Chụp screenshot và lưu ra file (dùng khi debug local)
     */
    // public static String captureAndSave(WebDriver driver, String fileName) {
    //     try {
    //         byte[] screenshot = ((TakesScreenshot) driver)
    //                 .getScreenshotAs(OutputType.BYTES);

    //         Path path = Paths.get(SCREENSHOT_DIR + fileName + ".png");
    //         Files.createDirectories(path.getParent());
    //         Files.write(path, screenshot);

    //         return path.toString();

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }
}
