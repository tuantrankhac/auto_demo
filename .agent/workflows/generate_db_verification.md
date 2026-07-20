---
description: Sinh bước verify Database cho Automation Test khi testcase yêu cầu so sánh dữ liệu UI/API với DB. AI tự nhận biết DB, bảng, cột, điều kiện từ mô tả testcase và tái sử dụng DbConnection.
skills:
  - framework_architect
  - jira_integration
  - db_verification_agent
  - automation_script_agent
  - code_review_agent
---

# Workflow: Generate DB Verification

> **BẮT BUỘC (MANDATORY SKILLS):**
>
> Trước khi bắt đầu, AI PHẢI đọc:
>
> - framework_architect
> - jira_integration (khi lấy TC từ Jira)
> - db_verification_agent
> - automation_script_agent
> - code_review_agent
>
> Và rule: `.agent/rules/database_verification_rules.md`

---

# Mục tiêu

Workflow này giúp AI sinh **bước xác minh dữ liệu dưới Database** khi testcase có yêu cầu verify DB.

AI phải:

- Tự nhận biết DB loại, bảng, cột, điều kiện từ testcase.
- Tái sử dụng `utilities.DbConnection`.
- Đặt verify đúng chỗ (Test Script).
- An toàn dữ liệu (chỉ SELECT, bind tham số).

---

# Khi nào sử dụng

Gọi khi testcase / bước có dấu hiệu verify DB:

- "Kiểm tra dữ liệu trong DB".
- "Verify bảng ... cột ...".
- "Record lưu đúng vào database".
- "UI khớp với DB".
- Testcase UI + DB hoặc API + DB.

Workflow này thường được gọi **bên trong** `generate_testscript` / `manual_to_auto`, không đứng riêng.

---

# Input

- **Jira Key** (ưu tiên) — lấy steps qua Jira MCP
- Manual Testcase (Excel / Markdown) — fallback
- Requirement
- Test Data
- Source code hiện có (schema đã dùng: bảng, cột)
- `utilities.DbConnection`

---

# Bước 0 - Lấy Testcase từ Jira (nếu có Jira Key)

Nếu prompt/USER có `{JiraKey}`:

```
jira_connect → jira_get_testcase → jira_get_test_steps
```

Dùng steps/expected từ Jira để nhận biết bước verify DB.

Nếu không có Jira → đọc `practices/testcases` / nội dung USER cung cấp.

---

# Bước 1 - Hiểu Framework

Đọc:

- Framework.md
- `utilities.DbConnection`
- Test mẫu `demo.web.testScripts.ValidataDataOnUIAndDB`

Hiểu:

- `connect()` / `disconnect()`
- `getValueRecord(Map)`
- `getLatestColumnValue(...)`
- verify helpers của `BaseTest`

Không generate nếu chưa hiểu `DbConnection`.

---

# Bước 2 - Xác định có bước verify DB

Đọc testcase.

Nếu **không** có bước DB → dừng, không tự thêm.

Nếu **có** → sang Bước 3.

---

# Bước 3 - Nhận diện thông tin DB (quan trọng)

Bóc tách từ mô tả testcase:

| Cần lấy | Map vào |
|---------|---------|
| DB loại (mặc định PostgreSQL) | cấu hình `DbConnection` |
| Bảng | `fromTable` |
| Cột | `selectColumns` |
| Điều kiện lọc (key/id/user) | `whereCondition` + `parameters` |
| Bản ghi mới nhất / theo mã | `orderBy` |
| JOIN nhiều bảng | `joins` |
| Giá trị mong đợi | tham số `verifyEquals` |

Nếu chưa rõ tên bảng/cột thật:

1. Tìm trong code hiện có.
2. Tìm trong requirement.
3. Không chắc → **HỎI USER** (không bịa schema).

---

# Bước 4 - Kiểm tra thành phần đã có

Kiểm tra:

- `DbConnection` đã có method phù hợp chưa (thường `getValueRecord` là đủ).

Nếu đủ → tái sử dụng.

Không tạo JDBC mới. Không thêm method DB mới nếu không cần.

---

# Bước 5 - Sinh code

Đặt code đúng lifecycle:

```
@BeforeClass / @BeforeMethod
   └── DbConnection.connect()   (+ business flow lấy dữ liệu UI/API)

@Test
   └── build params → DbConnection.getValueRecord(params) → verifyEquals(...)

@AfterClass / @AfterMethod (alwaysRun = true)
   └── DbConnection.disconnect()
```

Mẫu:

```java
Map<String, Object> params = new HashMap<>();
params.put("selectColumns", Arrays.asList("t.status"));
params.put("fromTable", "tickets t");
params.put("whereCondition", "t.business_key = ?");
params.put("parameters", Arrays.asList(businessKey));
params.put("orderBy", "t.created_at DESC");

Map<String, Object> record = DbConnection.getValueRecord(params);
verifyEquals(record.get("status"), expectedStatus);
```

---

# Bước 6 - An toàn dữ liệu

- Chỉ `SELECT`.
- Bind giá trị động qua `parameters` + `?`.
- Không `INSERT/UPDATE/DELETE/DROP` khi verify.
- Nếu cần chuẩn bị/cleanup dữ liệu bằng SQL ghi → hỏi USER, chỉ staging.
- Không log connection string / password.

---

# Bước 7 - Review

Kiểm tra theo `.agent/rules/database_verification_rules.md` mục 7:

- [ ] Dùng `DbConnection`, không JDBC mới
- [ ] Đúng DB / bảng / cột / điều kiện / orderBy
- [ ] Bind `parameters`, không nối chuỗi SQL
- [ ] Verify trong Test Script, không trong PageObject
- [ ] `connect()` / `disconnect()` đúng, `disconnect` có `alwaysRun`
- [ ] Chỉ SELECT
- [ ] Tên bảng/cột có căn cứ, không bịa

---

# Output

- Bước verify DB trong Test Script.
- (Nếu cần) đề xuất bổ sung config DB vào `config.properties`.

---

# Không được

- Không tạo kết nối JDBC mới.
- Không đặt SQL/verify trong PageObject.
- Không thao tác ghi DB khi verify.
- Không hardcode connection.
- Không bịa tên bảng/cột.
