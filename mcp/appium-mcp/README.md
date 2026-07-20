# Appium MCP

MCP server để Cursor AI Agent điều khiển thiết bị Mobile qua **Appium** (Android / iOS).

## Cấu trúc

```
mcp/appium-mcp/
├── package.json
├── tsconfig.json
├── index.ts              # MCP server + đăng ký tools
├── appiumClient.ts       # HTTP client (W3C / Appium protocol)
├── session.ts            # Quản lý session + build capabilities
└── commands/
    ├── launchApp.ts
    ├── click.ts
    ├── input.ts
    ├── scroll.ts
    ├── pageSource.ts
    └── screenshot.ts
```

## Tools

| Tool | Mô tả |
|------|--------|
| `appium_launch_app` | Tạo session + mở app |
| `appium_close_app` | Đóng session |
| `appium_click` | Tap element |
| `appium_input` | Nhập text |
| `appium_scroll` | Swipe / scrollIntoView |
| `appium_page_source` | Lấy UI hierarchy XML (generate locator) |
| `appium_screenshot` | Chụp màn hình |
| `appium_status` | Kiểm tra Appium server / session |

## Chuẩn bị

1. Cài dependency:

```bash
cd mcp/appium-mcp
npm install
```

2. Chạy Appium server:

```bash
appium
# hoặc: appium --address 127.0.0.1 --port 4723
```

3. Kết nối device / emulator (`adb devices`).

4. Reload MCP trong Cursor (Settings → MCP → refresh) — server `appium` đã cấu hình trong `.cursor/mcp.json`.

## Ví dụ gọi tool

**Launch Android app:**

```json
{
  "platform": "android",
  "udid": "emulator-5554",
  "appPackage": "com.demo.app",
  "appActivity": ".MainActivity"
}
```

**Lấy page source để generate locator:**

```json
{
  "savePath": "tmp/ui.xml"
}
```

## Biến môi trường

| Biến | Mặc định | Ý nghĩa |
|------|----------|---------|
| `APPIUM_URL` | `http://127.0.0.1:4723` | URL Appium server |
