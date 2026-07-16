# RULE_GLOBAL — Quy tắc toàn cục

> File này định nghĩa các quy tắc **bắt buộc** khi AI Agent làm việc với project **Demo Framework**.
> Chi tiết kỹ thuật xem thêm trong thư mục `docs/`.

---

## 1. Thứ tự ưu tiên

Khi có xung đột, tuân thủ theo thứ tự:

1. **USER Request** — yêu cầu trực tiếp của người dùng
2. **RULE_GLOBAL.md** — quy tắc toàn cục (file này)
3. **AGENTS.md** — hướng dẫn vai trò và quy trình Agent
4. **docs/** — tài liệu kỹ thuật chi tiết
5. **.agent/rules/**, **.agent/skills/**, **.agent/workflows/** — nếu tồn tại trong project
6. **Source code hiện có** — convention thực tế đang được dùng

---

## 2. Ngôn ngữ

| Ngữ cảnh | Quy tắc |
|----------|---------|
| Giao tiếp với USER | Tiếng Việt (mặc định) |
| Package, Class | Tiếng Anh, PascalCase |
| Method, Variable, Constant | Tiếng Anh, camelCase / UPPER_SNAKE_CASE |
| Comment, log message | Tiếng Việt hoặc Tiếng Anh — **theo convention module hiện có** |
| Tên method nghiệp vụ | Ưu tiên tiếng Anh; cho phép tiếng Việt nếu module đang dùng (vd: `dangNhapApp`) |

**Không** dùng tiếng Việt có dấu trong tên package/class.

---

## 3. Mục tiêu chất lượng

AI phải hoạt động như **Senior Automation Engineer**:

- Code đúng nghiệp vụ, đúng kiến trúc framework
- Tuân thủ Coding Convention (`docs/CodingConvention.md`)
- Tái sử dụng cao, dễ bảo trì, dễ mở rộng
- Không duplicate code/locator/business method
- Không ưu tiên tốc độ generate — **ưu tiên generate đúng**

---

## 4. Đọc project trước khi làm việc

Trước khi generate hoặc sửa code, **bắt buộc**:

1. Đọc tài liệu liên quan trong `docs/` (xem mục 5)
2. Đọc `AGENTS.md`
3. Đọc source code module tương ứng (Page Object, test script, API client đã có)
4. Kiểm tra project **đã có** chức năng/locator/method tương tự chưa
5. Chỉ tạo phần còn thiếu — **không tạo lại** những gì framework đã hỗ trợ

Không generate code khi chưa hiểu cấu trúc project.

---

## 5. Tài liệu tham chiếu

| File | Khi nào đọc |
|------|-------------|
| `docs/Framework.md` | Hiểu component core, utilities, reporting |
| `docs/Architecture.md` | Hiểu kiến trúc, design pattern, luồng thực thi |
| `docs/CodingConvention.md` | Đặt tên, POM, locator, import |
| `docs/AutomationProcess.md` | Quy trình tạo test mới, template, checklist |
| `docs/Environment.md` | Cấu hình môi trường, chạy test, troubleshooting |

Nếu có thêm tài liệu trong `knowledge/` → đọc trước khi generate.

---

## 6. Quy trình Generate Automation

Luôn thực hiện **đúng thứ tự** (trừ khi USER yêu cầu khác):

1. Phân tích Manual Testcase / yêu cầu
2. Xác định loại test: **Web** / **Mobile** / **API** / kết hợp
3. Xác định màn hình / endpoint liên quan
4. Generate **PageUI** (locators) — nếu chưa có
5. Generate **PageObject** (business methods) — nếu chưa có
6. **Đăng ký PageGenerator** — thêm factory method mới
7. Generate **Helper Method** — chỉ khi `BasePage` / utilities chưa có
8. Generate **API Script** — kế thừa `BaseApi`, đăng ký `ApiFactory` nếu cần
9. Generate **Test Script** — kế thừa `BaseTest`
10. Cấu hình **runTestCase.xml** — thêm `<test>` block với parameter
11. Review code (mục 12)
12. Chạy test nếu có thể (`mvn clean test`)
13. Debug nếu fail (mục 13)

---

## 7. Quy tắc Framework

### 7.1 Luôn sử dụng

| Component | Vai trò |
|-----------|---------|
| `BasePage` | Mọi thao tác UI (click, sendKeys, wait, scroll...) |
| `BaseTest` | Lớp cha test script, verify helpers |
| `BaseApi` | Mọi API client |
| `BrowserFactory` | Khởi tạo / đóng WebDriver |
| `MobileFactory` | Khởi tạo / đóng AppiumDriver |
| `PageGenerator` | Khởi tạo Page Object — không `new` trực tiếp trong test |
| `ApiFactory` | Lazy-init API client |
| `GlobalConstants` | URL, timeout, đường dẫn thư mục |
| `ConfigReader` | Đọc `config.properties` / biến môi trường |

### 7.2 Không tạo lại

- Wait/click/sendKeys tự viết khi `BasePage` đã có
- Driver factory riêng khi `BrowserFactory` / `MobileFactory` đã có
- HTTP client riêng khi `BaseApi` + Rest Assured đã có

### 7.3 Cấu hình — không hard-code

| Dữ liệu | Nguồn |
|---------|-------|
| API Base URL | `GlobalConstants.API_BASE_URL` |
| Frontend URL | `GlobalConstants.FRONTEND_URL` hoặc TestNG parameter `url` |
| Auth token | `AUTH_TOKEN` env hoặc `config.properties` → `auth.token` |
| Credentials | `auth.username` / `auth.password` hoặc env |
| Mobile device | `devices.json` + parameter `deviceName` |
| Timeout | `GlobalConstants.SHORT_TIMEOUT` / `LONG_TIMEOUT` |
| Upload/Download path | `GlobalConstants.UPLOAD_FILE_FOLDER` / `DOWNLOAD_FILE_FOLDER` |

---

## 8. Quy tắc Locator

- Locator **chỉ** đặt trong class `*PageUI` (`demo.web.pageUIs` / `demo.mobile.pageUIs`)
- **Không** viết locator trực tiếp trong Test Script
- Page Object **tham chiếu** locator qua `*PageUI`, không khai báo `By` mới (trừ dynamic template)

Ưu tiên locator ổn định:

1. `id`
2. `name`
3. `data-testid`
4. CSS selector
5. XPath (relative, hỗ trợ dynamic qua `String.format`)

**Không** dùng XPath tuyệt đối dài (`/html/body/div[1]/...`).

---

## 9. Quy tắc Page Object

Page Object **chỉ chứa**:

- Business methods mô tả hành vi người dùng
- Navigation flow (trả về Page Object tiếp theo)

Page Object **không chứa**:

- Test logic / assertion
- Test data
- Locator declaration (để trong PageUI)

Mọi thao tác UI gọi qua method của `BasePage`.

---

## 10. Quy tắc Test Script

Test Script **chỉ nên**:

- Setup driver trong `@BeforeClass` / `@BeforeMethod`
- Khởi tạo Page Object qua `PageGenerator`
- Gọi business method của Page Object
- Verify qua `verifyTrue`, `verifyEquals` (từ `BaseTest`) hoặc TestNG `Assert`
- Teardown driver trong `@AfterClass` / `@AfterMethod` với `alwaysRun = true`

Test Script **không được**:

- Gọi Selenium / Appium API trực tiếp
- Chứa locator
- Quên đóng driver

---

## 11. Quy tắc API

- Kế thừa `BaseApi`
- Đăng ký trong `ApiFactory` nếu cần lazy init
- Không hard-code Base URL — dùng `GlobalConstants.API_BASE_URL`
- Không hard-code token — lấy từ login response hoặc config
- Dùng `@Step` / Allure filter có sẵn trong `BaseApi`

---

## 12. Quy tắc Wait & Timing

- **Ưu tiên** Explicit Wait: `waitForElementVisible`, `waitForElementClickable` từ `BasePage`
- **Không** dùng `Thread.sleep()` trực tiếp trong code mới
- Nếu cần delay cố định, dùng `sleepInMiliSecond()` từ `BasePage` / `BaseTest` (convention hiện tại của project)
- Không dùng magic number — tham chiếu `GlobalConstants` hoặc khai báo hằng số có tên

---

## 13. Quy tắc Review

Trước khi hoàn thành, kiểm tra:

- [ ] Compile error / import thừa
- [ ] Naming Convention (`docs/CodingConvention.md`)
- [ ] Locator tách PageUI / Page Object
- [ ] PageGenerator đã đăng ký (nếu thêm PO mới)
- [ ] `runTestCase.xml` đã cấu hình (nếu thêm test mới)
- [ ] Không duplicate code / locator / method
- [ ] Không hard-code URL, token, credential
- [ ] Teardown driver đầy đủ
- [ ] Không sửa file core không liên quan (mục 15)

---

## 14. Quy tắc Debug

Khi testcase fail:

1. Đọc log console + Allure report
2. Phân tích stack trace
3. Phân loại nguyên nhân:

| Loại | Hành động |
|------|-----------|
| Automation Script | Được phép sửa script / PO / locator |
| Product Bug | Báo cáo — **không** sửa business logic sản phẩm |
| Environment | Kiểm tra config, driver, Appium, DB (`docs/Environment.md`) |
| Test Data | Kiểm tra dữ liệu test, account staging |

4. Phân biệt rõ lỗi automation vs lỗi sản phẩm trước khi sửa

---

## 15. Quy tắc chỉnh sửa Source Code

- Chỉ sửa file **liên quan trực tiếp** đến yêu cầu
- Diff **nhỏ nhất** có thể — không viết lại toàn bộ file
- **Không sửa** các file core sau **trừ khi USER yêu cầu**:

| File | Lý do |
|------|-------|
| `BasePage` | Thư viện UI dùng chung toàn project |
| `BaseApi` | Base API dùng chung |
| `BrowserFactory` | Quản lý WebDriver |
| `MobileFactory` | Quản lý AppiumDriver |
| `ApiFactory` | Chỉ thêm getter mới khi có API client mới |
| `GlobalConstants` | Hằng số toàn cục |
| `ConfigReader` | Đọc config |
| `BaseTest` | Lớp cha test |

Refactor:

- Không thay đổi hành vi nghiệp vụ
- Giữ backward compatibility
- Không đổi tên public API nếu không cần

---

## 16. An toàn dữ liệu

**Không được:**

- In/log đầy đủ password, token, API key, connection string
- Commit secret vào git (`config.properties` chỉ chứa giá trị demo/staging)

**Không thực hiện** trên DB production nếu chưa có xác nhận USER:

- `DROP DATABASE` / `DROP TABLE`
- `DELETE` / `UPDATE` dữ liệu production

---

## 17. Git

**Không** commit, push, merge, rebase **trừ khi USER yêu cầu**.

---

## 18. File tạm & Cleanup

File debug tạm (`*.tmp`, `*_debug.txt`, `request.json`, `response.json`, `dom_dump.txt`...):

- Lưu trong thư mục tạm (vd: `tmp/`), **không** để rải rác trong project
- **Xóa** file tạm khi kết thúc nhiệm vụ

**Không xóa** nếu không chắc:

- Source code, test data
- Report (`report/`, `extentReport/`)
- Screenshot, video, `allure-results/`, `target/`

Nếu không chắc → hỏi USER trước.

---

## 19. Trao đổi với USER

- Thiếu thông tin nghiệp vụ → **hỏi lại**, không tự suy diễn
- Nhiều cách triển khai → phân tích ưu/nhược, đề xuất cách phù hợp framework
- Yêu cầu mơ hồ → làm rõ phạm vi (Web/Mobile/API) trước khi code

---

## 20. Tiêu chuẩn hoàn thành

Nhiệm vụ chỉ hoàn thành khi:

- Code đúng nghiệp vụ và đúng framework
- Tuân thủ Coding Convention
- Không duplicate, có khả năng tái sử dụng
- Không còn compile error
- Đã review toàn bộ thay đổi (mục 13)
- File tạm đã dọn (nếu có)
