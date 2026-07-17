---
description: Phân tích và tái cấu trúc Automation Framework nhằm nâng cao khả năng mở rộng, tái sử dụng, bảo trì và hiệu năng. Không thay đổi Business Logic của hệ thống.
skills:
  - framework_architect
  - code_review_agent
  - pageobject_agent
  - api_agent
---

# Workflow: Refactor Framework

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - code_review_agent
> - pageobject_agent
> - api_agent

---

# Mục tiêu

Workflow này giúp AI phân tích toàn bộ Framework Automation và đề xuất hoặc tự động refactor các thành phần nhằm:

- Giảm duplicate code.
- Tăng khả năng tái sử dụng.
- Tăng khả năng mở rộng.
- Đơn giản hóa cấu trúc Framework.
- Cải thiện Maintainability.
- Cải thiện Performance.

Workflow này KHÔNG nhằm sửa bug.

---

# Khi nào sử dụng

Workflow được gọi khi:

- Framework ngày càng lớn.
- Có nhiều đoạn code trùng lặp.
- Chuẩn bị release lớn.
- Chuẩn bị migrate framework.
- Sau nhiều sprint phát triển.
- Trước khi xây dựng AI Agent.

---

# Input

AI có thể đọc:

- Toàn bộ Source Code.
- Framework.md.
- CodingConvention.md.
- Architecture.md.
- Package Structure.
- Git History.
- Pull Request.
- Existing Framework.

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- Architecture.md
- CodingConvention.md

Hiểu:

- BasePage
- BaseTest
- BrowserFactory
- MobileFactory
- BaseApi
- ApiFactory
- Utilities
- Reporting
- Driver Management

---

# Bước 2 - Phân tích cấu trúc Project

Đánh giá:

- Package Structure.
- Module Structure.
- Layer.
- Dependency.

Kiểm tra:

Có đúng kiến trúc nhiều tầng hay không.

Ví dụ:

UI

↓

PageUI

↓

PageObject

↓

Business Layer

↓

Test Script

---

# Bước 3 - Phân tích Duplicate

Kiểm tra:

Duplicate:

- Method.
- Locator.
- API.
- Utility.
- Wait.
- Verify.
- Common Action.

Nếu phát hiện.

↓

Đề xuất gom về Base hoặc Utility.

---

# Bước 4 - Phân tích Base Classes

Kiểm tra:

BasePage.

BaseTest.

BaseApi.

BrowserFactory.

MobileFactory.

Đề xuất:

Method nào nên đưa lên Base.

Method nào không nên nằm trong Base.

Không làm Base quá lớn.

---

# Bước 5 - Phân tích Page Object

Kiểm tra:

PageObject có:

- Business Logic.
- Assert.
- Driver.
- Locator.
- Duplicate Method.

Đề xuất:

Tách nhỏ.

Tái sử dụng.

Chuẩn hóa.

---

# Bước 6 - Phân tích API Layer

Kiểm tra:

- BaseApi.
- ApiFactory.
- DTO.
- Request.
- Response.

Đề xuất:

Tái sử dụng Request.

Không duplicate API.

---

# Bước 7 - Phân tích Utility

Kiểm tra:

Utility nào:

- Trùng chức năng.
- Không còn sử dụng.
- Có thể gom.

Ví dụ:

StringUtils.

DateUtils.

JsonUtils.

ExcelUtils.

DbUtils.

---

# Bước 8 - Phân tích Wait Strategy

Kiểm tra:

- Thread.sleep()
- Duplicate Wait.
- Hardcode Timeout.
- Wait không cần thiết.

Đề xuất:

Chuẩn hóa về BasePage.

---

# Bước 9 - Phân tích Test Data

Kiểm tra:

- Hardcode.
- JSON.
- Excel.
- Factory.
- API.

Đề xuất:

Chuẩn hóa chiến lược quản lý dữ liệu.

---

# Bước 10 - Phân tích Driver Management

Kiểm tra:

- ThreadLocal.
- Driver Lifecycle.
- BrowserFactory.
- MobileFactory.

Đề xuất:

Đảm bảo hỗ trợ:

- Parallel.
- Web.
- Mobile.

---

# Bước 11 - Phân tích Reporting

Kiểm tra:

- Allure.
- Screenshot.
- Video.
- Log.

Đề xuất:

Chuẩn hóa Report.

Không duplicate.

---

# Bước 12 - Phân tích Performance

Kiểm tra:

- Object khởi tạo nhiều lần.
- Driver khởi tạo nhiều lần.
- API gọi dư.
- DB Query dư.
- Wait dư.

Đề xuất tối ưu.

---

# Bước 13 - Phân tích khả năng mở rộng

Đánh giá Framework có dễ mở rộng cho:

✓ Web

✓ Mobile

✓ API

✓ Database

✓ AI Agent

✓ Parallel

✓ CI/CD

Nếu chưa.

↓

Đề xuất kiến trúc mới.

---

# Bước 14 - Refactor

Nếu an toàn.

↓

AI có thể:

- Di chuyển Method.
- Đổi Package.
- Tách Utility.
- Tách Base.
- Chuẩn hóa Naming.
- Xóa Duplicate.

Không thay đổi Business Logic.

---

# Bước 15 - Review

Sau khi Refactor.

↓

Kiểm tra:

- Compile.
- Dependency.
- Import.
- Convention.
- Regression Risk.

---

# Output

Workflow có thể sinh:

- Refactor Report.
- Kiến trúc đề xuất.
- Package Structure mới.
- Danh sách Duplicate.
- Danh sách Utility nên tạo.
- Danh sách Method nên chuyển.
- Refactor Diff.

---

# Tiêu chí hoàn thành

Framework phải:

✓ Dễ mở rộng.

✓ Dễ bảo trì.

✓ Không duplicate.

✓ Không hardcode.

✓ Đúng Convention.

✓ Hỗ trợ Parallel.

✓ Hỗ trợ AI Agent.

✓ Có khả năng tái sử dụng cao.

---

# Không được

- Không thay đổi Business Logic.
- Không sửa Requirement.
- Không sửa Test Data.
- Không đổi luồng nghiệp vụ.
- Không đổi API Contract.
- Không xóa class đang được sử dụng.
- Không refactor nếu có nguy cơ ảnh hưởng Regression mà chưa xác minh.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc toàn bộ Framework.
- Phân tích Package Structure.
- Phân tích Dependency.
- Phân tích Duplicate.
- Phân tích Utility.
- Phân tích Base Classes.
- Phân tích Driver Management.
- Phân tích API Layer.
- Phân tích Reporting.
- Đề xuất hoặc tự Refactor nếu an toàn.
- Sinh Refactor Report.

---

# Quy tắc Refactor

AI luôn Refactor theo thứ tự:

1. Package Structure
2. Base Classes
3. Utilities
4. API Layer
5. PageObject
6. Test Script
7. Test Data
8. Reporting
9. Driver Management
10. Performance

Không Refactor ngẫu nhiên.

---

# Quy tắc nâng cấp Framework

Khi Framework phát triển, AI phải ưu tiên:

- Tăng khả năng tái sử dụng.
- Giảm sự phụ thuộc giữa các module.
- Giảm số lượng class quá lớn (>1000 dòng nếu có thể tách hợp lý).
- Chuẩn hóa Coding Convention.
- Hỗ trợ AI Agent dễ dàng đọc và generate code.
- Chuẩn bị sẵn cho CI/CD và Parallel Execution.