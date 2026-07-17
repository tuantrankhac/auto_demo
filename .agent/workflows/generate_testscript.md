---
description: Tự động sinh Automation Test Script theo đúng Framework, sử dụng PageObject, API và Utility hiện có. Test Script chỉ đóng vai trò điều phối luồng kiểm thử (orchestration), không chứa Business Logic.
skills:
  - framework_architect
  - automation_script_agent
  - pageobject_agent
  - api_agent
  - code_review_agent
---

# Workflow: Generate Test Script

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - automation_script_agent
> - pageobject_agent
> - api_agent (nếu testcase có API)
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

- Có Manual Testcase.
- Có Requirement mới.
- PageObject đã hoàn thành.
- API Script đã hoàn thành.
- Cần generate Automation Test.

---

# Input

AI có thể nhận:

- Manual Testcase.
- Requirement.
- Business Flow.
- PageObject.
- API Script.
- Test Data.
- Existing Test Script.

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

# Bước 2 - Phân tích Testcase

Đọc:

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

# Bước 3 - Kiểm tra thành phần đã có

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

# Bước 4 - Generate Test Flow

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

# Bước 5 - Gọi PageObject

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

# Bước 6 - Gọi API (nếu có)

Nếu testcase yêu cầu API.

↓

Sử dụng API Class đã có.

Ví dụ:

loginApi.login()

ticketApi.createTicket()

orderApi.createOrder()

Không viết request trực tiếp trong Test Script.

---

# Bước 7 - Verify

Verify chỉ thực hiện tại Test Script.

Ví dụ:

verifyTrue()

verifyFalse()

verifyEquals()

verifyContains()

Assert.

Không Verify trong PageObject.

---

# Bước 8 - Xử lý Test Data

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

# Bước 9 - Exception Handling

Không tự ý try-catch toàn bộ Test Script.

Chỉ xử lý Exception khi:

- Có retry.
- Có cleanup.
- Có logging.

Ưu tiên để Framework xử lý lỗi.

---

# Bước 10 - Review

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