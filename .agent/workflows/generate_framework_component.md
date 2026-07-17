---
description: Sinh mới hoặc cập nhật các thành phần của Automation Framework theo đúng kiến trúc và Coding Convention của dự án. AI chỉ tạo component, không sinh Test Script.
skills:
  - framework_architect
  - pageobject_agent
  - api_agent
  - code_review_agent
---

# Workflow: Generate Framework Component

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - pageobject_agent
> - api_agent
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI tạo mới hoặc cập nhật các thành phần của Automation Framework.

AI phải đảm bảo:

- Đúng kiến trúc Framework.
- Đúng Coding Convention.
- Có khả năng tái sử dụng.
- Không sinh code trùng lặp.

Workflow này KHÔNG sinh Test Script.

---

# Khi nào sử dụng

Workflow được gọi khi:

- Framework cần thêm Component mới.
- Có màn hình mới.
- Có API mới.
- Có Utility mới.
- Có Base Class mới.
- Có Listener mới.
- Có Helper mới.

---

# Input

AI có thể nhận:

- Requirement.
- Manual Testcase.
- API Document.
- Existing Framework.
- Package Structure.
- Component Name.
- User Prompt.

Ví dụ:

"Tạo PageObject cho màn Login"

"Tạo DTO cho API Login"

"Tạo Utility đọc PDF"

"Tạo BasePage"

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- CodingConvention.md
- Architecture.md

Hiểu:

- Package Structure.
- Base Classes.
- Utilities.
- Naming Convention.
- Factory Pattern.

---

# Bước 2 - Xác định Component cần tạo

AI xác định loại Component.

Ví dụ:

- PageUI
- PageObject
- API Client
- DTO
- Request
- Response
- Utility
- Listener
- Factory
- Constant
- Config
- Helper
- Enum

---

# Bước 3 - Xác định vị trí

AI phải xác định đúng Package.

Ví dụ:

PageUI

↓

pageUIs/

PageObject

↓

pageObjects/

API

↓

api/

Utility

↓

commons/

Config

↓

config/

Không tạo sai Package.

---

# Bước 4 - Sinh Component

AI sinh đầy đủ:

- Package.
- Import.
- JavaDoc (nếu có).
- Constructor.
- Method.
- Constant.

Không để TODO hoặc code rỗng nếu có đủ thông tin.

---

# Bước 5 - Tuân thủ Framework

AI phải sử dụng:

- BasePage.
- BaseApi.
- BrowserFactory.
- MobileFactory.
- ApiFactory.
- Utility hiện có.

Không tạo lại chức năng đã tồn tại.

---

# Bước 6 - Kiểm tra Duplicate

Trước khi sinh.

↓

AI tìm kiếm toàn bộ Project.

Nếu Component đã tồn tại.

↓

Không tạo mới.

↓

Đề xuất cập nhật.

---

# Bước 7 - Review Component

Kiểm tra:

- Compile.
- Import.
- Naming.
- Dependency.
- Reuse.
- Convention.

---

# Output

Workflow có thể sinh:

- PageUI.
- PageObject.
- API Client.
- DTO.
- Utility.
- Helper.
- Listener.
- Factory.
- Config.
- Enum.
- Constant.
- Base Class.

---

# Tiêu chí hoàn thành

Component phải:

✓ Đúng Package.

✓ Đúng Naming Convention.

✓ Đúng Framework.

✓ Không duplicate.

✓ Có thể tái sử dụng.

✓ Compile thành công.

---

# Không được

- Không sinh Test Script.
- Không sinh Locator nếu chưa được yêu cầu.
- Không hardcode dữ liệu.
- Không tạo Component trùng chức năng.
- Không thay đổi Business Logic.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc toàn bộ Framework.
- Phân tích Package Structure.
- Kiểm tra Component đã tồn tại.
- Xác định vị trí phù hợp.
- Sinh Component mới.
- Tự thêm Import.
- Kiểm tra Compile.
- Kiểm tra Convention.
- Sinh báo cáo kết quả.

---

# Component AI có thể tạo

## UI Layer

- PageUI
- PageObject
- Component Object
- Dialog Object
- Navigation Helper

---

## API Layer

- API Client
- Request DTO
- Response DTO
- GraphQL Client
- API Factory

---

## Common Layer

- Base Class
- Utility
- Helper
- Constant
- Config
- Logger
- Retry

---

## Data Layer

- JSON Model
- Data Factory
- Excel Reader
- Database Helper
- Faker Factory

---

## Report Layer

- Allure Helper
- Screenshot Helper
- Report Listener
- Video Recorder

---

## Integration Layer

- Jenkins Helper
- Jira Client
- Slack Notifier
- Email Reporter

---

# Quy tắc sinh Component

AI luôn ưu tiên:

1. Tái sử dụng component hiện có.
2. Kế thừa Base Class nếu phù hợp.
3. Tạo component nhỏ, một nhiệm vụ duy nhất (Single Responsibility).
4. Không viết logic nghiệp vụ trong Utility.
5. Không để code trùng lặp.
6. Đảm bảo component có thể mở rộng trong tương lai.

---

# Quy tắc đặt tên

AI phải tuân thủ Naming Convention của Framework.

Ví dụ:

LoginPageObject

LoginPageUI

LoginApi

UserDto

ExcelUtils

ConfigReader

ApiFactory

GlobalConstants

Không sử dụng tên chung chung như:

Utils1

Helper2

CommonClass

TempClass