---
description: Tự động phân tích giao diện Web/Mobile, lấy Locator tối ưu theo Coding Convention của Framework và sinh PageUI.
skills:
  - framework_architect
  - smart_locator_agent
  - code_review_agent
---

# Workflow: Generate Locator

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - smart_locator_agent
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI tự động lấy Locator từ Web hoặc Mobile và generate PageUI theo đúng Framework.

Locator phải:

- Ổn định.
- Dễ bảo trì.
- Có khả năng tái sử dụng.
- Theo đúng Coding Convention.

---

# Khi nào sử dụng

Workflow này được gọi khi:

- Có màn hình mới.
- Có element mới.
- Locator bị thay đổi.
- Locator bị fail.
- Cần generate PageUI.

---

# Input

AI có thể nhận một trong các nguồn sau:

- URL của Web.
- Browser Agent.
- HTML.
- DOM.
- XML.
- Android Device.
- iOS Device.
- Screenshot (nếu AI hỗ trợ Vision).

Không giới hạn chỉ một loại Input.

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- CodingConvention.md
- Locator Strategy

Hiểu:

- Locator được lưu ở đâu.
- Quy tắc đặt tên.
- Framework sử dụng By nào.
- Có PageUI hay Object Repository hay không.

Không generate Locator nếu chưa hiểu Framework.

---

# Bước 1.1 - Đọc Practice môi trường (bắt buộc)

Xác định nền tảng rồi **đọc file practice tương ứng** trước khi mở browser/app:

| Nền tảng | File bắt buộc đọc |
|----------|-------------------|
| Web | `practices/browsers/browser.md` |
| Android | `practices/mobile/android.md` |
| iOS | `practices/mobile/ios.md` |

Từ file practice lấy:

- Browser / device mặc định
- URL / appPackage / appActivity / bundleId
- Cách mở (Playwright MCP / Appium MCP / ADB)
- Appium URL, ưu tiên Emulator…

USER override trong prompt → ưu tiên USER.
Không có override → dùng đúng giá trị trong practice.

**Không** mở URL/app tự ý khi chưa đọc practice.

---

# Bước 2 - Xác định nền tảng

AI xác định:

- Web
- Android
- iOS

Sau đó chuyển sang workflow tương ứng.

---

# Bước 3 - Thu thập giao diện

## Đối với Web

Ưu tiên theo thứ tự:

### Cách 0 (Bắt buộc trước khi mở)

Đọc `practices/browsers/browser.md` → lấy browser + URL mặc định.

### Cách 1 (Khuyến nghị)

AI Agent / Browser Agent (Playwright MCP)

↓

Mở URL (theo practice hoặc USER)

↓

Đọc toàn bộ DOM

↓

Phân tích DOM

↓

Generate Locator

---

### Cách 2

User cung cấp URL

↓

AI mở URL

↓

Đọc DOM

↓

Generate Locator

---

### Cách 3

User truyền HTML hoặc DOM

↓

AI phân tích

↓

Generate Locator

---

### Cách 4

User truyền OuterHTML của Element

↓

AI generate Locator

---

## Đối với Android

Ưu tiên theo thứ tự:

### Cách 0 (Bắt buộc trước khi mở)

Đọc `practices/mobile/android.md` → package, activity, device, appiumUrl.

### Cách 1 (Khuyến nghị)

Appium MCP: `appium_launch_app` → `appium_page_source`

↓

Đọc XML

↓

Phân tích XML

↓

Generate Locator

↓

Sinh PageUI

---

### Cách 2 (Fallback ADB)

AI gọi ADB để dump UI Hierarchy

```bash
adb shell uiautomator dump /sdcard/ui.xml
adb pull /sdcard/ui.xml
```

↓

Đọc file ui.xml

↓

Phân tích XML

↓

Generate Locator

↓

Sinh PageUI

---

### Cách 2

Appium Inspector

↓

Đọc XML

↓

Generate Locator

---

### Cách 3

User truyền XML

↓

AI phân tích

↓

Generate Locator

---

## Đối với iOS

Ưu tiên theo thứ tự:

### Cách 0 (Bắt buộc trước khi mở)

Đọc `practices/mobile/ios.md` → bundleId, device, appiumUrl. (Chỉ macOS.)

### Cách 1 (Khuyến nghị)

Appium MCP: `appium_launch_app` (ios) → `appium_page_source`

↓

Đọc XML / UI Hierarchy

↓

Generate Locator

↓

Sinh PageUI

---

### Cách 2

Appium Inspector

↓

Đọc XML

↓

Generate Locator

---

### Cách 3

User truyền XML

↓

AI phân tích

↓

Generate Locator

---

# Bước 4 - Phân tích Element

AI phân tích:

- id
- name
- resource-id
- accessibilityId
- content-desc
- class
- text
- aria-label
- data-testid
- parent
- child
- sibling
- hierarchy
- dynamic attribute

Không chỉ dựa vào XPath.

---

# Bước 5 - Chọn Locator

## Web

Ưu tiên:

1. id
2. name
3. data-testid
4. aria-label
5. css selector
6. relative xpath
7. absolute xpath (không khuyến khích)

---

## Mobile

Ưu tiên:

1. accessibilityId
2. resource-id
3. content-desc
4. id
5. UiSelector (Android)
6. Predicate / Class Chain (iOS)
7. xpath

Không ưu tiên XPath nếu còn lựa chọn tốt hơn.

---

# Bước 6 - Validate Locator

Kiểm tra:

- Locator có unique không.
- Có dynamic không.
- Có duplicate không.
- Có phụ thuộc index không.
- Có ổn định khi UI thay đổi không.

Nếu Locator chưa tối ưu.

↓

Đề xuất Locator khác.

---

# Bước 7 - Generate / Mở rộng PageUI

Trước khi tạo class PageUI mới, **bắt buộc** kiểm tra:

1. PageUI đúng tên đã tồn tại chưa.
2. PageUI **tên tương đồng** đã tồn tại chưa (vd: `LoginPageUI` ↔ `DangNhapPageUI`).

### Quyết định

| Kết quả so sánh | Hành động |
|-----------------|-----------|
| Giống nhau (cùng màn hình / cùng nghiệp vụ) | **Mở rộng** PageUI hiện có — chỉ bổ sung locator còn thiếu. Không tạo file mới. |
| Không giống nhau | **Tạo mới** PageUI theo Framework / Coding Convention. |
| Không chắc | **Hỏi USER** trước khi tạo mới. |

Ví dụ tên:

- LoginPageUI / DangNhapPageUI
- HomePageUI / TrangChuPageUI
- CustomerPageUI

Không viết Locator trong Test Script.

Không viết Locator trong PageObject.

Không tạo cặp song song (`LoginPageUI` + `DangNhapPageUI`) cho cùng một màn hình.

---

# Bước 8 - Review

Kiểm tra:

- Naming Convention.
- Duplicate Locator.
- Locator Strategy.
- Maintainability.
- Readability.

Nếu tìm được Locator tốt hơn.

↓

Đề xuất thay thế.

---

# Output

Workflow có thể sinh:

- PageUI.
- Locator.
- Locator Summary.
- Locator Recommendation.
- Review Summary.

---

# Tiêu chí hoàn thành

Locator phải:

- Unique.
- Stable.
- Dễ bảo trì.
- Theo Coding Convention.
- Có thể tái sử dụng.

---

# Không được

- Không hardcode XPath.
- Không ưu tiên Absolute XPath.
- Không sử dụng index nếu không cần.
- Không generate Locator khi chưa có dữ liệu.
- Không viết Locator trong Test Script.
- Không viết Locator trong PageObject.
- Không bỏ qua Coding Convention.
- Không bỏ qua bước Review.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent (Cursor Agent, Cline, Kilo Code...)

↓

AI sẽ tự động:

- Mở URL (Web) hoặc kết nối thiết bị (Mobile).
- Thu thập DOM hoặc UI Hierarchy.
- Phân tích cấu trúc giao diện.
- Generate Locator.
- Sinh PageUI.
- Review Locator trước khi trả kết quả.

User chỉ cần mô tả yêu cầu, ví dụ:

> Generate locator cho màn hình Login.

AI sẽ tự thực hiện toàn bộ quy trình trên.

Nếu project chưa hỗ trợ AI Agent.

↓

Fallback sang:

- DevTools.
- Appium Inspector.
- HTML.
- XML.
- OuterHTML.