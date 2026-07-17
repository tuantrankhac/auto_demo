# Quy Tắc Verify Database (UI/API ↔ DB)

> Áp dụng khi testcase có bước xác minh dữ liệu dưới Database (vd: "Kiểm tra dữ liệu trong DB", "verify bảng X cột Y", "record được lưu đúng...").
> Mục tiêu: AI **tự nhận biết** DB nào, bảng nào, cột nào, điều kiện nào từ mô tả testcase → sinh bước verify DB đúng Framework, **không** tự viết JDBC mới.

---

## 1. Công cụ bắt buộc dùng lại (KHÔNG viết mới)

| Thành phần | Vai trò |
|------------|---------|
| `utilities.DbConnection` | Kết nối DB + query. **Luôn dùng class này** |
| `DbConnection.connect()` | Mở kết nối — gọi ở `@BeforeClass` / `@BeforeMethod` |
| `DbConnection.disconnect()` | Đóng kết nối — gọi ở `@AfterClass` / `@AfterMethod` với `alwaysRun = true` |
| `DbConnection.getValueRecord(Map params)` | Query 1 bản ghi (LIMIT 1) → `Map<String,Object>` |
| `DbConnection.getLatestColumnValue(table, column, columnSort)` | Lấy giá trị mới nhất của 1 cột |
| `BaseTest.verifyEquals / verifyTrue / verifyContains` | So sánh kết quả (chỉ trong Test Script) |

**NGHIÊM CẤM:**

- Tự tạo `Connection` / `DriverManager` / `Statement` mới trong Test Script hoặc PageObject.
- Đặt query SQL trong PageObject (PageObject không chứa verify, không chứa DB).
- Đặt logic verify DB trong PageObject.

---

## 2. Nhận diện DB loại nào

Thứ tự xác định loại DB:

1. Đọc cấu hình hiện tại trong `utilities.DbConnection` (mặc định project: **PostgreSQL** — `org.postgresql.Driver`).
2. Nếu testcase / requirement ghi rõ DB khác (MySQL, SQL Server...) → dùng đúng loại đó (MySQL connector đã có sẵn phần comment trong `DbConnection` + `pom.xml`).
3. Không tự đổi driver mặc định của project khi testcase không yêu cầu.

Thông tin kết nối (URL, user, password) **không hardcode lại** — dùng cấu hình có sẵn trong `DbConnection` (hoặc `config.properties` nếu đã chuyển sang config).

---

## 3. Trích xuất thông tin DB từ testcase

Từ cột **Các bước** / **Kết quả mong đợi** của testcase (Excel, Jira, Markdown...), AI phải bóc tách:

| Cần lấy | Từ khoá gợi ý trong testcase | Map vào |
|---------|------------------------------|---------|
| Bảng (table) | "bảng", "table", tên danh từ nghiệp vụ (ticket → `tickets`) | `fromTable` |
| Cột (column) | "cột", "trường", "field", "status", "id"... | `selectColumns` |
| Điều kiện lọc | "với", "theo", "mã", "key", "id = ...", "của user..." | `whereCondition` + `parameters` |
| Sắp xếp / bản ghi mới nhất | "mới nhất", "gần nhất", "vừa tạo" | `orderBy` (vd: `created_at DESC`) |
| JOIN bảng khác | "kèm", "liên kết", "user tạo", nhiều bảng | `joins` |
| Giá trị mong đợi | "= ...", "phải bằng", "đúng với UI", "trạng thái ..." | tham số `verifyEquals` |

### Quy tắc suy luận tên bảng / cột

- Ưu tiên tên **đã dùng trong code hiện có** (vd `tickets`, `t.business_key`, `t.status` trong `ValidataDataOnUIAndDB`).
- Nếu testcase chỉ nói nghiệp vụ (vd "phiếu bảo hành") → ánh xạ sang bảng theo schema đã biết; **nếu chưa chắc tên bảng/cột thật → HỎI USER**, không bịa tên.
- Không đoán schema khi không có căn cứ (code cũ, requirement, hoặc USER cung cấp).

---

## 4. Mẫu tham số `getValueRecord`

```java
Map<String, Object> params = new HashMap<>();
params.put("selectColumns", Arrays.asList("t.id", "t.status"));   // cột cần lấy
params.put("fromTable", "tickets t");                              // bảng
params.put("joins", "JOIN users u ON t.created_by = u.id");       // optional
params.put("whereCondition", "t.business_key = ?");                // dùng ? cho giá trị động
params.put("parameters", Arrays.asList(businessKey));              // bind ? an toàn
params.put("orderBy", "t.created_at DESC");                        // bản ghi mới nhất

Map<String, Object> record = DbConnection.getValueRecord(params);
verifyEquals(record.get("status"), expectedStatus);
```

---

## 5. An toàn dữ liệu (bắt buộc)

- **Chỉ đọc** (`SELECT`). Không `INSERT` / `UPDATE` / `DELETE` / `DROP` trong bước verify.
- Nếu testcase cần chuẩn bị/cleanup dữ liệu bằng SQL ghi → **phải hỏi USER** và chỉ chạy trên **staging**, không production.
- Giá trị động (mã đơn, id, key) bind qua `parameters` + `?` — **không** nối chuỗi SQL để tránh SQL injection.
- Không log full connection string / password vào console hay Allure.

---

## 6. Vị trí đặt code (đúng kiến trúc)

| Việc | Đặt ở đâu |
|------|-----------|
| `connect()` / lấy dữ liệu nghiệp vụ trên UI | `@BeforeClass` / business flow qua PageObject |
| Build params + `getValueRecord()` + `verifyEquals()` | **Test Script** (`@Test`) |
| `disconnect()` | `@AfterClass` / `@AfterMethod` (`alwaysRun = true`) |
| Query SQL | Chỉ qua `DbConnection` |

---

## 7. Checklist trước khi hoàn thành bước verify DB

- [ ] Dùng `DbConnection`, không tạo JDBC mới
- [ ] Đã xác định đúng: DB loại, bảng, cột, điều kiện, orderBy
- [ ] Giá trị động bind qua `parameters` (không nối chuỗi)
- [ ] Verify đặt trong Test Script, không trong PageObject
- [ ] `connect()` / `disconnect()` đúng lifecycle, `disconnect` có `alwaysRun`
- [ ] Chỉ `SELECT`, không thao tác ghi khi chưa được phép
- [ ] Tên bảng/cột có căn cứ (code cũ / requirement / USER) — không bịa
