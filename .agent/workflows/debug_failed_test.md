---
description: Phân tích Automation Test bị fail, xác định nguyên nhân gốc (Root Cause), đề xuất hoặc tự sửa lỗi nếu an toàn, sau đó review và báo cáo kết quả.
skills:
  - framework_architect
  - code_review_agent
  - smart_locator_agent
  - pageobject_agent
  - automation_script_agent
  - api_agent
---

# Workflow: Debug Failed Test

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - code_review_agent
> - smart_locator_agent
> - pageobject_agent
> - automation_script_agent
> - api_agent (nếu testcase có API)

---

# Mục tiêu

Workflow này giúp AI tự động:

- Phân tích Test bị Fail.
- Xác định Root Cause.
- Xác định vị trí gây lỗi.
- Đề xuất cách sửa.
- Nếu an toàn, tự sửa.
- Review lại code sau khi sửa.

Không chỉ đọc StackTrace.

---

# Khi nào sử dụng

Workflow này được gọi khi:

- Automation Test Fail.
- Build Jenkins Fail.
- Pipeline Fail.
- Retry vẫn Fail.
- Locator Fail.
- API Fail.
- Assertion Fail.

---

# Input

AI có thể nhận:

- StackTrace.
- Console Log.
- TestNG Report.
- Allure Report.
- Screenshot.
- Video.
- HTML Source.
- XML Source.
- Request / Response API.
- Source Code.
- Git Diff.

Nếu có nhiều nguồn.

↓

Phân tích tất cả.

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- Architecture.md
- CodingConvention.md

Hiểu:

- BasePage.
- BaseTest.
- BaseApi.
- Retry.
- Wait Strategy.
- Reporting.

Không sửa nếu chưa hiểu Framework.

---

# Bước 2 - Phân tích lỗi

Đọc:

- Exception.
- StackTrace.
- Screenshot.
- Video.
- Allure Step.
- Console.

Xác định:

- Test Fail ở Step nào.
- Action nào.
- Method nào.
- Class nào.

---

# Bước 3 - Xác định Root Cause

AI phải xác định nguyên nhân thật sự.

Ví dụ:

Locator thay đổi.

↓

Element chưa xuất hiện.

↓

Sai dữ liệu.

↓

API lỗi.

↓

Environment lỗi.

↓

Timing Issue.

↓

Popup.

↓

Permission.

↓

Business thay đổi.

Không chỉ đọc dòng Exception cuối cùng.

---

# Bước 4 - Phân loại lỗi

## Locator

Ví dụ:

NoSuchElementException

StaleElementReferenceException

InvalidSelectorException

↓

Chuyển sang Smart Locator Agent.

---

## Wait

Ví dụ:

TimeoutException

↓

Đánh giá:

- Wait sai.
- Điều kiện wait sai.
- Element load chậm.

---

## Assertion

Ví dụ:

AssertionError

↓

Kiểm tra:

- Expected.
- Actual.
- Business Logic.

Không tự sửa Assertion nếu chưa xác nhận nghiệp vụ.

---

## API

Ví dụ:

400

401

403

404

500

↓

Kiểm tra:

- Endpoint.
- Header.
- Token.
- Payload.
- Environment.

---

## Environment

Ví dụ:

Database.

Server.

Network.

VPN.

SSL.

↓

Không sửa code.

Chỉ báo cáo.

---

## Test Data

Ví dụ:

Không có dữ liệu.

Duplicate.

Expired.

↓

Đề xuất tạo lại dữ liệu.

---

# Bước 5 - Xác định phạm vi ảnh hưởng

Kiểm tra:

Có bao nhiêu Test bị ảnh hưởng.

Locator dùng ở bao nhiêu Page.

Method dùng ở bao nhiêu Test.

API dùng ở bao nhiêu Flow.

Nếu sửa.

↓

Có ảnh hưởng Regression không.

---

# Bước 6 - Tự sửa (nếu an toàn)

Cho phép tự sửa:

✓ Locator.

✓ Wait.

✓ Refactor.

✓ Import.

✓ Naming.

✓ Duplicate.

✓ BasePage.

Không tự sửa:

Business Logic.

Requirement.

Expected Result.

Database.

API Contract.

---

# Bước 7 - Review

Sau khi sửa.

↓

Kiểm tra:

Compile.

Import.

Convention.

Reuse.

Maintainability.

---

# Bước 8 - Đề xuất chạy lại

Nếu AI đã sửa.

↓

Đề xuất:

Run lại Test.

Hoặc:

Run Regression liên quan.

Không kết luận Pass nếu chưa chạy lại.

---

# Output

Workflow có thể sinh:

- Root Cause Analysis.
- Danh sách lỗi.
- Vị trí lỗi.
- File đã sửa.
- Diff Code.
- Danh sách Test bị ảnh hưởng.
- Đề xuất tiếp theo.

---

# Tiêu chí hoàn thành

AI phải:

✓ Xác định đúng Root Cause.

✓ Không chỉ đọc StackTrace.

✓ Không sửa Business Logic.

✓ Chỉ sửa khi an toàn.

✓ Báo cáo đầy đủ.

✓ Review sau khi sửa.

---

# Không được

- Không sửa Requirement.
- Không sửa Expected Result.
- Không hardcode Locator mới.
- Không tăng Timeout vô lý.
- Không thêm Thread.sleep().
- Không bỏ qua Assert.
- Không sửa khi chưa xác định Root Cause.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc log.
- Đọc Allure Report.
- Đọc Screenshot.
- Đọc Video.
- Đọc Source Code.
- Xác định Root Cause.
- Kiểm tra Locator.
- Kiểm tra API.
- Kiểm tra Framework.
- Đề xuất hoặc tự sửa nếu an toàn.
- Review lại code.
- Đề xuất chạy lại Test.

Nếu lỗi liên quan Locator.

↓

Tự động chuyển sang **Generate Locator Workflow**.

Nếu lỗi liên quan PageObject.

↓

Tự động chuyển sang **Generate PageObject Workflow**.

Nếu lỗi liên quan API.

↓

Tự động chuyển sang **Generate API Script Workflow**.

Nếu lỗi liên quan Test Script.

↓

Tự động chuyển sang **Generate Test Script Workflow**.

---

# Quy tắc tự sửa lỗi

AI chỉ được phép tự sửa khi:

- Có thể xác định rõ nguyên nhân.
- Không làm thay đổi Business Logic.
- Không thay đổi Expected Result.
- Không làm ảnh hưởng đến các Testcase khác.

Nếu không đáp ứng các điều kiện trên.

↓

AI chỉ phân tích và đề xuất phương án xử lý, chờ User xác nhận trước khi sửa.

---

# Ưu tiên xử lý lỗi

Khi có nhiều lỗi cùng lúc, AI xử lý theo thứ tự ưu tiên:

1. Environment (Server, DB, Network)
2. API
3. Test Data
4. Locator
5. Wait
6. Assertion
7. Coding Convention
8. Refactor

Không sửa các lỗi ở mức thấp nếu nguyên nhân gốc nằm ở mức cao hơn.