---
description: Thực thi toàn bộ quy trình Automation Test từ chuẩn bị môi trường, chạy test, phân tích kết quả, xử lý lỗi, sinh báo cáo và đề xuất cải tiến.
skills:
  - framework_architect
  - automation_script_agent
  - mobile_agent
  - api_agent
  - code_review_agent
---

# Workflow: Run Full Automation

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - automation_script_agent
> - mobile_agent
> - api_agent
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI thực hiện toàn bộ quy trình Automation Test End-to-End.

AI đóng vai trò như một QA Automation Engineer:

- Chuẩn bị môi trường.
- Kiểm tra Framework.
- Chuẩn bị dữ liệu.
- Chạy Automation.
- Phân tích kết quả.
- Đề xuất hoặc tự sửa lỗi nếu an toàn.
- Sinh báo cáo cuối cùng.

---

# Khi nào sử dụng

Workflow được gọi khi:

- Chạy Regression.
- Chạy Smoke Test.
- Chạy Sanity Test.
- Chạy toàn bộ Automation.
- Trước khi Release.
- Sau khi Merge Code.
- Sau khi Deploy môi trường mới.

---

# Input

AI có thể nhận:

- Module cần chạy.
- Test Suite.
- TestNG XML.
- Maven Profile.
- Browser.
- Device.
- Environment.
- Danh sách Test Case.

Ví dụ:

Run Login Regression.

Run Full Regression.

Run API Regression.

Run Mobile Smoke.

---

# Bước 1 - Kiểm tra Framework

Đọc:

Framework.md

Kiểm tra:

✓ Compile.

✓ Dependency.

✓ Maven.

✓ Java Version.

✓ Driver.

✓ Appium.

Nếu có lỗi.

↓

Dừng.

↓

Thông báo.

---

# Bước 2 - Kiểm tra Environment

Kiểm tra:

- URL.
- Browser.
- Device.
- Appium Server.
- API Server.
- Database.
- Config.

Nếu thiếu.

↓

Thông báo.

Không tự đoán.

---

# Bước 3 - Chuẩn bị Test Data

AI gọi:

Generate Test Data Workflow.

Ưu tiên:

API

↓

Database

↓

JSON

↓

Excel

↓

Factory

↓

Faker

---

# Bước 4 - Build Project

Thực hiện:

mvn clean compile

Kiểm tra:

Compile Error.

Dependency Error.

Import Error.

Nếu lỗi.

↓

Dừng.

---

# Bước 5 - Thực thi Test

Ví dụ:

mvn clean test

hoặc

mvn test -Pweb

hoặc

mvn test -Pmobile

hoặc

runTestCase.xml

Theo đúng Framework.

---

# Bước 6 - Theo dõi quá trình chạy

Trong quá trình chạy.

AI theo dõi:

- Console.
- Maven Log.
- Allure Result.
- Screenshot.
- Video.
- API Log.

---

# Bước 7 - Phân tích Fail

Nếu Test Fail.

↓

AI xác định:

Compile.

↓

Environment.

↓

Locator.

↓

Wait.

↓

API.

↓

Database.

↓

Business.

↓

Framework.

Không đoán.

Phải có bằng chứng.

---

# Bước 8 - Retry

Nếu Framework có Retry.

↓

Retry theo cấu hình.

Không Retry vô hạn.

---

# Bước 9 - Đề xuất Heal

Nếu lỗi:

Locator.

↓

Gọi Generate Locator Workflow.

Nếu lỗi:

PageObject.

↓

Gọi Generate PageObject Workflow.

Nếu lỗi:

API.

↓

Gọi Generate API Workflow.

Nếu lỗi:

Test Script.

↓

Gọi Generate Test Script Workflow.

Nếu lỗi:

Framework.

↓

Gọi Refactor Framework Workflow.

---

# Bước 10 - Review Code

Sau khi AI sửa.

↓

Gọi Review Code Workflow.

Đảm bảo:

Compile.

Convention.

Reuse.

---

# Bước 11 - Sinh Report

Thu thập:

- Pass.
- Fail.
- Skip.
- Retry.

Đính kèm:

- Screenshot.
- Video.
- Log.
- StackTrace.
- API Log.

---

# Bước 12 - Tổng hợp kết quả

AI sinh:

## Test Summary

- Total.
- Passed.
- Failed.
- Skipped.
- Retry.

---

## Root Cause Summary

Ví dụ:

5 lỗi Locator.

2 lỗi API.

1 lỗi Environment.

---

## Recommendation

Ví dụ:

- Refactor Locator.
- Tăng Explicit Wait.
- Dùng API tạo dữ liệu.
- Cập nhật Test Data.

---

# Output

Workflow có thể sinh:

- Allure Report.
- Extent Report.
- Test Summary.
- Root Cause Report.
- Failed Test List.
- Retry Summary.
- Improvement Report.

---

# Tiêu chí hoàn thành

AI phải:

✓ Build thành công.

✓ Chạy Automation.

✓ Phân tích toàn bộ lỗi.

✓ Sinh báo cáo.

✓ Không bỏ sót Failed Test.

✓ Đề xuất cải tiến.

---

# Không được

- Không bỏ qua Failed Test.
- Không sửa Business Logic.
- Không Retry vô hạn.
- Không thay đổi Requirement.
- Không Commit Code.
- Không Push Code.
- Không Merge Pull Request.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc Framework.
- Kiểm tra Environment.
- Chuẩn bị Test Data.
- Build Project.
- Chạy Automation.
- Theo dõi Console.
- Theo dõi Allure.
- Theo dõi Screenshot.
- Phân tích Fail.
- Gọi Workflow phù hợp để xử lý.
- Review Code sau khi sửa.
- Sinh Report cuối cùng.

---

# Thứ tự thực thi

AI luôn thực hiện theo trình tự:

1. Analyze Requirement (nếu có Requirement mới)
2. Generate Test Data
3. Generate Locator (nếu cần)
4. Generate PageObject (nếu cần)
5. Generate API Script (nếu cần)
6. Generate Test Script
7. Build Project
8. Run Automation
9. Analyze Failed Tests
10. Review Code
11. Refactor Framework (nếu cần)
12. Generate Report

Không bỏ qua bước nào nếu dữ liệu đầu vào yêu cầu.

---

# Quy tắc xử lý lỗi

AI luôn ưu tiên kiểm tra theo thứ tự:

1. Compile Error
2. Environment
3. Configuration
4. Test Data
5. Locator
6. Wait Strategy
7. API
8. Database
9. Framework
10. Business Logic

Chỉ chuyển sang bước tiếp theo khi đã loại trừ nguyên nhân ở bước trước.

---

# Báo cáo cuối cùng

Sau khi hoàn thành, AI phải trả về:

## Kết quả

- Tổng số test chạy.
- Số lượng Pass.
- Số lượng Fail.
- Số lượng Skip.
- Thời gian thực thi.

## Nguyên nhân lỗi

Phân loại:

- Framework.
- Locator.
- API.
- Test Data.
- Environment.
- Business Logic.

## Hành động đã thực hiện

- Retry.
- Generate Locator.
- Refactor PageObject.
- Sinh lại Test Data.
- Review Code.

## Khuyến nghị

Đề xuất cải thiện Framework hoặc Test Suite nếu cần.