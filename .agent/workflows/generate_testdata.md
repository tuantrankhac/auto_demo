---
description: Tự động chuẩn bị, sinh và quản lý Test Data phục vụ Automation Test. AI ưu tiên tái sử dụng nguồn dữ liệu hiện có trước khi sinh dữ liệu mới.
skills:
  - framework_architect
  - api_agent
  - automation_script_agent
  - code_review_agent
---

# Workflow: Generate Test Data

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - api_agent
> - automation_script_agent
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI chuẩn bị Test Data phù hợp cho Automation Test.

Ưu tiên:

- Dữ liệu ổn định.
- Có thể tái sử dụng.
- Dễ bảo trì.
- Không hardcode.
- Không phụ thuộc môi trường.

---

# Khi nào sử dụng

Workflow này được gọi khi:

- Có testcase mới.
- Thiếu dữ liệu test.
- Cần tạo dữ liệu trước khi chạy Test.
- Cần Cleanup dữ liệu.
- Test Data đã hết hiệu lực.

---

# Input

AI có thể nhận:

- Manual Testcase.
- Requirement.
- API.
- Database.
- JSON.
- Excel.
- Existing Test Data.

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- CodingConvention.md
- Data Strategy.

Hiểu:

- JSON Data.
- Excel Data.
- Data Factory.
- API Factory.
- Database Utility.

---

# Bước 2 - Phân tích Testcase

Đọc:

- Preconditions.
- Test Steps.
- Expected Result.

Xác định:

- Dữ liệu bắt buộc.
- Dữ liệu có thể sinh.
- Dữ liệu cần Cleanup.

---

# Bước 3 - Chọn nguồn dữ liệu

AI ưu tiên theo thứ tự:

① API

Nếu có API tạo dữ liệu.

↓

Sử dụng API.

---

② Database

Nếu dữ liệu đã tồn tại.

↓

Đọc Database.

---

③ Existing JSON

Nếu project đã có JSON.

↓

Tái sử dụng.

---

④ Excel

Nếu testcase sử dụng Excel.

↓

Đọc Excel.

---

⑤ Factory

Nếu Framework có DataFactory.

↓

Sinh dữ liệu.

---

⑥ Faker

Chỉ sử dụng khi không còn nguồn nào khác.

---

# Bước 4 - Sinh dữ liệu

Có thể sinh:

- User.
- Customer.
- Product.
- Ticket.
- Order.
- Transaction.
- Email.
- Phone.
- Address.
- File Upload.
- JSON Payload.

Không sinh dữ liệu trùng.

---

# Bước 5 - Validate dữ liệu

Kiểm tra:

- Đúng format.
- Không duplicate.
- Đúng Business Rule.
- Có thể sử dụng.

Ví dụ:

Email.

Phone.

CCCD.

Mã khách hàng.

---

# Bước 6 - Quản lý dữ liệu

Ưu tiên:

Factory.

↓

JSON.

↓

Excel.

↓

Hardcode (không khuyến khích).

---

# Bước 7 - Cleanup

Nếu dữ liệu được tạo trong Test.

↓

Đề xuất:

API Cleanup.

Database Cleanup.

Delete bằng UI (nếu bắt buộc).

Không để dữ liệu rác.

---

# Bước 8 - Review

Kiểm tra:

- Hardcode.
- Duplicate.
- Reuse.
- Maintainability.
- Security.

---

# Output

Workflow có thể sinh:

- JSON.
- Excel.
- DTO.
- Faker Data.
- SQL.
- API Create Data.
- Cleanup Script.

---

# Tiêu chí hoàn thành

Test Data phải:

✓ Không hardcode.

✓ Có thể tái sử dụng.

✓ Đúng Business Rule.

✓ Không duplicate.

✓ Có Cleanup.

✓ Đúng Framework.

---

# Không được

- Không hardcode Username.
- Không hardcode Password.
- Không hardcode Token.
- Không tạo dữ liệu trùng.
- Không bỏ Cleanup.
- Không dùng Faker nếu đã có nguồn dữ liệu phù hợp.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc Manual Testcase.
- Phân tích dữ liệu cần thiết.
- Kiểm tra API tạo dữ liệu.
- Kiểm tra Database.
- Kiểm tra JSON.
- Kiểm tra Excel.
- Kiểm tra DataFactory.
- Chỉ sinh dữ liệu mới khi thực sự cần.
- Đề xuất Cleanup sau khi Test hoàn thành.

---

# Quy tắc ưu tiên Test Data

AI luôn ưu tiên theo thứ tự sau:

1. API tạo dữ liệu
2. Database
3. JSON có sẵn
4. Excel
5. Data Factory
6. Faker

Không sử dụng Faker nếu dữ liệu đã tồn tại hoặc có thể tạo bằng API.

---

# Quy tắc đặt dữ liệu

- Không hardcode trong Test Script.
- Không hardcode trong PageObject.
- Dữ liệu phải truyền qua Parameter hoặc đọc từ nguồn dữ liệu.
- Nếu nhiều Test dùng chung dữ liệu, ưu tiên lưu trong JSON hoặc Data Factory.

---

# Quy tắc Cleanup

Nếu Test tạo dữ liệu mới.

↓

AI phải đề xuất hoặc sinh bước Cleanup tương ứng.

Ưu tiên:

API Cleanup

↓

Database Cleanup

↓

UI Cleanup (chỉ khi không có lựa chọn khác)