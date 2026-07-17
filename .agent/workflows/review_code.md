---
description: Review toàn bộ Automation Code theo Coding Convention, Framework Convention và Automation Best Practices. Xác định lỗi, đề xuất cải tiến và đảm bảo code sẵn sàng để chạy và commit.
skills:
  - framework_architect
  - code_review_agent
---

# Workflow: Review Code

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI review toàn bộ Automation Code trước khi chạy hoặc commit.

Review không chỉ tập trung vào cú pháp mà còn đánh giá:

- Framework.
- Kiến trúc.
- Coding Convention.
- Khả năng tái sử dụng.
- Khả năng bảo trì.
- Hiệu năng.
- Best Practice.

---

# Khi nào sử dụng

Workflow này được gọi khi:

- AI vừa generate code.
- Developer vừa sửa code.
- Chuẩn bị commit.
- Chuẩn bị tạo Pull Request.
- Sau khi fix bug.
- Sau khi refactor.

---

# Input

AI có thể nhận:

- Source Code.
- Git Diff.
- Pull Request.
- Entire Project.
- Package.
- Một hoặc nhiều Class.

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- Architecture.md
- CodingConvention.md

Hiểu:

- BaseTest.
- BasePage.
- BaseApi.
- BrowserFactory.
- MobileFactory.
- ApiFactory.
- Utility.
- Reporting.

Không review nếu chưa hiểu Framework.

---

# Bước 2 - Kiểm tra Compile

Kiểm tra:

- Import.
- Syntax.
- Compile Error.
- Missing Dependency.
- Unused Import.
- Unused Variable.

Nếu phát hiện lỗi.

↓

Đề xuất sửa.

---

# Bước 3 - Review Framework Convention

Kiểm tra:

Code có đúng Framework không.

Ví dụ:

✓ Có sử dụng BasePage.

✓ Có sử dụng BaseApi.

✓ Có sử dụng PageGenerator.

✓ Có sử dụng ApiFactory.

Không được:

driver.findElement()

new ChromeDriver()

new AppiumDriver()

RestAssured trực tiếp trong Test Script.

---

# Bước 4 - Review Coding Convention

Kiểm tra:

- Naming.
- Package.
- Method.
- Variable.
- Constant.
- Comment.

Đảm bảo đúng Coding Convention của dự án.

---

# Bước 5 - Review Locator

Kiểm tra:

Locator có:

- Duplicate.
- Dynamic.
- Absolute XPath.
- Index XPath.
- Hardcode.

Nếu có Locator tốt hơn.

↓

Đề xuất thay thế.

---

# Bước 6 - Review PageObject

Kiểm tra:

PageObject chỉ chứa:

Business Method.

Không chứa:

- Assert.
- Verify.
- Test Data.
- Driver trực tiếp.
- Locator.

Nếu phát hiện.

↓

Đề xuất Refactor.

---

# Bước 7 - Review Test Script

Kiểm tra:

Test Script chỉ:

Setup.

↓

Business Flow.

↓

Verify.

↓

Cleanup.

Không được:

driver.findElement()

click()

sendKeys()

Locator.

Business Logic.

---

# Bước 8 - Review API

Kiểm tra:

- Có dùng BaseApi.
- Có duplicate Request.
- Có hardcode URL.
- Có hardcode Token.
- Có hardcode Header.

Nếu phát hiện.

↓

Đề xuất sửa.

---

# Bước 9 - Review Test Data

Kiểm tra:

- Hardcode Username.
- Hardcode Password.
- Hardcode Email.
- Hardcode OTP.
- Hardcode SQL.

Ưu tiên:

- JSON.
- Excel.
- Factory.
- API.

---

# Bước 10 - Review Wait Strategy

Kiểm tra:

Có:

Thread.sleep()

Implicit Wait lạm dụng.

Explicit Wait sai.

Wait dư thừa.

Flaky Wait.

Ưu tiên:

Explicit Wait.

Common Wait của BasePage.

---

# Bước 11 - Review Duplicate Code

Kiểm tra:

- Duplicate Method.
- Duplicate Locator.
- Duplicate API.
- Duplicate Utility.

Nếu có.

↓

Đề xuất tái sử dụng.

---

# Bước 12 - Review Maintainability

Kiểm tra:

- Class quá lớn.
- Method quá dài.
- Dependency quá nhiều.
- Độ đọc hiểu.
- Khả năng mở rộng.

Nếu cần.

↓

Đề xuất Refactor.

---

# Bước 13 - Review Performance

Kiểm tra:

- Wait dư.
- Mở Browser nhiều lần.
- API gọi lặp.
- Query Database lặp.
- Khởi tạo Object không cần thiết.

Đề xuất tối ưu.

---

# Bước 14 - Review Security

Kiểm tra:

Không được:

- Hardcode Password.
- Hardcode Token.
- Hardcode API Key.
- Hardcode Secret.

Ưu tiên:

config.properties.

Environment Variable.

Secret Manager.

---

# Bước 15 - Tổng hợp kết quả

AI phân loại các vấn đề theo mức độ:

## Critical

Ảnh hưởng đến việc chạy Test.

Ví dụ:

- Compile Error.
- Locator sai.
- API sai.
- NullPointer.

---

## High

Có thể gây Flaky Test.

Ví dụ:

- Wait chưa đúng.
- Dynamic Locator.
- Hardcode dữ liệu.

---

## Medium

Ảnh hưởng Maintainability.

Ví dụ:

- Duplicate.
- Naming.
- Method dài.

---

## Low

Khuyến nghị cải tiến.

Ví dụ:

- Format.
- Comment.
- Readability.

---

# Output

Workflow có thể sinh:

- Code Review Report.
- Danh sách lỗi.
- Danh sách cảnh báo.
- Danh sách cải tiến.
- Refactor Recommendation.
- File Diff (nếu AI tự sửa).

---

# Tiêu chí hoàn thành

Code phải:

✓ Compile thành công.

✓ Đúng Framework.

✓ Đúng Coding Convention.

✓ Không duplicate.

✓ Không hardcode.

✓ Không Locator trong Test Script.

✓ Không Business Logic trong Test Script.

✓ Có khả năng tái sử dụng.

✓ Có khả năng bảo trì.

---

# Không được

- Không sửa Business Logic.
- Không sửa Requirement.
- Không sửa Expected Result.
- Không thay đổi Flow nghiệp vụ.
- Không bỏ qua Coding Convention.
- Không tự ý tối ưu nếu có nguy cơ thay đổi hành vi của Test.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc toàn bộ Source Code.
- Đọc Framework.
- Đọc Git Diff.
- Phân tích phạm vi thay đổi.
- Review theo từng tầng (Framework → PageObject → API → Test Script → Utility).
- Đề xuất hoặc tự sửa các lỗi an toàn.
- Sinh Code Review Report.

Nếu phát hiện lỗi liên quan:

- Locator → Chuyển sang **Generate Locator Workflow**.
- PageObject → Chuyển sang **Generate PageObject Workflow**.
- API → Chuyển sang **Generate API Script Workflow**.
- Test Script → Chuyển sang **Generate Test Script Workflow**.
- Flaky Test → Chuyển sang **Debug Failed Test Workflow**.

---

# Quy tắc Review

AI luôn review theo thứ tự sau:

1. Compile
2. Framework
3. Coding Convention
4. Architecture
5. Locator
6. PageObject
7. API
8. Test Script
9. Test Data
10. Wait Strategy
11. Duplicate Code
12. Maintainability
13. Performance
14. Security

Chỉ khi tầng trước đạt yêu cầu mới chuyển sang tầng tiếp theo.