# Framework Overview

## Giới thiệu

**Demo Framework** (`com.demo:Demo:1.0.0`) là framework kiểm thử tự động đa kênh, xây dựng trên nền **Selenium 4 + Appium + Rest Assured + TestNG + Allure**.

Mục tiêu:

- Tái sử dụng code cao qua **Page Object Model** và **Base class**.
- Hỗ trợ **Web**, **Mobile**, **API** trong cùng một project.
- Báo cáo chi tiết với **Allure** (step, screenshot, video, API log).
- Sẵn sàng mở rộng: parallel, retry, DB validation, data-driven.

## Stack công nghệ

| Layer | Công nghệ | Phiên bản |
|-------|-----------|-----------|
| Language | Java | 17 |
| Build | Maven | 3.x |
| Test Runner | TestNG | 7.9.0 |
| Web | Selenium | 4.24.0 |
| Mobile | Appium Java Client | 9.2.3 |
| API | Rest Assured | 5.4.0 |
| Report | Allure | 2.32.0 |
| Report (phụ) | ExtentReports | 5.1.1 |
| Driver Manager | WebDriverManager | 5.9.2 |
| Config | Owner + Properties | 1.0.12 |
| JSON | Jackson | 2.17.x |
| Excel | Apache POI | 5.3.0 |
| DB | PostgreSQL JDBC | 42.7.4 |
| Fake Data | JavaFaker | 1.0.2 |
| Logging | SLF4J + Log4j | 2.0.13 / 1.2.17 |

## Core Components

### BaseTest (`src/test/java/commons/BaseTest.java`)

Lớp cha của mọi test script.

| Chức năng | Mô tả |
|-----------|-------|
| Kế thừa `BrowserFactory` | Truy cập `getBrowserDriver()`, `getDriver()`, `closeAllBrowsers()` |
| Mobile helper | `getMobileDriver(deviceName, appiumUrl)`, `closeAllMobiles()` |
| `ApiFactory` | Truy cập API client: `apiFactory.getLoginRest()` |
| Verify helpers | `verifyTrue`, `verifyFalse`, `verifyEquals`, `verifyEqualsContains` |
| Data helpers | `generateEmail()`, `generateNumber()`, `deleteAllFileInFolder()` |
| Logging | Apache Commons Logging |

### BrowserFactory (`src/main/java/commons/BrowserFactory.java`)

Quản lý vòng đời WebDriver.

| Method | Mô tả |
|--------|-------|
| `getBrowserDriver(browserName)` | Khởi tạo driver, maximize, implicit wait 30s |
| `getBrowserDriver(browserName, appUrl)` | Khởi tạo + navigate URL |
| `getBrowserDriverWithInjectHeader(browser, url)` | Inject Bearer token qua CDP (Chrome/Edge) |
| `getBrowserDriverWithCredentials(browser, url)` | Basic Auth qua URL |
| `getMultiBrowserDriver(browser, url)` | Mở nhiều browser, lưu window handle |
| `closeAllBrowsers()` | Quit tất cả driver trong list |
| `configDownloadBehaviorViaCDP(chromeDriver)` | Cấu hình download folder + bắt tên file |
| `injectAuthHeader(driver, token)` | CDP `Network.setExtraHTTPHeaders` |

**Browser hỗ trợ:** Chrome, Firefox, Edge Chromium, Safari.

**Thread safety:** `ThreadLocal<WebDriver>` — mỗi thread một driver instance.

### MobileFactory (`src/main/java/commons/MobileFactory.java`)

| Method | Mô tả |
|--------|-------|
| `createMobileDriver(deviceName, appiumUrl)` | Đọc `devices.json`, khởi tạo Android/iOS driver |
| `getDriver()` | Lấy AppiumDriver hiện tại (ThreadLocal) |
| `quitMobileDriver()` | Quit và remove ThreadLocal |

**Android:** UiAutomator2Options — `appPackage`/`appActivity` hoặc `appPath`.

**iOS:** XCUITestOptions — `bundleId`.

### BasePage (`src/main/java/commons/BasePage.java`)

Thư viện hành vi UI dùng chung (~1500 dòng).

**Nhóm chức năng chính:**

| Nhóm | Method tiêu biểu |
|------|------------------|
| Navigation | `openPageUrl`, `backToPageByNavigate`, `refreshPageByJS` |
| Element interaction | `clickToElement`, `sendkeyToElement`, `getElementText` |
| Dropdown | `selectDropdownByText`, `selectItemInCustomDropdown` |
| Wait | `waitForElementVisible`, `waitForElementClickable`, `fluentWaitForElementVisible` |
| Alert | `acceptAlert`, `cancelAlert`, `getAlertText` |
| Frame / Window | `switchToFrameByWebElement`, `switchWindowByTitle`, `switchWindowByUrl` |
| Actions | `hoverMouseToElement`, `dragAndDropElementByAction`, `dragAndDropJS` |
| JavaScript | `clickToElementByJS`, `scrollToElementOnTopByJS`, `setAttributeInDOM` |
| Shadow DOM | `getShadowRoot`, `getElementInNestedShadowRoot` |
| Upload / Download | `uploadFile`, `waitForFileDownloadSuccess`, `waitAndGetDownloadFileName` |
| Cookie / CDP | `getAllCookiesByCDP`, `getCookieByJS` |
| Mobile | `smartScrollToElement`, `swipeBetweenTwoPoints`, `swipeUpW3C` |

Mọi thao tác quan trọng bọc `Allure.step()` để ghi report.

### BaseApi (`src/main/java/commons/BaseApi.java`)

| Method | Mô tả |
|--------|-------|
| `get(endpoint)` | GET request |
| `post(endpoint, body)` | POST request |
| `put(endpoint, body)` | PUT request |
| `delete(endpoint)` | DELETE request |
| `setAuthToken(token)` | Thêm header `Authorization: Bearer ...` |
| `setAdditionalHeaders(map)` | Thêm custom headers (GraphQL Origin/Referer) |
| `verifyStatusCode(response, code)` | Assert status code |
| `verifyJsonPath(response, path, value)` | Assert JSON path |
| `extractValueFromResponse(response, jsonPath)` | Trích xuất giá trị + attach Allure |

Tích hợp **AllureRestAssured** filter — tự log request/response vào report.

### PageGenerator (`src/test/java/commons/PageGenerator.java`)

Factory tập trung khởi tạo Page Object Web và Mobile. Test script không `new` trực tiếp Page Object.

### ApiFactory (`src/test/java/commons/ApiFactory.java`)

Lazy-init API clients:

- `getLoginGraphQL()` → `LoginApiWithGraphQL`
- `getLoginRest()` → `LoginApiWithRest`
- `getCreateTicketV2()` → `CreateTicketV2`

## Utilities

| Class | Chức năng |
|-------|-----------|
| `ConfigReader` | Đọc `config.properties` |
| `GlobalConstants` | URL, timeout, đường dẫn thư mục, locator loading |
| `ScreenshotUtils` | Chụp màn hình, attach Allure |
| `VideoRecorderUtils` | Quay video Appium, attach khi fail |
| `ExcelUtils` | Đọc/ghi Excel (data-driven) |
| `DataUtils` | Xử lý dữ liệu test |
| `DbConnection` | Kết nối PostgreSQL, query linh hoạt |
| `StringUtils` | Xử lý chuỗi |
| `CmdUtils` | Thực thi lệnh OS |
| `AppStateUtils` | Quản lý trạng thái app mobile |
| `AppNotificationUtils` | Xử lý notification mobile |
| `VerificationFailures` | Thu thập lỗi verify cho TestNG Reporter |

## Retry Mechanism

| Class | Vai trò |
|-------|---------|
| `RetryTest` | `IRetryAnalyzer` — retry tối đa **2 lần** |
| `RetryTransformer` | `IAnnotationTransformer` — gắn retry cho mọi `@Test` trong suite |

Kích hoạt bằng listener trong `runTestCase.xml`:

```xml
<listener class-name="commons.retry.RetryTransformer"/>
```

## Reporting

### Allure (chính)

- Adapter: `allure-testng`, `allure-rest-assured`.
- Kết quả thô: `target/allure-results/`.
- Report HTML: `report/allure-report-{HH-mm_yyyy-MM-dd}/` (timezone `Asia/Ho_Chi_Minh`).
- Sinh tự động ở Maven phase `verify`.
- `allure-results` được xóa ở phase `initialize` lần chạy tiếp theo (tránh trùng dữ liệu cũ).

### TestListener

| Event | Hành vi |
|-------|---------|
| `onTestStart` | Bắt đầu quay video (Mobile) |
| `onTestFailure` | Screenshot (Web + Mobile), lưu video (Mobile) |
| `onTestSuccess` / `onTestSkipped` | Dừng quay, không lưu video (tiết kiệm RAM) |

### ExtentReports

- `ExtentManager.getInstance()` — report HTML tại `extentReport/Report.html`.
- Theme dark, tiêu đề "Web-SOffice HTML Report".

## Maven Build Pipeline

```
initialize  → xóa allure-results cũ, sinh build.time timestamp
compile     → biên dịch source
test        → Surefire chạy TestNG (runTestCase.xml)
verify      → Allure Maven Plugin generate report
```

**Surefire config:**

- Suite: `src/test/resources/runTestCase.xml`
- `testFailureIgnore=true` — build không fail khi test fail (vẫn sinh report)
- `forkCount=0` — tránh crash JVM agent trên Windows
- `-Xmx2048m` — heap 2GB

## Cấu hình môi trường test

| File | Nội dung |
|------|----------|
| `config.properties` | Auth token, credentials, timeout |
| `devices.json` | Cấu hình thiết bị mobile |
| `runTestCase.xml` | Danh sách test, parameter browser/url/device |
| `GlobalConstants.java` | URL staging CRM, API base URL |

**URL mặc định:**

- Frontend: `https://stg-crm.smarthiz.vn`
- API: `https://stg-crm-api.smarthiz.com`
- GraphQL path: `/graphql`

## Mở rộng framework

### Thêm test Web mới

1. Tạo `PageUI` + `PageObject`.
2. Đăng ký `PageGenerator`.
3. Tạo test script kế thừa `BaseTest`.
4. Thêm `<test>` block vào `runTestCase.xml`.

### Thêm API client mới

1. Tạo class extends `BaseApi` trong `demo.api`.
2. Thêm getter vào `ApiFactory`.
3. Gọi từ test qua `apiFactory`.

### Thêm thiết bị mobile

1. Thêm entry vào `devices.json`.
2. Truyền key làm `deviceName` parameter trong suite XML.

### Chạy parallel

Uncomment `parallel="tests"` trong suite XML và đảm bảo ThreadLocal driver hoạt động đúng.

## Lệnh thường dùng

```bash
# Compile
mvn clean compile

# Chạy test
mvn clean test

# Chạy test + Allure report
mvn clean verify

# Chạy với profile
mvn test -Pweb
mvn test -Pmobile

# Mở Allure report
allure open report/allure-report-<timestamp>
```

## Giới hạn & lưu ý

- CDP inject header chỉ hỗ trợ **ChromeDriver / EdgeDriver**.
- `MobileFactory` đọc `devices.json` bằng đường dẫn relative `src/test/resources/devices.json` — chạy từ project root.
- Safari không hỗ trợ headless chính thức.
- Secret trong `config.properties` hiện tại là demo — **không dùng cho production**.
- MySQL connector đã comment trong `pom.xml`; project mặc định dùng PostgreSQL.
