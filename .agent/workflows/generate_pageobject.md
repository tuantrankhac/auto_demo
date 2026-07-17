---
description: Tự động sinh PageObject theo đúng kiến trúc Page Object Model (POM), tái sử dụng BasePage và Coding Convention của Framework.
skills:
  - framework_architect
  - pageobject_agent
  - code_review_agent
---

# Workflow: Generate PageObject

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - pageobject_agent
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI sinh PageObject theo đúng Framework của dự án.

PageObject phải:

- Chỉ chứa Business Action.
- Không chứa Locator.
- Không chứa Assert.
- Không chứa Verify.
- Không chứa Test Data.
- Có khả năng tái sử dụng.

---

# Khi nào sử dụng

Workflow này được gọi khi:

- Có màn hình mới.
- Có thêm chức năng mới.
- Locator đã được tạo.
- Cần bổ sung Business Method.

---

# Input

AI có thể nhận:

- PageUI.
- Manual Testcase.
- Requirement.
- Business Flow.
- Existing PageObject.

Nếu đã tồn tại PageObject.

↓

Ưu tiên cập nhật.

Không tạo mới nếu không cần.

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- CodingConvention.md
- Architecture.md

Hiểu:

- BasePage.
- PageGenerator.
- Common Method.
- Utility.
- Wait Strategy.

Không generate nếu chưa hiểu Framework.

---

# Bước 2 - Kiểm tra PageObject / PageUI (tên đúng + tên tương đồng)

Trước khi tạo mới, **bắt buộc** kiểm tra:

1. Class đúng tên đã tồn tại chưa (`LoginPage`, `LoginPO`, `LoginPageUI`...).
2. Class **tên tương đồng** đã tồn tại chưa (vd: `LoginPage` ↔ `DangNhapPO`, `LoginPageUI` ↔ `DangNhapPageUI`).

Cách nhận diện tương đồng: cùng URL/module, cùng nghiệp vụ, dịch EN↔VI, khác hậu tố `Page`/`PO`/`Screen`.

### Quyết định

| Kết quả so sánh | Hành động |
|-----------------|-----------|
| Giống nhau (cùng màn hình / cùng nghiệp vụ) | **Mở rộng** file hiện có — bổ sung method/locator còn thiếu. Không tạo class mới. |
| Không giống nhau | **Tạo mới** theo tên yêu cầu / Coding Convention. |
| Không chắc | **Hỏi USER** trước khi tạo mới. |

Ví dụ: đã có `DangNhapPO` + `DangNhapPageUI` cho Login → yêu cầu `LoginPage` → mở rộng `DangNhap*`, không sinh `LoginPage` song song.

Chi tiết: `.agent/skills/pageobject_agent/SKILL.md` mục 2.

---

# Bước 3 - Đọc PageUI

Đọc Locator từ **PageUI đã chọn ở Bước 2** (file đúng tên hoặc tương đồng đã quyết định mở rộng):

- LoginPageUI / DangNhapPageUI
- HomePageUI / TrangChuPageUI
- ...

Không tạo Locator mới trong workflow này.

Không tạo PageUI trùng nghiệp vụ.

Nếu thiếu Locator.

↓

Chuyển sang Workflow Generate Locator — yêu cầu **mở rộng PageUI hiện có**.

---

# Bước 4 - Phân tích Business Action

Đọc Manual Testcase.

↓

Xác định Business Action.

Ví dụ:

- Login.
- Logout.
- Search Customer.
- Create Order.
- Update Profile.
- Approve.
- Reject.

Mỗi Business Action tương ứng với một hoặc nhiều Method.

---

# Bước 5 - Generate Method

Sinh các Method theo Business Action.

Ví dụ:

login()

logout()

searchCustomer()

createCustomer()

deleteCustomer()

approveOrder()

rejectOrder()

Method phải thể hiện đúng nghiệp vụ.

Không đặt tên theo thao tác Selenium.

Sai:

clickLoginButton()

Đúng:

login()

---

# Bước 6 - Tận dụng BasePage

Chỉ sử dụng các Common Method của BasePage.

Ví dụ:

clickToElement()

sendkeyToElement()

selectItem()

waitForElementVisible()

getElementText()

Không sử dụng:

driver.findElement()

driver.click()

driver.sendKeys()

Nếu BasePage chưa có Method phù hợp.

↓

Đề xuất bổ sung BasePage.

Không viết Selenium trực tiếp trong PageObject nếu có thể tái sử dụng.

---

# Bước 7 - Thiết kế Method

Method nên:

- Ngắn gọn.
- Dễ đọc.
- Một nhiệm vụ duy nhất.
- Có thể tái sử dụng.

Nếu Method quá dài.

↓

Tách nhỏ.

---

# Bước 8 - Không đưa Verify vào PageObject

Không viết:

Assert

verifyEquals

verifyTrue

verifyFalse

Expected Result

Các bước Verify chỉ được thực hiện tại Test Script.

---

# Bước 9 - Không đưa Test Data vào PageObject

Không hardcode:

Username.

Password.

Email.

Phone.

OTP.

URL.

Token.

Method chỉ nhận dữ liệu qua Parameter.

Ví dụ:

login(String username, String password)

Không:

login()

{

username="admin"

password="123"

}

---

# Bước 10 - Review

Kiểm tra:

- Compile.
- Duplicate.
- Coding Convention.
- Reusability.
- Readability.
- Maintainability.

Nếu có Method trùng.

↓

Đề xuất tái sử dụng.

---

# Output

Workflow có thể sinh:

- PageObject.
- Business Method.
- Helper Method.
- Constructor.
- Review Summary.

---

# Tiêu chí hoàn thành

PageObject phải:

✓ Chỉ chứa Business Action.

✓ Không chứa Locator.

✓ Không chứa Verify.

✓ Không chứa Assert.

✓ Không hardcode dữ liệu.

✓ Tận dụng BasePage.

✓ Dễ bảo trì.

✓ Có khả năng tái sử dụng.

---

# Không được

- Không generate Locator.
- Không viết Locator trong PageObject.
- Không dùng driver.findElement().
- Không dùng driver.click().
- Không dùng Thread.sleep().
- Không Verify trong PageObject.
- Không Assert trong PageObject.
- Không hardcode dữ liệu.
- Không duplicate Method.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc Framework.
- Đọc PageUI.
- Đọc Manual Testcase.
- Phân tích Business Flow.
- Kiểm tra PageObject hiện có.
- Tái sử dụng Method nếu đã tồn tại.
- Sinh Business Method mới nếu cần.
- Review toàn bộ PageObject trước khi trả kết quả.

Nếu thiếu Locator.

↓

Tự động chuyển sang Workflow Generate Locator trước khi tiếp tục.