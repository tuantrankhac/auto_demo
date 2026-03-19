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

import constant.GlobalConstants;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v128.page.Page;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Allure;
import utilities.ConfigReader;

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

			Map<String, Object> headers = new HashMap<>();
			headers.put("Authorization", authToken);

			Map<String, Object> params = new HashMap<>();
			params.put("headers", headers);

			chromeDriver.executeCdpCommand("Network.enable", new HashMap<>());
			chromeDriver.executeCdpCommand("Network.setExtraHTTPHeaders", params);

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
			ChromeOptions options = new ChromeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("--incognito");
			options.addArguments("--use-fake-ui-for-media-stream");

			// Tắt notification/popup hệ thống
			// options.addArguments("--disable-notifications");
			// options.addArguments("--disable-popup-blocking");
			// Có thể truyền thêm các preferences nếu cần
			// options.setExperimentalOption("prefs", new java.util.HashMap<String,
			// Object>() {{
			// put("profile.default_content_setting_values.notifications", 2);
			// put("profile.default_content_setting_values.geolocation", 2);
			// put("profile.default_content_setting_values.media_stream_camera", 2);
			// put("profile.default_content_setting_values.media_stream_mic", 2);
			// put("profile.default_content_setting_values.automatic_downloads", 1);
			// put("profile.default_content_settings.popups", 0);
			// }});

			// Thiết lập kích thước cửa sổ trình duyệt (ví dụ: 1280x800)
			// options.addArguments("window-size=1280,800");

			// Cấu hình download nếu không dùng CDP
			// Map<String, Object> prefs = new HashMap<>();
			// prefs.put("download.default_directory",
			// GlobalConstants.DOWNLOAD_FILE_FOLDER);
			// prefs.put("download.prompt_for_download", false);
			// options.setExperimentalOption("prefs", prefs);

			// Tạo driver
			driverInstance = new ChromeDriver(options);

			// Cấu hình download
			// configDownloadBehaviorViaCDP((ChromeDriver) driverInstance);

		} else if (browser == BROWSER.FIREFOX) {
			FirefoxOptions options = new FirefoxOptions();
			// options.addArguments("-headless");
			options.addArguments("-private");

			// // Sử dụng Preferences để tắt notification/popup cho Firefox
			// options.addPreference("media.navigator.permission.disabled", true);
			// options.addPreference("media.navigator.streams.fake", true);
			// options.addPreference("dom.webnotifications.enabled", false);
			// options.addPreference("dom.push.enabled", false);
			// options.addPreference("dom.disable_beforeunload", true); // Tắt popup warning
			// trước khi unload page

			// Thiết lập kích thước cửa sổ trình duyệt (ví dụ: 1280x800)
			// options.addPreference("browser.window.width", 1280);
			// options.addPreference("browser.window.height", 800);

			// Cấu hình download nếu không dùng CDP
			// options.addPreference("browser.download.dir",
			// GlobalConstants.DOWNLOAD_FILE_FOLDER);
			// options.addPreference("browser.download.folderList", 2);
			// options.addPreference("browser.helperApps.neverAsk.saveToDisk",
			// "application/pdf,application/octet-stream");
			// options.addPreference("pdfjs.disabled", true);

			driverInstance = new FirefoxDriver(options);
		} else if (browser == BROWSER.EDGE_CHROMIUM) {
			// WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			// options.addArguments("--headless=new");
			options.addArguments("-inprivate");
			options.addArguments("--use-fake-ui-for-media-stream");
			// Tắt notification/popup hệ thống đối với Edge Chromium
			// options.addArguments("--disable-notifications");
			// options.addArguments("--disable-popup-blocking");
			// java.util.HashMap<String, Object> edgePrefs = new java.util.HashMap<String,
			// Object>();
			// edgePrefs.put("profile.default_content_setting_values.notifications", 2);
			// edgePrefs.put("profile.default_content_setting_values.geolocation", 2);
			// edgePrefs.put("profile.default_content_setting_values.media_stream_camera",2);
			// edgePrefs.put("profile.default_content_setting_values.media_stream_mic", 2);
			// edgePrefs.put("profile.default_content_setting_values.automatic_downloads",1);
			// edgePrefs.put("profile.default_content_settings.popups", 0);
			// options.setExperimentalOption("prefs", edgePrefs);

			// Thiết lập kích thước cửa sổ trình duyệt (ví dụ: 1280x800)
			// options.addArguments("window-size=1280,800");

			// Cấu hình download nếu không dùng CDP
			// Map<String, Object> prefs = new HashMap<>();
			// prefs.put("download.default_directory",
			// GlobalConstants.DOWNLOAD_FILE_FOLDER);
			// prefs.put("download.prompt_for_download", false);
			// options.setExperimentalOption("prefs", prefs);

			driverInstance = new EdgeDriver(options);
		} else if (browser == BROWSER.SAFARI) {
			// Safari hỗ trợ private nhưng SafariDriver không trực tiếp expose option này.
			// Có thể mở thủ công nếu muốn hoặc ghi chú lại.
			// Safari hiện tại chưa hỗ trợ headless mode chính thức.
			// SafariDriver không có option disable-notifications chính thức
			driverInstance = new SafariDriver();
		} else {
			throw new RuntimeException("Please enter correct browser name!");
		}

		// Đẩy vào ThreadLocal để cô lập cho luồng hiện tại
		threadDriver.set(driverInstance);

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		getDriver().manage().window().maximize();

		// Set kích thước màn hình 1280 x 800 với setSize
		// getDriver().manage().window().setSize(new Dimension(1280, 800));

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
		Allure.step("Đã mở trình duyệt với ID: " + getDriver().getWindowHandle());
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

	// Khai báo một biến để lưu tên file bắt được từ CDP (Dùng ThreadLocal nếu chạy
	// song song)
	public static ThreadLocal<String> cdpDownloadedFileName = new ThreadLocal<>();

	// Thêm getter static (protected hoặc public)
	protected static String getCdpDownloadedFileName() {
		return cdpDownloadedFileName.get();
	}

	public void configDownloadBehaviorViaCDP(ChromeDriver chromeDriver) {
		DevTools devTools = chromeDriver.getDevTools();
		// Khởi tạo session để dùng DevTools API
		devTools.createSession();

		// Cấu hình download
		String downloadPath = GlobalConstants.DOWNLOAD_FILE_FOLDER;
		File downloadDir = new File(downloadPath);
		if (!downloadDir.exists()) {
			downloadDir.mkdirs();
		}

		// Cấu hình Parameter cho lệnh Page.setDownloadBehavior
		Map<String, Object> params = new HashMap<>();
		params.put("behavior", "allow"); // Cho phép tải xuống
		params.put("downloadPath", downloadPath); // Chỉ định folder đích
		params.put("eventsEnabled", true); // Bật event để theo dõi trạng thái nếu cần

		// Gửi lệnh trực tiếp đến Chrome thông qua CDP
		chromeDriver.executeCdpCommand("Page.setDownloadBehavior", params);

		// Bắt tên file từ trình duyệt
		devTools.addListener(Page.downloadWillBegin(), event -> {
			String fileName = event.getSuggestedFilename();
			cdpDownloadedFileName.set(fileName); // Lưu tên file vào biến để Testcase dùng
			log.info("CDP bắt được tên file sắp tải: " + fileName);
		});
	}
}