# Môi trường & Cài đặt

## Yêu cầu hệ thống

| Thành phần | Phiên bản tối thiểu | Ghi chú |
|------------|---------------------|---------|
| JDK | 17 | Bắt buộc (`pom.xml` dùng `release 17`) |
| Maven | 3.8+ | Build và chạy test |
| Git | — | Clone repository |
| Chrome / Firefox / Edge | Mới nhất | Web UI test (driver tương ứng phải có trong PATH) |
| Appium Server | 2.x | Mobile test |
| Node.js | 18+ | Cài Appium qua npm |
| Android SDK | — | Test Android (platform-tools, adb) |
| Xcode | — | Test iOS (chỉ macOS) |
| PostgreSQL | — | Test kết nối DB (tùy chọn) |
| Docker & Docker Compose | — | Selenium Grid (tùy chọn) |
| Allure CLI | 2.32.0 | Mở report local (đã có sẵn trong `.allure/`) |

## Clone & build project

```bash
git clone <repository-url>
cd auto_demo
mvn clean compile
```

## Cấu hình môi trường

### 1. File `src/test/resources/config.properties`

| Key | Mô tả | Ví dụ |
|-----|-------|-------|
| `auth.token` | Bearer token inject qua CDP | `Bearer eyJ...` |
| `auth.username` | Username Basic Auth | `testuser` |
| `auth.password` | Password Basic Auth | `testpass123` |
| `base.url` | URL ứng dụng (tham khảo) | `https://your-protected-app.com` |
| `timeout.seconds` | Timeout mặc định | `15` |
| `headless` | Chế độ headless (tham khảo) | `true` |

**Ưu tiên biến môi trường** (override config file):

| Biến môi trường | Tương ứng config |
|-----------------|------------------|
| `AUTH_TOKEN` | `auth.token` |
| `AUTH_USERNAME` | `auth.username` |
| `AUTH_PASSWORD` | `auth.password` |

### 2. File `src/test/resources/devices.json`

Cấu hình thiết bị mobile theo tên key (truyền vào TestNG parameter `deviceName`):

```json
{
  "V350C": {
    "platform": "android",
    "version": "10.0",
    "udid": "BNS00000074",
    "appPackage": "com.sharitek.lada",
    "appActivity": "com.sharitek.lada.MainActivity",
    "appPath": ""
  }
}
```

| Field | Mô tả |
|-------|-------|
| `platform` | `android` hoặc `ios` |
| `version` | Phiên bản OS |
| `udid` | UDID thiết bị thật / emulator |
| `appPackage` / `appActivity` | Android: mở app đã cài sẵn |
| `appPath` | Đường dẫn file `.apk` / `.ipa` (ưu tiên nếu không rỗng) |
| `bundleId` | iOS: bundle identifier |

### 3. Hằng số trong `GlobalConstants.java`

| Hằng số | Giá trị | Ý nghĩa |
|---------|---------|---------|
| `API_BASE_URL` | `https://stg-crm-api.smarthiz.com` | Base URL API |
| `FRONTEND_URL` | `https://stg-crm.smarthiz.vn` | URL frontend CRM |
| `SHORT_TIMEOUT` | 5 giây | Wait ngắn |
| `LONG_TIMEOUT` | 12 giây | Wait dài |
| `UPLOAD_FILE_FOLDER` | `{project}/uploadFiles/` | Thư mục file upload |
| `DOWNLOAD_FILE_FOLDER` | `{project}/downloadFiles` | Thư mục file download |

### 4. Kết nối Database (`DbConnection.java`)

Chỉnh trực tiếp trong class hoặc chuyển sang config nếu cần:

```
jdbc:postgresql://localhost:5432/your_database_name
Username: postgres
Password: your_password
```

## Cài đặt WebDriver

Framework hỗ trợ: **Chrome**, **Firefox**, **Edge Chromium**, **Safari**.

Driver binary cần tương thích với trình duyệt đã cài. Project có dependency **WebDriverManager** (hiện đang comment trong code — có thể bật lại nếu muốn tự tải driver).

Trình duyệt mặc định khi chạy test:

- Chrome: `--incognito`
- Firefox: `-private`
- Edge: `-inprivate`

## Cài đặt Mobile (Appium)

```bash
npm install -g appium
appium driver install uiautomator2   # Android
appium driver install xcuitest       # iOS
```

Khởi động Appium Server:

```bash
appium --address 127.0.0.1 --port 4723
```

Kiểm tra thiết bị Android:

```bash
adb devices
```

## Selenium Grid (Docker)

```bash
docker-compose up -d
```

| Service | Port | Mô tả |
|---------|------|-------|
| selenium-hub | 4444 | Hub endpoint |
| chrome | — | Chrome node (tối đa 5 session) |

Hub URL: `http://localhost:4444`

> Lưu ý: Cần chỉnh `BrowserFactory` để trỏ `RemoteWebDriver` nếu muốn chạy qua Grid.

## Chạy test

### Chạy toàn bộ suite (mặc định)

```bash
mvn clean verify
```

Surefire dùng suite: `src/test/resources/runTestCase.xml`.

### Chạy test cụ thể

Bật (uncomment) block `<test>` tương ứng trong `runTestCase.xml`, comment các test khác, rồi chạy:

```bash
mvn clean test
```

### Maven profiles

| Profile | Lệnh | Mô tả |
|---------|------|-------|
| `web` | `mvn test -Pweb` | Đặt `run.target=web` |
| `mobile` | `mvn test -Pmobile` | Đặt `run.target=mobile` |

### Xem Allure Report

Sau `mvn verify`, report nằm tại:

```
report/allure-report-{HH-mm_yyyy-MM-dd}/
```

Mở bằng Allure CLI:

```bash
allure open report/allure-report-<timestamp>
```

Hoặc dùng bản local trong project:

```bash
.allure/allure-2.32.0/bin/allure open report/allure-report-<timestamp>
```

## Thư mục dữ liệu cần chuẩn bị

| Thư mục | Mục đích |
|---------|----------|
| `uploadFiles/` | File upload trong test UI |
| `downloadFiles/` | File tải về từ browser |
| `testData/` | Excel, JSON data-driven |
| `target/allure-results/` | Kết quả thô Allure (tự sinh khi chạy test) |

## Troubleshooting

| Vấn đề | Cách xử lý |
|--------|------------|
| `AUTH_TOKEN` không tìm thấy | Set biến môi trường hoặc thêm vào `config.properties` |
| Driver crash / OOM | Tăng `-Xmx` trong `pom.xml` (hiện tại 2048m) |
| Appium không kết nối | Kiểm tra `appiumUrl`, port 4723, `adb devices` |
| Không tìm thấy device trong JSON | Kiểm tra key `deviceName` khớp với `devices.json` |
| Allure report trống | Đảm bảo chạy `mvn verify` (phase generate report) |
| Fork crash trên Windows | Surefire đã cấu hình `forkCount=0` để tránh lỗi agent |
