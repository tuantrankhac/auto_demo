# Android Practice — Hướng dẫn mở App cho AI

> AI **bắt buộc đọc file này** trước khi kết nối device Android / generate Locator Mobile / chạy flow Mobile.
> USER có thể override trong prompt — nếu không nói, dùng mặc định bên dưới.

---

## Mặc định (staging)

| Tham số | Giá trị mặc định | Nguồn |
|---------|------------------|--------|
| Platform | `android` | — |
| Device ưu tiên | Emulator nếu có (`emulator-5554`); không thì device trong `devices.json` | `adb devices` |
| Device name (TestNG) | `V350C` (có thể đổi) | `devices.json` |
| Appium URL | `http://127.0.0.1:4723` | — |
| appPackage | `com.sharitek.lada` | `environments/stg.json` |
| appActivity | `com.sharitek.lada.MainActivity` | `environments/stg.json` |
| Orientation | `PORTRAIT` | `devices.json` |
| MCP | Appium MCP (`appium_launch_app`, `appium_page_source`) | `.cursor/mcp.json` |

> Đồng bộ với `src/test/resources/environments/stg.json` và `devices.json`. Nếu env file đổi → cập nhật file này.

---

## Cách AI mở app Android

### 1. Generate Locator / đọc UI (Appium MCP hoặc ADB)

**Thứ tự bắt buộc:**

1. Đọc file này → lấy package / activity / appiumUrl / ưu tiên device.
2. Kiểm tra device:

```bash
adb devices
```

- Không có device → **báo lỗi**, không generate.
- Nhiều device → **ưu tiên Emulator**; không có emulator → hỏi USER.

3. Đảm bảo Appium server đang chạy (`http://127.0.0.1:4723`).
4. Mở app bằng Appium MCP:

```
appium_launch_app
  platform: android
  appiumUrl: http://127.0.0.1:4723
  udid: <từ adb / devices.json>
  appPackage: com.sharitek.lada
  appActivity: com.sharitek.lada.MainActivity
  noReset: true
  autoGrantPermissions: true
```

5. Lấy UI hierarchy:

```
appium_page_source  (savePath: tmp/ui.xml)
```

Hoặc fallback ADB (nếu MCP chưa sẵn sàng):

```bash
adb shell uiautomator dump /sdcard/ui.xml
adb pull /sdcard/ui.xml tmp/ui.xml
```

6. Phân tích XML → sinh / mở rộng PageUI trong `demo.mobile.pageUIs`.
7. Dọn file tạm sau khi xong.

### 2. Generate / chạy Test Script (Appium Framework)

```xml
<parameter name="deviceName" value="V350C"/>
<parameter name="appiumUrl" value="http://127.0.0.1:4723"/>
```

```java
driver = getMobileDriver(deviceName, appiumUrl);
```

Capabilities lấy từ `MobileFactory` + `devices.json` + `EnvironmentManager` (stg/dev/prod).

---

## Devices tham chiếu (`devices.json`)

| Key | platform | version | udid |
|-----|----------|---------|------|
| `V350C` | android | 10.0 | `BNS00000074` |
| `SAMSUNG_S23` | android | 13.0 | `RF8N123456` |

Emulator thường hiện là `emulator-5554` trên `adb devices` — ưu tiên khi generate locator.

---

## Quy tắc

- Không hardcode package/activity khác stg khi USER không yêu cầu.
- Không generate locator khi đang ở Home Launcher / sai app — báo USER.
- Kiểm tra tên PageUI tương đồng trước khi tạo mới (`DangNhapAppPageUI`…).
- Secret / credential: không ghi vào file practice này.

---

## Liên kết

- `src/test/resources/devices.json`
- `src/test/resources/environments/stg.json`
- `commons.MobileFactory`
- `mcp/appium-mcp/README.md`
- Workflow: `.agent/workflows/generate_locator.md` (nhánh Android)
