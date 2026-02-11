package commons;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import constant.GlobalConstants;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v128.network.Network;
import org.openqa.selenium.devtools.v128.network.model.Headers;
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
import io.qameta.allure.Allure;
import utilities.ConfigReader;
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

	public void injectAuthHeader(WebDriver driver, String authToken) {
		if (!(driver instanceof ChromeDriver)) {
			Allure.step("CDP inject header chỉ hỗ trợ ChromeDriver/EdgeDriver, bỏ qua");
			return;
		}
		Allure.step("Inject Authorization header bằng CDP", () -> {
			Allure.parameter("Authorization Token",
					authToken != null ? authToken.substring(0, Math.min(20, authToken.length())) + "..." : "null");
		});
		try {
			ChromeDriver chromeDriver = (ChromeDriver) driver;
			DevTools devTools = chromeDriver.getDevTools();
			devTools.createSession();
			// Enable Network domain
			devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
			// Set extra HTTP headers
			Map<String, Object> headers = new HashMap<>();
			headers.put("Authorization", authToken);
			devTools.send(Network.setExtraHTTPHeaders((Headers) headers));
			Allure.step("Đã inject thành công Authorization header");
		} catch (Exception e) {
			Allure.step("Lỗi khi inject Authorization header qua CDP", step -> {
				step.parameter("Lỗi chi tiết", e.getMessage());
			});
			System.err.println("CDP inject header thất bại: " + e.getMessage());
		}
	}

	private String getAuthToken() {
		String token = System.getenv("AUTH_TOKEN");
		if (token == null || token.trim().isEmpty()) {
			token = ConfigReader.getProperty("auth.token");
		}

		if (token == null || token.trim().isEmpty()) {
			throw new RuntimeException("Không tìm thấy AUTH_TOKEN trong env hoặc config. " +
					"Vui lòng set biến môi trường AUTH_TOKEN hoặc thêm vào config.properties");
		}
		// Dùng biến riêng để log (không thay đổi token gốc)
		final String logToken = token.substring(0, Math.min(20, token.length())) + "...";

		Allure.step("Đã lấy AUTH_TOKEN thành công", () -> {
			Allure.parameter("Token (cắt ngắn)", logToken);
		});
		return token;
	}

	public String buildUrlWithBasicAuth(String baseUrl, String username, String password) {
		Allure.step("Build URL với Basic Auth credential");
		if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			Allure.step("Không có credential → dùng URL gốc");
			return baseUrl;
		}
		// Encode username:password thành Base64
		String credentials = username + ":" + password;
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
		// Build URL: https://username:password@domain.com/path
		String authUrl;
		try {
			URI uri = new URI(baseUrl);
			String userInfo = username + ":" + password;
			URI authUri = new URI(uri.getScheme(), userInfo, uri.getHost(), uri.getPort(), uri.getPath(),
					uri.getQuery(), uri.getFragment());
			authUrl = authUri.toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Không thể build URL với credential", e);
		}
		Allure.parameter("URL sau khi inject credential", authUrl.replace(password, "*****")); // ẩn pass trong report
		return authUrl;
	}

	protected WebDriver getBrowserDriverWithInjectHeader(String browserName, String appUrl) {
		WebDriver driverInstance; // Khởi tạo biến tạm
		BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());
		if (browser == BROWSER.CHROME) {
			// WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("--incognito");
			options.addArguments("--use-fake-ui-for-media-stream");
			// Open với profile có sẵn
			// options.addArguments("user-data-dir=path/to/profile");
			driverInstance = new ChromeDriver(options);

			// Inject header
			String authToken = getAuthToken();
			injectAuthHeader(driverInstance, authToken);

		} else if (browser == BROWSER.EDGE_CHROMIUM) {
			// WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("-inprivate");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new EdgeDriver(options);
			// Inject header
			String authToken = getAuthToken();
			injectAuthHeader(driverInstance, authToken);
		} else {
			throw new RuntimeException("Browser không hỗ trợ: " + browserName);
		}

		// Đẩy vào ThreadLocal để cô lập cho luồng hiện tại
		threadDriver.set(driverInstance);

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		getDriver().manage().window().maximize();
		getDriver().get(appUrl);
		drivers.add(getDriver());
		return getDriver();
	}

	protected WebDriver getBrowserDriver(String browserName) {
		WebDriver driverInstance; // Khởi tạo biến tạm
		BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());
		if (browser == BROWSER.CHROME) {
			ChromeOptions options = new ChromeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("--incognito");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new ChromeDriver(options);
		} else if (browser == BROWSER.FIREFOX) {
			FirefoxOptions options = new FirefoxOptions();
			// options.addArguments("-headless");
			options.addArguments("-private");

			// Sử dụng Preferences thay vì Arguments cho Firefox
			options.addPreference("media.navigator.permission.disabled", true);
			options.addPreference("media.navigator.streams.fake", true);
			driverInstance = new FirefoxDriver(options);
		} else if (browser == BROWSER.EDGE_CHROMIUM) {
			// WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("-inprivate");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new EdgeDriver(options);
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
			// WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("--incognito");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new ChromeDriver(options);
		} else if (browser == BROWSER.FIREFOX) {
			// WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			// options.addArguments("-headless");
			options.addArguments("-private");

			// Sử dụng Preferences thay vì Arguments cho Firefox
			options.addPreference("media.navigator.permission.disabled", true);
			options.addPreference("media.navigator.streams.fake", true);
			driverInstance = new FirefoxDriver(options);
		} else if (browser == BROWSER.EDGE_CHROMIUM) {
			// WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("-inprivate");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new EdgeDriver(options);
		} else if (browser == BROWSER.SAFARI) {
			// Safari hỗ trợ private nhưng SafariDriver không trực tiếp expose option này.
			// Có thể mở thủ công nếu muốn hoặc ghi chú lại.
			// Safari hiện tại chưa hỗ trợ headless mode chính thức.
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

	protected WebDriver getBrowserDriverWithCredentials(String browserName, String appUrl) {
		// Lấy username/password từ config/env (tương tự getAuthToken)
		String username = ConfigReader.getProperty("auth.username", System.getenv("AUTH_USERNAME"));
		String password = ConfigReader.getProperty("auth.password", System.getenv("AUTH_PASSWORD"));

		// Build URL với credential nếu có
		String finalUrl = buildUrlWithBasicAuth(appUrl, username, password);

		WebDriver driverInstance; // Khởi tạo biến tạm
		BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());
		if (browser == BROWSER.CHROME) {
			// WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("--incognito");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new ChromeDriver(options);
		} else if (browser == BROWSER.FIREFOX) {
			// WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			// options.addArguments("-headless");
			options.addArguments("-private");

			// Sử dụng Preferences thay vì Arguments cho Firefox
			options.addPreference("media.navigator.permission.disabled", true);
			options.addPreference("media.navigator.streams.fake", true);
			driverInstance = new FirefoxDriver(options);
		} else if (browser == BROWSER.EDGE_CHROMIUM) {
			// WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("-inprivate");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new EdgeDriver(options);
		} else if (browser == BROWSER.SAFARI) {
			// Safari hỗ trợ private nhưng SafariDriver không trực tiếp expose option này.
			// Có thể mở thủ công nếu muốn hoặc ghi chú lại.
			// Safari hiện tại chưa hỗ trợ headless mode chính thức.
			driverInstance = new SafariDriver();
		} else {
			throw new RuntimeException("Please enter correct browser name!");
		}

		// Đẩy vào ThreadLocal để cô lập cho luồng hiện tại
		threadDriver.set(driverInstance);

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		getDriver().manage().window().maximize();
		getDriver().get(finalUrl);
		drivers.add(getDriver());
		return getDriver();
	}

	protected void getMultiBrowserDriver(String browserName, String appUrl) {
		WebDriver driverInstance;
		BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());
		if (browser == BROWSER.CHROME) {
			// WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--incognito");
			options.addArguments("--use-fake-ui-for-media-stream");
			driverInstance = new ChromeDriver(options);
		} else if (browser == BROWSER.FIREFOX) {
			// WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("-private");
			// Sử dụng Preferences thay vì Arguments cho Firefox
			options.addPreference("media.navigator.permission.disabled", true);
			options.addPreference("media.navigator.streams.fake", true);
			driverInstance = new FirefoxDriver(options);
		} else if (browser == BROWSER.EDGE_CHROMIUM) {
			// WebDriverManager.edgedriver().setup();
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

	/**
	 * Switch to window/tab by its title.
	 */
	public void switchWindowByTitle(WebDriver driver, String tabTitle) {
		Allure.step("Chuyển sang cửa sổ có tiêu đề: " + tabTitle);
		Set<String> allWindowIDs = driver.getWindowHandles();

		for (String id : allWindowIDs) {
			driver.switchTo().window(id);
			String actualTitle = driver.getTitle();
			if (actualTitle.equals(tabTitle)) {
				return;
			}
		}
	}

	/**
	 * Switch to window/tab by its window ID.
	 */
	public void switchWindowById(WebDriver driver, String windowId) {
		Allure.step("Chuyển sang cửa sổ có ID: " + windowId);
		Set<String> allWindowIDs = driver.getWindowHandles();
		for (String id : allWindowIDs) {
			if (id.equals(windowId)) {
				driver.switchTo().window(id);
				return;
			}
		}
		Allure.step("Không tìm thấy window với ID: " + windowId);
	}

	/**
	 * Switch to window/tab by part (or all) of its URL.
	 */
	public void switchWindowByUrl(WebDriver driver, String urlMatch) {
		Allure.step("Chuyển sang cửa sổ có URL (hoặc chứa): " + urlMatch);
		Set<String> allWindowIDs = driver.getWindowHandles();

		for (String id : allWindowIDs) {
			driver.switchTo().window(id);
			String currentUrl = driver.getCurrentUrl();
			if (currentUrl != null && currentUrl.contains(urlMatch)) {
				return;
			}
		}
		Allure.step("Không tìm thấy window với URL chứa: " + urlMatch);
	}

	/**
	 * Switch to window/tab by index.
	 * 
	 * @param driver The WebDriver instance.
	 * @param index  The index of the window (starting from 0).
	 */
	public void switchWindowByIndex(WebDriver driver, int index) {
		Allure.step("Chuyển sang cửa sổ theo index: " + index);
		List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
		if (index >= 0 && index < windowHandles.size()) {
			driver.switchTo().window(windowHandles.get(index));
		} else {
			Allure.step("Index cửa sổ không hợp lệ: " + index);
		}
	}

	public void closeCurrentWindowAndSwitchToParent(WebDriver driver, String parentHandle) {
		Allure.step("Đóng tab/window hiện tại và chuyển về tab/window ban đầu");
		// Đóng tab hiện tại
		driver.close();

		// Sau khi tab hiện tại đóng, tập mới sẽ còn lại các tab chưa đóng
		Set<String> updatedWindowHandles = driver.getWindowHandles();

		// Nếu parentHandle vẫn tồn tại sau khi đóng tab hiện tại, thì switch về nó
		if (parentHandle != null && updatedWindowHandles.contains(parentHandle)) {
			driver.switchTo().window(parentHandle);
			Allure.step("Đã chuyển về tab ban đầu có handle: " + parentHandle);
		} else if (updatedWindowHandles.size() == 1) { // fallback nếu chỉ còn 1 tab
			String handle = updatedWindowHandles.iterator().next();
			driver.switchTo().window(handle);
			Allure.step("Chỉ còn 1 tab, chuyển về handle: " + handle);
		} else {
			Allure.step("Không tìm thấy hoặc không thể chuyển về tab ban đầu.");
		}
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