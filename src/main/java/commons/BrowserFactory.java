package commons;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import constant.GlobalConstants;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.VerificationFailures;

public class BrowserFactory {
	private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
	private List<WebDriver> drivers = new java.util.concurrent.CopyOnWriteArrayList<>();
	protected List<String> windowHandles = new ArrayList<>();
	protected final Log log;

	// Các class Page Object sẽ gọi vào để tương tác
	public WebDriver getDriver() {
		return threadDriver.get();
	}

	protected BrowserFactory() {
		log = LogFactory.getLog(getClass());
	}

	private enum BROWSER {
		CHROME, FIREFOX, IE, SAFARI, EDGE_LEGACY, EDGE_CHROMIUM, H_CHROME, H_FIREFOX;
	}

	protected WebDriver getBrowserDriver(String browserName) {
		WebDriver driverInstance; // Khởi tạo biến tạm
		BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());
		if (browser == BROWSER.CHROME) {
			WebDriverManager.chromedriver().setup();
			driverInstance = new ChromeDriver();
		} else if (browser == BROWSER.FIREFOX) {
			WebDriverManager.firefoxdriver().setup();
			driverInstance = new FirefoxDriver();
		} else if (browser == BROWSER.EDGE_CHROMIUM) {
			WebDriverManager.edgedriver().setup();
			driverInstance = new EdgeDriver();
		} else if (browser == BROWSER.SAFARI) {
			driverInstance = new SafariDriver();
		} else {
			throw new RuntimeException("Please enter correct browser name!");
		}

		// Đẩy vào ThreadLocal để cô lập cho luồng hiện tại
		threadDriver.set(driverInstance);

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		getDriver().manage().window().maximize();

		// Thêm vào danh sách CopyOnWriteArrayList
		drivers.add(getDriver());
		return getDriver();
	}

	protected WebDriver getBrowserDriver(String browserName, String appUrl) {
		WebDriver driverInstance; // Khởi tạo biến tạm
		BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());
		if (browser == BROWSER.CHROME) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--incognito");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new ChromeDriver(options);
		} else if (browser == BROWSER.FIREFOX) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("-private");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new FirefoxDriver(options);
		} else if (browser == BROWSER.EDGE_CHROMIUM) {
			WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			options.addArguments("-inprivate");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new EdgeDriver(options);
		} else if (browser == BROWSER.SAFARI) {
			// Safari hỗ trợ private nhưng SafariDriver không trực tiếp expose option này.
			// Có thể mở thủ công nếu muốn hoặc ghi chú lại.
			driverInstance = new SafariDriver();
		} else {
			throw new RuntimeException("Please enter correct browser name!");
		}

		// Đẩy vào ThreadLocal để cô lập cho luồng hiện tại
		threadDriver.set(driverInstance);

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		getDriver().manage().window().maximize();
		getDriver().get(appUrl);
		drivers.add(getDriver());
		return getDriver();
	}

	protected void getMultiBrowserDriver(String browserName, String appUrl) {
		WebDriver driverInstance;
		BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());
		if (browser == BROWSER.CHROME) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--incognito");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new ChromeDriver(options);
		} else if (browser == BROWSER.FIREFOX) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("-private");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new FirefoxDriver(options);
		} else if (browser == BROWSER.EDGE_CHROMIUM) {
			WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			options.addArguments("-inprivate");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new EdgeDriver(options);
		} else if (browser == BROWSER.SAFARI) {
			// Safari hỗ trợ private nhưng SafariDriver không trực tiếp expose option này.
			// Có thể mở thủ công nếu muốn hoặc ghi chú lại.
			driverInstance = new SafariDriver();
		} else {
			throw new RuntimeException("Please enter correct browser name!");
		}

		// Đẩy vào ThreadLocal để cô lập cho luồng hiện tại
		threadDriver.set(driverInstance);

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		getDriver().manage().window().maximize();
		getDriver().get(appUrl);
		drivers.add(getDriver());

		// Lưu trữ window handle của từng trình duyệt
		windowHandles.add(getDriver().getWindowHandle());
		System.out.println("Đã mở trình duyệt với ID: " + getDriver().getWindowHandle());
	}

	// Hàm trả về danh sách các window handle
	public List<String> getWindowHandles() {
		return windowHandles;
	}

	// Chuyển qua một trình duyệt dựa trên window handle
	public WebDriver switchToWindow(String windowHandle) {
		for (int i = 0; i < windowHandles.size(); i++) {
			if (windowHandles.get(i).equals(windowHandle)) {
				WebDriver driver = drivers.get(i); // Lấy WebDriver tương ứng với window handle
				driver.switchTo().window(windowHandle);
				System.out.println("Đã chuyển sang cửa sổ với ID: " + windowHandle);
				sleepInMiliSecond(1000);
				return driver;
			}
		}
		System.out.println("Không tìm thấy cửa sổ với ID: " + windowHandle);
		return null;
	}

	public void sleepInMiliSecond(long timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected String generateEmail() {
		Random rand = new Random();
		return rand.nextInt(9999) + "@qa.team";
	}

	protected static int generateNumber() {
		Random rand = new Random();
		return rand.nextInt(9999);
	}

	protected boolean verifyTrue(boolean condition) {
		boolean status = true;
		try {
			Assert.assertTrue(condition);
			log.info("-------------- Có hiển thị ---------------");
		} catch (Throwable e) {
			status = false;
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
			log.info("---------------- Không hiển thị --------------------");
		}
		return status;
	}

	protected boolean verifyFileUploadOpenSuccess(boolean condition) {
		boolean status = true;
		try {
			Assert.assertTrue(condition);
			log.info("-------------- Upload file thành công và có thể mở ---------------");
		} catch (Throwable e) {
			status = false;
			log.info("---------------- Upload file không thành công --------------------");
		}
		return status;
	}

	protected boolean verifyImageUploadOpenSuccess(boolean condition) {
		boolean status = true;
		try {
			Assert.assertTrue(condition);
			log.info("-------------- Upload image thành công và có thể mở ---------------");
		} catch (Throwable e) {
			status = false;
			log.info("---------------- Upload image không thành công --------------------");
		}
		return status;
	}

	protected boolean verifyFalse(boolean condition) {
		boolean status = false;
		try {
			Assert.assertFalse(condition);
			log.info("----------------- Element không hiển thị -------------------");
		} catch (Throwable e) {
			status = true;
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
			log.info("------------------ Element có hiển thị ------------------");
		}
		return status;
	}

	protected boolean verifyEquals(Object actual, Object expected) {
		boolean status = true;
		try {
			if (actual == null) {
				return false;
			}
			log.info("Text hiện tại: " + actual + " và Text mong muốn: " + expected);
			Assert.assertEquals(actual, expected);
			log.info("------------ Passed ------------");
		} catch (Throwable e) {
			status = false;
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
			log.info("----------------- Failed -------------------");
			log.info("Text hiện tại: " + actual);
		}
		return status;
	}

	protected boolean verifyTrangThaiTienTrinh(Object actual, Object expected) {
		boolean status = true;
		log.info("Kiểm tra tiến trình có trạng thái: " + expected);
		try {
			Assert.assertEquals(actual, expected);
			log.info("------------ Passed ------------");
		} catch (Throwable e) {
			status = false;
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
			log.info("----------------- Failed -------------------");
			log.info("Trạng thái hiện tại: " + actual);
		}
		return status;
	}

	protected boolean verifyEqualsContains(String actual, String expected) {
		boolean result = actual.contains(expected);
		try {
			Assert.assertTrue(result);
			log.info("------------ Text giống nhau ------------");
		} catch (Throwable e) {
			VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
			Reporter.getCurrentTestResult().setThrowable(e);
			log.info("----------------- Text không giống nhau -------------------");
		}
		return result;
	}

	@BeforeTest
	public void deleteFileInReport() {
		deleteAllFileInFolder("reportNGImage");
		deleteAllFileInFolder("allure-json");
	}

	public void deleteAllFileInFolder(String folderName) {
		try {
			String pathFolderDownload = GlobalConstants.PROJECT_PATH + File.separator + folderName;
			File file = new File(pathFolderDownload);
			File[] listOfFiles = file.listFiles();
			if (listOfFiles.length != 0) {
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals("environment.properties")) {
						new File(listOfFiles[i].toString()).delete();
					}
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	public void closeAllBrowsers() {
		for (WebDriver driverInstans : drivers) {
			if (driverInstans != null) {
				driverInstans.quit();
			}
		}
		drivers.clear();
		windowHandles.clear();
		// Đảm bảo xóa cả thread hiện tại đang gọi hàm này
		threadDriver.remove();
	}

	protected void cleanBrowserAndDriver() {
		String cmd = null;
		try {
			String osName = System.getProperty("os.name").toLowerCase();
			log.info("OS name = " + osName);

			// Lấy driver từ ThreadLocal thay vì biến local
			WebDriver currentDriver = getDriver();
			if (currentDriver == null)
				return;

			String driverInstanceName = currentDriver.toString().toLowerCase();
			log.info("Driver instance name = " + driverInstanceName);

			String browserDriverName = null;

			if (driverInstanceName.contains("chrome")) {
				browserDriverName = "chromedriver";
			} else if (driverInstanceName.contains("internetexplorer")) {
				browserDriverName = "IEDriverServer";
			} else if (driverInstanceName.contains("firefox")) {
				browserDriverName = "geckodriver";
			} else if (driverInstanceName.contains("edge")) {
				browserDriverName = "msedgedriver";
			} else if (driverInstanceName.contains("opera")) {
				browserDriverName = "operadriver";
			} else {
				browserDriverName = "safaridriver";
			}

			if (osName.contains("window")) {
				cmd = "taskkill /F /FI \"IMAGENAME eq " + browserDriverName + "*\"";
			} else {
				cmd = "pkill " + browserDriverName;
			}

			// Đóng trình duyệt của Thread hiện tại
			currentDriver.manage().deleteAllCookies();
			currentDriver.quit();

		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			try {
				Process process = Runtime.getRuntime().exec(cmd);
				process.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}