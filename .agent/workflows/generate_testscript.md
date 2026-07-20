---
description: Tự động sinh Automation Test Script theo đúng Framework, sử dụng PageObject, API và Utility hiện có. Test Script chỉ đóng vai trò điều phối luồng kiểm thử (orchestration), không chứa Business Logic.
skills:
  - framework_architect
  - jira_integration
  - automation_script_agent
  - pageobject_agent
  - api_agent
  - db_verification_agent
  - code_review_agent
---

# Workflow: Generate Test Script

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - jira_integration (khi có Jira Key / lấy TC từ Jira)
> - automation_script_agent
> - pageobject_agent
> - api_agent (nếu testcase có API)
> - db_verification_agent (nếu testcase có verify DB)
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI sinh Test Script theo đúng Framework.

Test Script phải:

- Dễ đọc.
- Dễ bảo trì.
- Chỉ điều phối luồng kiểm thử.
- Không chứa Selenium code.
- Không chứa Locator.
- Không chứa Business Logic.

---

# Khi nào sử dụng

Workflow này được gọi khi:

- Có Manual Testcase (Jira Key hoặc Excel).
- Có Requirement mới.
- PageObject đã hoàn thành.
- API Script đã hoàn thành.
- Cần generate Automation Test.

---

# Input

AI có thể nhận:

- **Jira / Xray Test Key** (ưu tiên) — vd: `CRM-123`
- Manual Testcase (Excel / Markdown) — fallback
- Requirement
- Business Flow
- PageObject
- API Script
- Test Data
- Existing Test Script

Thứ tự ưu tiên nguồn testcase:

```
Jira Key (Jira MCP)
        │
        ▼
Excel / practices/testcases
        │
        ▼
Markdown / text USER cung cấp
```

Nếu Test Script đã tồn tại.

↓

Ưu tiên cập nhật.

Không tạo mới nếu không cần.

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- Architecture.md
- CodingConvention.md

Hiểu:

- BaseTest.
- BasePage.
- PageGenerator.
- ApiFactory.
- Utility.
- Reporting.
- Retry.

Không generate nếu chưa hiểu Framework.

---

# Bước 2 - Lấy Testcase từ Jira MCP (bắt buộc nếu có Jira Key)

Khi prompt / USER cung cấp `{JiraKey}` / `{TestCaseID}` dạng Jira key (vd: `CRM-123`), hoặc yêu cầu lấy TC từ Jira:

```
jira_connect
        │
        ▼
Xác định TestCase ID (Jira Key)
        │
        ▼
jira_get_testcase(testKey)
        │
        ▼
jira_get_test_steps(testKey)
        │
        ▼
(Phân tích steps + expected → sang Bước 3)
```

### Chi tiết

1. Gọi `jira_connect` (đọc `knowledge/config/jira.yaml` + `knowledge/secrets/.env`).
2. Nếu USER chỉ đưa module / JQL → dùng `jira_search_issues` (defaultJQL hoặc JQL USER) để lấy danh sách key, **xác nhận / chọn ID** trước khi generate.
3. Gọi `jira_get_testcase({JiraKey})` và `jira_get_test_steps({JiraKey})`.
4. Từ steps + expected result → xác định flow automation.

### Fallback

Nếu **không** có Jira / MCP lỗi / USER chỉ định Excel:

↓

Đọc `practices/testcases` như trước.

**Không** generate Test Script khi chưa có nội dung testcase (Jira hoặc local).

---

# Bước 3 - Phân tích Testcase

Đọc (từ Jira steps hoặc Excel):

- Preconditions.
- Test Steps.
- Expected Result.
- Test Data.

Xác định:

- Luồng nghiệp vụ.
- Điều kiện chuẩn bị.
- Điều kiện xác minh.
- Điều kiện dọn dẹp.

---

# Bước 4 - Kiểm tra thành phần đã có

Kiểm tra:

- PageObject.
- API.
- Utility.
- Common Method.

Nếu đã tồn tại.

↓

Tái sử dụng.

Không generate lại.

---

# Bước 5 - Generate Test Flow

Test Script chỉ thực hiện:

Setup

↓

Business Flow

↓

Verify

↓

Cleanup

Ví dụ:

Open Login Page

↓

Login

↓

Search Customer

↓

Verify Result

↓

Logout

Không triển khai Business Logic trong Test Script.

---

# Bước 6 - Gọi PageObject

Chỉ gọi các Business Method.

Ví dụ:

loginPage.login()

customerPage.searchCustomer()

orderPage.createOrder()

Không gọi:

click()

sendKeys()

findElement()

wait()

driver...

---

# Bước 7 - Gọi API (nếu có)

Nếu testcase yêu cầu API.

↓

Sử dụng API Class đã có.

Ví dụ:

loginApi.login()

ticketApi.createTicket()

orderApi.createOrder()

Không viết request trực tiếp trong Test Script.

---

# Bước 8 - Verify

Verify chỉ thực hiện tại Test Script.

Ví dụ:

verifyTrue()

verifyFalse()

verifyEquals()

verifyContains()

Assert.

Không Verify trong PageObject.

## Bước 8.1 - Verify Database (nếu testcase yêu cầu)

Nếu testcase có bước kiểm tra dữ liệu dưới DB ("Kiểm tra dữ liệu trong DB", "verify bảng ... cột ...", "UI khớp DB"...).

↓

Chuyển sang Workflow `generate_db_verification` (skill `db_verification_agent`).

↓

Tự nhận biết DB / bảng / cột / điều kiện từ testcase.

↓

Dùng `utilities.DbConnection.getValueRecord(...)` — không tạo JDBC mới.

↓

`connect()` ở `@BeforeClass/@BeforeMethod`, `disconnect()` ở teardown (`alwaysRun = true`).

Chi tiết: `.agent/rules/database_verification_rules.md`.

---

# Bước 9 - Xử lý Test Data

Sử dụng:

- Factory.
- JSON.
- Excel.
- Random Data.
- SQL.
- API Response.

Không hardcode.

Ví dụ sai:

login("admin","123")

Ví dụ đúng:

login(user.getUsername(), user.getPassword())

---

# Bước 10 - Exception Handling

Không tự ý try-catch toàn bộ Test Script.

Chỉ xử lý Exception khi:

- Có retry.
- Có cleanup.
- Có logging.

Ưu tiên để Framework xử lý lỗi.

---

# Bước 11 - Review

Kiểm tra:

- Compile.
- Import.
- Duplicate.
- Hardcode.
- Naming Convention.
- Readability.
- Maintainability.
- Reuse.

Nếu phát hiện lỗi.

↓

Đề xuất sửa.

---

# Output

Workflow có thể sinh:

- Test Script.
- Test Class.
- Test Method.
- Review Summary.

---

# Tiêu chí hoàn thành

Test Script phải:

✓ Chỉ điều phối luồng kiểm thử.

✓ Không chứa Selenium code.

✓ Không chứa Locator.

✓ Không chứa Business Logic.

✓ Tận dụng PageObject.

✓ Tận dụng API.

✓ Không hardcode.

✓ Có khả năng tái sử dụng.

✓ Đúng Coding Convention.

---

# Không được

- Không generate Locator.
- Không viết Locator trong Test Script.
- Không gọi driver.findElement().
- Không gọi click().
- Không gọi sendKeys().
- Không viết XPath.
- Không viết CSS Selector.
- Không viết Business Logic.
- Không Verify trong PageObject.
- Không hardcode dữ liệu.
- Không duplicate code.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc Manual Testcase.
- Đọc Framework.
- Đọc PageObject.
- Đọc API.
- Đọc Test Data.
- Kiểm tra Test Script hiện có.
- Tái sử dụng tối đa các thành phần sẵn có.
- Chỉ sinh phần còn thiếu.
- Review toàn bộ Test Script trước khi trả kết quả.

Nếu thiếu PageObject.

↓

Tự động chuyển sang Workflow **Generate PageObject**.

Nếu thiếu Locator.

↓

Tự động chuyển sang Workflow **Generate Locator**.

Nếu thiếu API.

↓

Tự động chuyển sang Workflow **Generate API Script**.