# iOS Practice — Hướng dẫn mở App cho AI

> AI **bắt buộc đọc file này** trước khi kết nối device iOS / generate Locator Mobile iOS / chạy flow iOS.
> USER có thể override trong prompt — nếu không nói, dùng mặc định bên dưới.

---

## Mặc định (staging)

| Tham số | Giá trị mặc định | Nguồn |
|---------|------------------|--------|
| Platform | `ios` | — |
| Device name (TestNG) | `IPHONE_15` | `devices.json` |
| Appium URL | `http://127.0.0.1:4723` | — |
| bundleId | `com.sharitek.lada.ios.stg` | `environments/stg.json` |
| Orientation | `PORTRAIT` | `devices.json` |
| Automation | `XCUITest` | Appium |
| MCP | Appium MCP (`appium_launch_app`, `appium_page_source`) | `.cursor/mcp.json` |

> Chỉ chạy được trên **macOS** + Xcode + thiết bị/simulator thật. Windows host → báo USER không hỗ trợ iOS local.

---

## Cách AI mở app iOS

### 1. Generate Locator / đọc UI (Appium MCP)

1. Đọc file này → lấy `bundleId`, device, appiumUrl.
2. Xác nhận môi trường macOS + Appium + XCUITest driver.
3. Mở app:

```
appium_launch_app
  platform: ios
  appiumUrl: http://127.0.0.1:4723
  udid: <udid simulator/device>
  bundleId: com.sharitek.lada.ios.stg
  noReset: true
```

4. Lấy hierarchy:

```
appium_page_source  (savePath: tmp/ios_ui.xml)
```

5. Sinh locator theo ưu tiên Appium iOS: accessibility id → predicate → class chain → xpath.
6. PageUI đặt trong `demo.mobile.pageUIs` (cùng convention, có thể dùng chung locator accessibilityId với Android nếu app cross-platform).

### 2. Generate / chạy Test Script

```xml
<parameter name="deviceName" value="IPHONE_15"/>
<parameter name="appiumUrl" value="http://127.0.0.1:4723"/>
```

```java
driver = getMobileDriver(deviceName, appiumUrl);
```

`MobileFactory` dùng `XCUITestOptions` + `bundleId` từ env config.

---

## Devices tham chiếu (`devices.json`)

| Key | platform | version | udid |
|-----|----------|---------|------|
| `IPHONE_15` | ios | 17.0 | `uuid-cua-iphone-that` |

Cập nhật `udid` thật trước khi chạy trên máy local.

---

## Quy tắc

- Không giả lập iOS trên Windows — dừng và báo USER.
- Không hardcode bundleId khác stg khi USER không yêu cầu.
- Kiểm tra PageUI / PO tên tương đồng trước khi tạo mới.
- AccessibilityId ổn định được ưu tiên (dùng chung Android/iOS nếu có).

---

## Liên kết

- `src/test/resources/devices.json`
- `src/test/resources/environments/stg.json`
- `commons.MobileFactory`
- `mcp/appium-mcp/README.md`
- Workflow: `.agent/workflows/generate_locator.md` (nhánh iOS)
