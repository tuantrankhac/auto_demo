---
description: Tự động sinh API Automation Script theo đúng kiến trúc BaseApi của Framework, tái sử dụng ApiFactory và Coding Convention.
skills:
  - framework_architect
  - api_agent
  - code_review_agent
---

# Workflow: Generate API Script

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - api_agent
> - code_review_agent

---

# Mục tiêu

Workflow này giúp AI sinh API Automation theo đúng Framework của dự án.

API Script phải:

- Tái sử dụng BaseApi.
- Tái sử dụng ApiFactory.
- Không duplicate request.
- Dễ mở rộng.
- Dễ bảo trì.

---

# Khi nào sử dụng

Workflow này được gọi khi:

- Có API mới.
- Có testcase API.
- Có API được sử dụng để chuẩn bị dữ liệu.
- Có API Verify.
- Có API Cleanup.

---

# Input

AI có thể nhận:

- Swagger/OpenAPI.
- Postman Collection.
- Curl.
- API Documentation.
- GraphQL Schema.
- Existing API Class.
- Manual Testcase.

Nếu API Class đã tồn tại.

↓

Ưu tiên tái sử dụng.

Không tạo mới nếu không cần.

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- Architecture.md
- CodingConvention.md

Hiểu:

- BaseApi.
- ApiFactory.
- Authentication.
- Reporting.
- Verify Method.

Không generate nếu chưa hiểu Framework.

---

# Bước 2 - Phân tích API

Đọc:

- Endpoint.
- HTTP Method.
- Header.
- Query Param.
- Path Param.
- Request Body.
- Response.
- Authentication.

Xác định:

- GET
- POST
- PUT
- PATCH
- DELETE
- GraphQL

---

# Bước 3 - Kiểm tra API đã tồn tại

Kiểm tra:

- ApiFactory.
- Existing API Class.
- BaseApi.

Nếu API đã tồn tại.

↓

Tái sử dụng.

Không generate lại.

---

# Bước 4 - Generate API Class

Tạo API Class theo đúng Convention.

Ví dụ:

LoginApi

CustomerApi

OrderApi

TicketApi

GraphQLApi

Không viết toàn bộ request trong Test Script.

---

# Bước 5 - Tận dụng BaseApi

Chỉ sử dụng các method có sẵn.

Ví dụ:

get()

post()

put()

delete()

setAuthToken()

setAdditionalHeaders()

verifyStatusCode()

extractValueFromResponse()

Không viết lại các method đã có trong BaseApi.

---

# Bước 6 - Generate Business API Method

Method phải thể hiện nghiệp vụ.

Ví dụ:

login()

createCustomer()

deleteCustomer()

createOrder()

approveOrder()

Không đặt tên:

postLogin()

callCreateCustomer()

executeApi()

---

# Bước 7 - Request Body

Nếu Request Body lớn.

↓

Tách thành:

DTO

POJO

Builder

JSON Template

Không hardcode JSON trong Test Script.

---

# Bước 8 - Response

Nếu Response cần dùng nhiều lần.

↓

Sinh DTO.

Hoặc

Response Model.

Không parse JSON thủ công nếu Framework đã hỗ trợ.

---

# Bước 9 - Authentication

Ưu tiên sử dụng:

Bearer Token

OAuth

Basic Auth

Session

JWT

Theo đúng Framework.

Không hardcode Token.

Không hardcode Cookie.

---

# Bước 10 - Review

Kiểm tra:

- Compile.
- Duplicate.
- Naming Convention.
- Endpoint.
- Authentication.
- Header.
- Reuse.
- Readability.

Nếu phát hiện API đã tồn tại.

↓

Đề xuất tái sử dụng.

---

# Output

Workflow có thể sinh:

- API Class.
- DTO.
- Request Model.
- Response Model.
- JSON Payload.
- Review Summary.

---

# Tiêu chí hoàn thành

API Script phải:

✓ Tái sử dụng BaseApi.

✓ Không duplicate request.

✓ Không hardcode URL.

✓ Không hardcode Token.

✓ Đúng Coding Convention.

✓ Có khả năng tái sử dụng.

✓ Có thể gọi từ Test Script.

---

# Không được

- Không viết RestAssured trực tiếp trong Test Script.
- Không hardcode Base URL.
- Không hardcode Token.
- Không duplicate Request.
- Không viết Verify trong API Class nếu Framework đã hỗ trợ.
- Không bỏ qua BaseApi.
- Không tạo request mới nếu đã có API tương tự.

---

# AI Agent Mode

Nếu project đã tích hợp AI Agent.

↓

AI sẽ tự động:

- Đọc Swagger/OpenAPI hoặc Postman Collection.
- Đọc Framework.
- Kiểm tra API Class hiện có.
- Tái sử dụng nếu API đã tồn tại.
- Sinh API Class mới nếu cần.
- Sinh DTO/Request/Response Model.
- Cập nhật ApiFactory.
- Review toàn bộ API trước khi trả kết quả.

Nếu API đã tồn tại.

↓

Không generate lại.

Nếu thiếu Authentication.

↓

Yêu cầu User cung cấp hoặc đọc từ file cấu hình của Framework.