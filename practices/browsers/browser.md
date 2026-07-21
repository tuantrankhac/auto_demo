# Browser Practice — Hướng dẫn mở Web cho AI

> AI **bắt buộc đọc file này** trước khi mở trình duyệt / generate Locator Web / chạy flow Web UI.
> USER có thể override từng giá trị trong prompt — nếu không nói, dùng mặc định bên dưới.

---

## Mặc định

| Tham số | Giá trị mặc định | Ghi chú |
|---------|------------------|---------|
| Browser | `chrome` | Hỗ trợ: `chrome`, `firefox`, `edge`, `safari` |
| Base URL | `https://stg-crm.smarthiz.vn` | Staging CRM |
| Login path | `/login` | Full URL: `https://stg-crm.smarthiz.vn/login` |
| Headless | `false` (khi AI mở bằng Browser MCP) | TestNG có thể khác theo config |
| MCP | Playwright (`browser_navigate`) | Ưu tiên Browser MCP khi generate locator / đọc DOM |

---

## Cách AI mở trình duyệt

### 1. Generate Locator / đọc DOM (Browser MCP)

1. Đọc file này → lấy `browser` + URL.
2. Gọi Playwright MCP:
   - `browser_navigate` với URL đầy đủ (vd: `https://stg-crm.smarthiz.vn/login`).
3. Nếu SSL lỗi (staging cert) → dùng context `ignoreHTTPSErrors` (xem kinh nghiệm project).
4. `browser_snapshot` / `browser_evaluate` để đọc DOM.
5. Sinh / mở rộng PageUI theo convention.

### 2. Generate / chạy Test Script (Selenium Framework)

Tham số TestNG (khớp `runTestCase.xml`):

```xml
<parameter name="browser" value="chrome"/>
<parameter name="url" value="https://stg-crm.smarthiz.vn/login"/>
```

Trong test:

```java
driver = getBrowserDriver(browserName, appUrl);
```

---

## URL theo module (tham chiếu)

| Module | Path | Full URL |
|--------|------|----------|
| Login | `/login` | `https://stg-crm.smarthiz.vn/login` |
| Home | `/` (sau login) | theo redirect sau đăng nhập |

Nếu requirement / Jira có URL khác → **ưu tiên URL trong requirement/Jira**, vẫn đọc file này để biết browser mặc định.

---

## Quy tắc

- Không tự bịa URL khi file này + requirement đã có.
- Không đổi browser mặc định trừ khi USER yêu cầu.
- Sau khi mở trang: chờ DOM ổn định rồi mới đọc locator.
- Credential / token: lấy từ `practices/testdata` hoặc `config.properties` — không hardcode trong prompt log.

---

## Liên kết

- Config URL global: `constant.GlobalConstants.FRONTEND_URL`
- Suite: `src/test/resources/runTestCase.xml`
- MCP: `.cursor/mcp.json` → `playwright`
