---
description: Phân tích Requirement, User Story hoặc Tài liệu nghiệp vụ để xác định phạm vi kiểm thử, Business Rule, Test Scope, Risk và chuẩn bị đầu vào cho Manual Test hoặc Automation Test.
skills:
  - framework_architect
  - manual_to_auto_agent
  - api_agent
  - code_review_agent
---

# Workflow: Analyze Requirement

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - manual_to_auto_agent
> - api_agent
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI phân tích Requirement để hiểu đầy đủ nghiệp vụ trước khi sinh Test Case hoặc Automation.

AI phải hiểu:

- Chức năng.
- Luồng nghiệp vụ.
- Business Rule.
- Điều kiện đầu vào.
- Điều kiện đầu ra.
- Các trường hợp ngoại lệ.
- Phạm vi ảnh hưởng.

Không generate Testcase khi chưa hiểu Requirement.

---

# Khi nào sử dụng

Workflow này được gọi khi:

- Có Requirement mới.
- Có User Story mới.
- Có BRD.
- Có SRS.
- Có Confluence.
- Có Jira Story.
- Có Mockup.
- Có Figma.
- Có API Document.

---

# Input

AI có thể nhận:

- Requirement Document.
- User Story.
- BRD.
- SRS.
- Jira.
- Confluence.
- Figma.
- API Document.
- Screenshot.
- Flow Diagram.

Có thể kết hợp nhiều nguồn.

---

# Bước 1 - Hiểu Requirement

Đọc toàn bộ tài liệu.

Không chỉ đọc phần mô tả.

Đọc:

- Title.
- Description.
- Acceptance Criteria.
- Business Flow.
- Rule.
- Exception.
- Validation.

---

# Bước 2 - Xác định Business Flow

AI phải xác định:

Luồng chính.

↓

Luồng phụ.

↓

Luồng ngoại lệ.

↓

Luồng lỗi.

Ví dụ:

Login

↓

Sai Password

↓

Account Lock

↓

Forgot Password

---

# Bước 3 - Xác định Business Rule

Trích xuất tất cả Rule.

Ví dụ:

- Password tối thiểu 8 ký tự.
- Email phải duy nhất.
- User chỉ được tạo tối đa 5 Ticket.
- Admin mới được Approve.

Không bỏ sót Rule.

---

# Bước 4 - Xác định dữ liệu

Phân tích:

Input.

Output.

Mandatory Field.

Optional Field.

Boundary.

Validation.

Dependency.

---

# Bước 5 - Xác định thành phần hệ thống

AI xác định:

UI.

↓

API.

↓

Database.

↓

Background Job.

↓

Notification.

↓

Scheduler.

↓

Third-party.

---

# Bước 6 - Xác định phạm vi kiểm thử

AI phân loại:

Functional.

Regression.

Integration.

API.

UI.

Mobile.

Performance.

Security.

Compatibility.

Không phải mọi Requirement đều cần Automation.

---

# Bước 7 - Đánh giá khả năng Automation

AI xác định:

Có nên Automation không.

Nếu có.

↓

Ưu tiên:

API

↓

UI

↓

Mobile

Không Automation:

Captcha.

OTP thủ công.

Luồng chỉ chạy một lần.

Tác vụ phụ thuộc phần cứng đặc biệt.

---

# Bước 8 - Xác định Test Data

AI xác định:

- Dữ liệu cần tạo.
- Dữ liệu cần Cleanup.
- API hỗ trợ.
- Database liên quan.

---

# Bước 9 - Phân tích Risk

Xác định:

Business Risk.

Technical Risk.

Automation Risk.

Flaky Risk.

Dependency.

Regression Impact.

---

# Bước 10 - Xác định phạm vi ảnh hưởng

Requirement ảnh hưởng:

Module nào.

API nào.

Page nào.

Database nào.

Automation nào.

Regression nào.

---

# Bước 11 - Đề xuất Test Case

AI chỉ sinh danh sách.

Ví dụ:

TC01

TC02

TC03

...

Không generate Automation ngay.

---

# Bước 12 - Review

Kiểm tra:

Có bỏ sót Business Rule không.

Có thiếu Exception không.

Có thiếu Boundary không.

Có thiếu Validation không.

Có thiếu Negative Case không.

---

# Output

Workflow có thể sinh:

- Requirement Summary.
- Business Flow.
- Business Rule.
- Validation Rule.
- Test Scope.
- Risk Analysis.
- Automation Recommendation.
- Danh sách Test Case đề xuất.

---

# Tiêu chí hoàn thành

AI phải:

✓ Hiểu đầy đủ Requirement.

✓ Xác định đầy đủ Business Rule.

✓ Xác định Test Scope.

✓ Xác định Risk.

✓ Đề xuất Automation hợp lý.

✓ Không bỏ sót Validation.

---

# Không được

- Không generate Automation Script ngay.
- Không generate Locator.
- Không generate PageObject.
- Không bỏ qua Business Rule.
- Không bỏ qua Exception.
- Không bỏ qua Boundary Value.
- Không bỏ qua Validation Rule.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc Requirement từ Confluence hoặc Jira.
- Phân tích Business Flow.
- Trích xuất Business Rule.
- Xác định Test Scope.
- Phân tích Risk.
- Đánh giá khả năng Automation.
- Đề xuất Test Data.
- Sinh danh sách Test Case.
- Chuyển sang Workflow **Manual to Auto** khi được yêu cầu.

---

# Quy tắc phân tích Requirement

AI luôn phân tích theo thứ tự:

1. Mục tiêu nghiệp vụ.
2. Business Flow.
3. Business Rule.
4. Validation Rule.
5. Input / Output.
6. Exception Flow.
7. Test Scope.
8. Test Data.
9. Risk.
10. Automation Feasibility.

Không được bỏ qua bất kỳ bước nào.

---

# Quy tắc đánh giá Automation

AI chỉ đề xuất Automation khi:

- Chức năng ổn định.
- Có thể chạy lặp lại.
- Không phụ thuộc thao tác thủ công.
- Có thể xác minh kết quả tự động.

Nếu không phù hợp.

↓

Giải thích rõ lý do không nên Automation.