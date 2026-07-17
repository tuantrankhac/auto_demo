---
description: Chuyển đổi Manual Test Case thành Automation Test Script theo đúng Framework, Coding Convention và kiến trúc của dự án.
skills:
  - framework_architect
  - manual_to_auto_agent
  - smart_locator_agent
  - pageobject_agent
  - automation_script_agent
  - api_agent
  - code_review_agent
---

# Workflow: Chuyển Manual Test Case thành Automation Test

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc lần lượt các Skill sau:
>
> - framework_architect
> - manual_to_auto_agent
> - smart_locator_agent
> - pageobject_agent
> - automation_script_agent
> - api_agent (nếu testcase có API)
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

- Có Manual Test Case mới.
- Cần chuyển Manual sang Automation.
- Cần viết Automation từ Requirement.
- Cần generate Test Script hàng loạt.

---

# Input

AI có thể nhận một hoặc nhiều nguồn dữ liệu:

- Excel Test Case
- Jira Test Case
- Xray
- Confluence
- Word
- PDF
- Markdown
- Google Sheet

Nếu có nhiều Test Case.

↓

Chỉ xử lý những Test Case được đánh dấu:

- Automation
- Auto
- Candidate
- Regression
- Smoke

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

# Bước 2 - Phân tích Test Case

Đọc:

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

# Bước 3 - Xác định Locator

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

# Bước 4 - Generate PageUI

Sinh Locator vào:

PageUI

Không viết Locator trong Test Script.

Không viết Locator trong PageObject.

---

# Bước 5 - Generate PageObject

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

# Bước 6 - Generate API

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

# Bước 7 - Generate Test Script

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

# Bước 8 - Sinh Test Data

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

# Bước 9 - Review Code

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