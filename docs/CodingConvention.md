# Coding Convention

## Ngôn ngữ & phiên bản

- **Java 17** — dùng feature hiện đại (var, text block nếu cần) nhưng ưu tiên code dễ đọc.
- Encoding: **UTF-8** (`project.build.sourceEncoding`).
- Comment/log có thể dùng **tiếng Việt** (theo convention hiện tại của project).

## Cấu trúc package

| Package | Vai trò |
|---------|---------|
| `commons` (main) | Base class, factory, retry — dùng chung production code |
| `commons` (test) | `BaseTest`, `PageGenerator`, `ApiFactory` |
| `constant` | Hằng số toàn cục |
| `utilities` | Helper không phụ thuộc business logic |
| `exception` | Custom exception |
| `demo.web.pageUIs` | Locator Web (chỉ `By`) |
| `demo.web.pageObjects` | Page Object Web (hành vi) |
| `demo.web.testScripts` | Test case Web UI |
| `demo.mobile.pageUIs` | Locator Mobile |
| `demo.mobile.pageObjects` | Page Object Mobile |
| `demo.mobile.testScripts` | Test case Mobile |
| `demo.api` | API client class |
| `report` | Listener, ExtentReports |

## Quy tắc đặt tên

### Class

| Loại | Pattern | Ví dụ |
|------|---------|-------|
| Page UI | `<TenTrang>PageUI` | `DangNhapPageUI` |
| Page Object | `<TenTrang>PO` | `DangNhapPO` |
| Test Script | `<MôTảTest>` hoặc `<Feature>` | `TaoMoiSuVu`, `HandleAlert` |
| API Client | `<Action>Api<Protocol>` | `LoginApiWithRest`, `LoginApiWithGraphQL` |
| Utility | `<ChứcNăng>Utils` | `ScreenshotUtils`, `ExcelUtils` |
| Factory | `<Loại>Factory` | `BrowserFactory`, `ApiFactory` |
| Listener | `<Tên>Listener` | `TestListener` |

### Method

| Loại | Pattern | Ví dụ |
|------|---------|-------|
| Action UI | `clickTo...`, `enterTo...`, `open...` | `clickToLoginButton()` |
| Getter | `get...` | `getElementText()` |
| Verify | `verify...`, `is...` | `verifyTrue()`, `isElementDisplayed()` |
| Wait | `waitFor...` | `waitForElementVisible()` |
| Flow business | `<động từ><kết quả>` | `loginWithAccount()`, `dangNhapApp()` |

### Biến & hằng số

| Loại | Quy tắc | Ví dụ |
|------|---------|-------|
| Locator (Page UI) | `UPPER_SNAKE_CASE` | `USERNAME_TEXTBOX` |
| Hằng số global | `UPPER_SNAKE_CASE` trong `GlobalConstants` | `API_BASE_URL` |
| Field Page Object | camelCase | `driver`, `dangNhapPO` |
| Biến local | camelCase | `userName`, `ticketID` |
| TestNG parameter | camelCase | `browser`, `deviceName`, `appiumUrl` |

### File & thư mục

- Java source: **PascalCase** (`DangNhapPO.java`).
- Resource: **camelCase / kebab-case** (`config.properties`, `runTestCase.xml`, `devices.json`).
- Test data: đặt trong `testData/`, `uploadFiles/` — tên mô tả nội dung.

## Page Object Model

### Page UI — chỉ chứa locator

```java
public class DangNhapPageUI {
    public static final By USERNAME_TEXTBOX = By.xpath("//input[@type='email']");
    public static final By PASSWORD_TEXTBOX = By.xpath("//input[@type='password']");
    public static final By LOGIN_BUTTON = By.xpath("//button[text()=' Đăng nhập ']");
}
```

**Không** đặt logic, wait, click trong Page UI.

### Page Object — chứa hành vi

```java
public class DangNhapPO extends BasePage {
    WebDriver driver;

    public DangNhapPO(WebDriver driver) {
        this.driver = driver;
    }

    public TrangChuPO loginWithAccount(String user, String password) {
        sendkeyToElement(driver, DangNhapPageUI.USERNAME_TEXTBOX, user);
        sendkeyToElement(driver, DangNhapPageUI.PASSWORD_TEXTBOX, password);
        clickToElement(driver, DangNhapPageUI.LOGIN_BUTTON);
        return PageGenerator.getTrangChuPage(driver);
    }
}
```

**Quy tắc:**

- Kế thừa `BasePage`, không gọi Selenium API trực tiếp nếu `BasePage` đã có method tương ứng.
- Không expose `By` locator ra test script.
- Method public mô tả hành vi nghiệp vụ, không mô tả kỹ thuật (`clickButtonX` → `clickToLoginButton`).

## Test Script

```java
public class TenTest extends BaseTest {

    @Parameters({"browser", "url"})
    @BeforeClass
    public void beforeClass(String browserName, String appUrl) {
        driver = getBrowserDriver(browserName, appUrl);
        pagePO = PageGenerator.getTenTrangPage(driver);
    }

    @Test(priority = 1)
    public void TenTestCase_MoTaNghiepVu() {
        // Arrange → Act → Assert
        verifyTrue(condition);
        verifyEquals(actual, expected);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        closeAllBrowsers();
    }

    private WebDriver driver;
    TenTrangPO pagePO;
}
```

**Quy tắc:**

- Test class kế thừa `BaseTest`.
- Setup/teardown trong `@BeforeClass` / `@AfterClass` (Web) hoặc `@BeforeMethod` / `@AfterMethod` (Mobile).
- Luôn dọn driver ở teardown với `alwaysRun = true`.
- Assert dùng method `verify*` của `BaseTest` hoặc TestNG `Assert`.
- Không chứa locator — gọi qua Page Object.
- `@Test` method: mô tả nghiệp vụ, có thể dùng `priority` khi phụ thuộc thứ tự.

## API Client

```java
public class LoginApiWithRest extends BaseApi {
    private final String REST_LOGIN_PATH = "/api/v1/login";

    public Response loginWithRest(String email, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        return post(REST_LOGIN_PATH, body);
    }
}
```

- Kế thừa `BaseApi`.
- Endpoint khai báo `private final String`.
- Dùng `@Step` (kế thừa từ BaseApi) để ghi Allure.
- Verify response trong test hoặc method riêng, không trộn lẫn request + assert phức tạp trong một method.

## Locator strategy

Ưu tiên theo thứ tự:

1. **ID** — ổn định nhất.
2. **CSS Selector** — hiệu năng tốt.
3. **XPath** — khi không có lựa chọn khác; hỗ trợ dynamic qua `String.format`.

Dynamic locator trong `BasePage`:

```java
// Page UI
public static final String TICKET_ROW = "//div[text()='%s']";

// Page Object
clickToElement(driver, TicketPageUI.TICKET_ROW, ticketId);
```

## Wait & sleep

- Ưu tiên **explicit wait** (`waitForElementVisible`, `waitForElementClickable`) từ `BasePage`.
- `sleepInMiliSecond()` chỉ dùng khi không có điều kiện wait rõ ràng (animation, loading không có locator).
- Timeout: dùng `GlobalConstants.SHORT_TIMEOUT` (5s) và `LONG_TIMEOUT` (12s).

## Logging & reporting

- Log: `log.info()` từ Apache Commons Logging (`BaseTest`) hoặc Log4j (`BaseApi`).
- Allure step: `Allure.step("Mô tả bước")` trong `BasePage`; `@Step` trên method API.
- Không log token/password đầy đủ — cắt ngắn như `BrowserFactory.getAuthToken()`.

## Exception handling

- Custom exception: đặt trong `exception/` (ví dụ `BrowserNotSupport`).
- Trong Page Object: để `BasePage` xử lý wait/timeout; throw `RuntimeException` có message rõ ràng khi data không hợp lệ.
- Không nuốt exception im lặng (tránh `catch (Exception e) {}` trống — trừ listener có comment giải thích).

## Import & format

- Không dùng wildcard import (`import java.util.*`) — import tường minh.
- Thứ tự import: Java standard → third-party → project internal.
- Indent: **4 spaces** (theo code hiện tại).
- Một class một file public.

## Git & bảo mật

- **Không commit** token thật, password DB vào repo — dùng `config.properties` local hoặc biến môi trường CI.
- File `.env`, credential thật nên nằm trong `.gitignore`.
- Test data nhạy cảm: anonymize hoặc dùng account staging.

## Checklist review code

- [ ] Page UI / PO tách biệt.
- [ ] Tên class/method theo convention.
- [ ] Không duplicate logic đã có trong `BasePage`.
- [ ] Teardown driver đầy đủ.
- [ ] Không hardcode URL nếu đã có trong `GlobalConstants` hoặc TestNG parameter.
- [ ] Allure step đủ cho flow chính.
