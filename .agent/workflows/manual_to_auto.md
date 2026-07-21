---
description: Chuyển đổi Manual Test Case thành Automation Test Script theo đúng Framework, Coding Convention và kiến trúc của dự án.
skills:
  - framework_architect
  - jira_integration
  - manual_to_auto_agent
  - smart_locator_agent
  - pageobject_agent
  - automation_script_agent
  - api_agent
  - db_verification_agent
  - code_review_agent
---

# Workflow: Chuyển Manual Test Case thành Automation Test

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc lần lượt các Skill sau:
>
> - framework_architect
> - jira_integration (khi lấy TC từ Jira MCP)
> - manual_to_auto_agent
> - smart_locator_agent
> - pageobject_agent
> - automation_script_agent
> - api_agent (nếu testcase có API)
> - db_verification_agent (nếu testcase có verify DB)
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI chuyển đổi Manual Test Case thành Automation Script hoàn chỉnh theo đúng Framework của dự án.

Automation được sinh ra phải:

- Đúng Coding Convention.
- Đúng Framework.
- Có khả năng tái sử dụng.
- Không duplicate code.
- Không hardcode dữ liệu.
- Có thể chạy được sau khi review.

---

# Khi nào sử dụng

Workflow này được gọi khi:

- Có Manual Test Case mới (Jira / Excel).
- Cần chuyển Manual sang Automation.
- Cần viết Automation từ Requirement.
- Cần generate Test Script hàng loạt.

---

# Input

AI có thể nhận một hoặc nhiều nguồn dữ liệu:

- **Jira / Xray Test Key** (ưu tiên) — qua Jira MCP
- Excel Test Case (`practices/testcases`) — fallback
- Confluence / Word / PDF / Markdown / Google Sheet

Thứ tự ưu tiên nguồn:

```
Jira MCP (jira_connect → get_testcase → get_test_steps)
        │
        ▼
Excel / practices/testcases
        │
        ▼
Markdown / text USER
```

Nếu có nhiều Test Case.

↓

Chỉ xử lý những Test Case được đánh dấu:

- Automation
- Auto
- Candidate
- Regression
- Smoke
- (Jira) label / field tương đương Automation=Yes

Không tự động generate cho toàn bộ Manual Test Case nếu chưa có yêu cầu.

---

# Bước 1 - Hiểu Framework

Đọc:

Framework.md

Architecture.md

CodingConvention.md

Hiểu:

- BasePage
- BaseTest
- BaseApi
- BrowserFactory
- MobileFactory
- PageGenerator
- Utility
- Config

Không generate code nếu chưa hiểu Framework.

---

# Bước 1.1 - Đọc Practice môi trường (Web / Mobile)

| Kênh | Đọc file |
|------|----------|
| Web | `practices/browsers/browser.md` |
| Android | `practices/mobile/android.md` |
| iOS | `practices/mobile/ios.md` |

Mở đúng browser/URL hoặc app theo practice trước khi thu thập DOM/XML.

---

# Bước 2 - Lấy Testcase ID & nội dung từ Jira MCP

**Bắt buộc** khi USER / prompt cung cấp Jira Key, module trên Jira, hoặc yêu cầu lấy TC từ Jira.

```
1. jira_connect
2. Xác định danh sách TestCase ID
   - USER đưa sẵn {JiraKey} → dùng trực tiếp
   - USER đưa module / JQL → jira_search_issues (defaultJQL trong jira.yaml)
3. Với mỗi ID (Automation=Yes / label phù hợp):
   - jira_get_testcase(testKey)
   - jira_get_test_steps(testKey)
4. Mới được sang bước phân tích & generate
```

Nếu không có Jira Key và USER chỉ định Excel → đọc `practices/testcases`.

**Không** bỏ qua bước lấy ID khi nguồn là Jira.

**Không** generate khi chưa có steps/expected từ Jira hoặc file local.

---

# Bước 3 - Phân tích Test Case

Đọc (từ Jira steps hoặc Excel):

- Preconditions
- Steps
- Expected Result
- Test Data

Xác định:

- Web
- Mobile
- API
- Hybrid

Tách từng Step thành từng Business Action.

---

# Bước 4 - Xác định Locator

Nếu là Web.

↓

Đọc DOM.

Nếu là Mobile.

↓

Đọc XML hoặc UI Hierarchy.

Nếu project có Browser Agent hoặc Mobile Agent.

↓

Cho phép Agent tự đọc.

Không tự tạo Locator nếu chưa có dữ liệu.

Ưu tiên:

id

name

data-testid

accessibilityId

css

xpath

Theo đúng Rule của project.

---

# Bước 5 - Generate PageUI

Sinh Locator vào:

PageUI

Không viết Locator trong Test Script.

Không viết Locator trong PageObject.

---

# Bước 6 - Generate PageObject

Sinh:

Business Method.

Ví dụ:

login()

searchCustomer()

createOrder()

approve()

delete()

Không generate Verify.

Không generate Assert.

---

# Bước 7 - Generate API

Nếu testcase có API.

↓

Sinh API Class.

↓

Tận dụng BaseApi.

Không duplicate.

Nếu testcase không có API.

↓

Bỏ qua bước này.

---

# Bước 8 - Generate Test Script

Test Script chỉ chứa:

- Setup
- Business Flow
- Verify
- Cleanup

Không chứa:

Locator

driver.findElement()

XPath

CSS Selector

Hardcode.

---

# Bước 9 - Sinh Test Data

Nếu cần.

↓

Generate:

JSON

Excel

Random Data

SQL

Factory

Payload

Không hardcode.

---

# Bước 10 - Review Code

Sau khi generate.

↓

Tự review.

Kiểm tra:

- Compile.
- Import.
- Coding Convention.
- Framework Convention.
- Duplicate.
- Wait.
- Hardcode.
- Reuse.
- Maintainability.

Nếu phát hiện lỗi.

↓

Sửa nếu không ảnh hưởng Business Logic.

Nếu ảnh hưởng.

↓

Thông báo User.

---

# Output

Workflow có thể sinh:

- PageUI.
- PageObject.
- API Class.
- Test Script.
- Test Data.
- SQL.
- Payload.
- Review Summary.

---

# Tiêu chí hoàn thành

Automation phải:

✓ Compile được.

✓ Đúng Framework.

✓ Đúng Coding Convention.

✓ Không duplicate.

✓ Không hardcode.

✓ Có khả năng tái sử dụng.

✓ Có thể chạy được sau khi review.

---

# Không được

Không generate Locator ngẫu nhiên.

Không hardcode dữ liệu.

Không bypass BasePage.

Không Verify trong PageObject.

Không generate Selenium code trực tiếp trong Test Script.

Không thay đổi Requirement.

Không thay đổi Expected Result.

Không bỏ qua bước Review.