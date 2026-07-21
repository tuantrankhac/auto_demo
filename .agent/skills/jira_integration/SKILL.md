---
name: jira_integration
description: Skill tích hợp Jira/Xray — lấy requirements từ Jira, xác thực Xray, và đẩy kết quả test lên Xray Cloud/Server.
---

# Jira & Xray Integration Skill

## Mô tả

Skill này cung cấp khả năng tích hợp giữa Demo Framework với hệ thống Jira và Xray để:

1. **Lấy Test Case / Steps** từ Jira qua **Jira MCP** (ưu tiên) — đầu vào cho generate automation
2. **Lấy Requirements/User Stories** từ Jira → chuyển thành tài liệu yêu cầu chuẩn
3. **Xác thực Xray** (Cloud hoặc Server/Data Center)
4. **Đẩy kết quả test** lên Xray Test Management

---

## Jira MCP (ưu tiên khi generate automation)

Server: `mcp/jira-mcp` (đăng ký trong `.cursor/mcp.json` với tên `jira`).

Config: `knowledge/config/jira.yaml`  
Secrets: `knowledge/secrets/.env` (`JIRA_API_TOKEN`)

### Tools bắt buộc khi lấy testcase đầu vào

| Tool | Khi nào dùng |
|------|----------------|
| `jira_connect` | Luôn gọi trước |
| `jira_search_issues` | Khi chưa có key — search theo defaultJQL / module |
| `jira_get_issue` | Lấy thông tin issue / story |
| `jira_get_testcase` | Lấy Test Case theo key |
| `jira_get_test_steps` | Lấy steps + expected (nguồn chính cho automation) |
| `jira_update_field` | Cập nhật field (khi được yêu cầu) |
| `jira_add_comment` | Comment kết quả / ghi chú |
| `jira_transition_issue` | Đổi status (khi được yêu cầu) |

### Luồng chuẩn trước khi generate

```
Prompt có {JiraKey} hoặc module trên Jira
        │
        ▼
jira_connect
        │
        ▼
Xác định TestCase ID
  - Có key sẵn → dùng luôn
  - Chưa có → jira_search_issues → chọn ID
        │
        ▼
jira_get_testcase(ID)
        │
        ▼
jira_get_test_steps(ID)
        │
        ▼
Mới generate Locator / PageObject / Test Script
```

**Không** bỏ qua bước lấy ID + steps khi nguồn là Jira.

**Fallback:** Excel `practices/testcases` chỉ khi USER chỉ định hoặc Jira MCP không khả dụng.

---

## Khi nào sử dụng

Agent sử dụng skill này khi user yêu cầu:

- Generate automation từ Jira Test Case / Xray
- Lấy requirement / user story từ Jira
- Kết nối / test connection đến Jira API
- Đẩy kết quả test lên Xray
- Import test results lên Jira

Trigger keywords:
- "fetch jira", "lấy testcase từ jira", "JiraKey", "CRM-123"
- "get jira ticket", "xray steps"
- "import xray", "đẩy kết quả lên xray"
- "test jira connection"

---

## Cấu trúc Scripts

```
scripts/integrations/
├── jira/
│   ├── jira_fetcher.js      # Lấy Requirement/User Story từ Jira
│   ├── xray_auth.js         # Xác thực và lấy Token Xray
│   ├── xray_importer.js     # Import kết quả test lên Xray
│   └── utils.js             # Hàm utility dùng chung
└── package.json             # Dependencies (axios, dotenv)
```

---

## Điều kiện tiên quyết (Prerequisites)

### 1. Cài đặt dependencies

```bash
cd scripts/integrations
npm install
```

### 2. Cấu hình .env

Copy `.env.example` thành `.env` ở thư mục gốc project:

```bash
cp .env.example .env
```

Điền các thông tin bắt buộc:

| Biến | Mô tả | Bắt buộc |
|------|--------|----------|
| `JIRA_BASE_URL` | URL Jira instance (VD: `https://domain.atlassian.net`) | ✅ |
| `JIRA_EMAIL` | Email tài khoản Jira (Cloud) | ✅ (Cloud) |
| `JIRA_API_TOKEN` | API Token (Cloud) | ✅ (Cloud) |
| `JIRA_PAT` | Personal Access Token (Server/DC) | ✅ (Server) |
| `JIRA_PROJECT_KEY` | Project key mặc định | Khuyến nghị |
| `XRAY_PLATFORM` | `cloud` hoặc `server` | Mặc định: cloud |
| `XRAY_CLIENT_ID` | Xray API Client ID | Khi dùng Xray Cloud |
| `XRAY_CLIENT_SECRET` | Xray API Client Secret | Khi dùng Xray Cloud |

### 3. Cách lấy Jira API Token (Cloud)

1. Đăng nhập vào [https://id.atlassian.com/manage-profile/security/api-tokens](https://id.atlassian.com/manage-profile/security/api-tokens)
2. Nhấn **Create API token**
3. Đặt label (VD: "Antigravity Automation")
4. Copy token → dán vào `JIRA_API_TOKEN` trong file `.env`

### 4. Cách lấy Xray Cloud API Key

1. Truy cập [https://app.getxray.app](https://app.getxray.app) → Settings → API Keys
2. Hoặc trong Jira: Apps → Xray → Settings → API Keys
3. Tạo API Key mới → Copy **Client ID** và **Client Secret**

---

## Hướng dẫn sử dụng

### Lấy 1 issue cụ thể

```bash
node scripts/integrations/jira/jira_fetcher.js --issue PROJ-123
```

### Lấy issues theo project

```bash
node scripts/integrations/jira/jira_fetcher.js --project PROJ --type Story --max 20
```

### Tìm theo JQL

```bash
node scripts/integrations/jira/jira_fetcher.js --jql "project = PROJ AND status = 'To Do'"
```

### Xuất thành Markdown requirement

```bash
node scripts/integrations/jira/jira_fetcher.js --issue PROJ-123 --format md
```

### Lấy children của Epic

```bash
node scripts/integrations/jira/jira_fetcher.js --epic PROJ-10 --format md
```

### Test Xray authentication

```bash
node scripts/integrations/jira/xray_auth.js
node scripts/integrations/jira/xray_auth.js --verify
```

### Import kết quả Playwright lên Xray

```bash
node scripts/integrations/jira/xray_importer.js --format playwright --file ./test-results.json --project PROJ
```

### Import JUnit XML lên Xray

```bash
node scripts/integrations/jira/xray_importer.js --format junit --file ./junit-results.xml --project PROJ
```

---

## Workflow liên quan

| Workflow | Mô tả |
|----------|--------|
| `/generate_testscript` | Lấy TC từ Jira MCP rồi sinh Test Script |
| `/manual_to_auto` | Module automation — bắt đầu bằng Jira Key / search |
| `/generate_db_verification` | Lấy steps từ Jira rồi sinh verify DB |
| `/fetch_jira_requirements` | Lấy requirements từ Jira ticket và lưu thành file |
| `/import_test_results_xray` | Đẩy kết quả test lên Xray |

---

## Lưu ý quan trọng

- **Bảo mật**: KHÔNG bao giờ commit file `.env` lên Git. File `.gitignore` đã được cấu hình bỏ qua `.env`.
- **Rate Limiting**: Jira Cloud có giới hạn API calls. Script đã hỗ trợ phân trang (pagination) để tránh vượt limit.
- **Atlassian Document Format (ADF)**: Jira Cloud sử dụng ADF cho description. Script tự động chuyển đổi ADF → plain text.
- **Test Key Convention**: Khi import Playwright results, nên đặt test key trong title: `test('[PROJ-123] Login should work', ...)` để Xray mapping đúng test case.

---

## Troubleshooting

| Lỗi | Nguyên nhân | Giải pháp |
|------|-------------|-----------|
| HTTP 401 | Token/password sai | Kiểm tra JIRA_API_TOKEN hoặc JIRA_PAT |
| HTTP 403 | Không có quyền | Kiểm tra permission trên Jira project |
| HTTP 404 | URL sai hoặc issue không tồn tại | Kiểm tra JIRA_BASE_URL và issue key |
| `ENOTFOUND` | DNS không resolve | Kiểm tra JIRA_BASE_URL có đúng domain không |
| `ECONNREFUSED` | Server không chạy | Kiểm tra Jira Server có online không |
| File .env not found | Chưa tạo .env | Copy `.env.example` → `.env` |