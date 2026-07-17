# Demo Framework — Automation + AI Agent

Bộ **Automation Framework** đa kênh (Web UI, Mobile, API) trên Java 17, Selenium 4, Appium, Rest Assured, TestNG và Allure.

Ngoài source automation, project tích hợp **AI Agent** để hỗ trợ QA/Developer generate, review, debug và refactor theo đúng kiến trúc & convention của framework.

---

## Mục tiêu AI trong project

AI hoạt động như **Senior Automation Engineer**:

- Sinh code đúng nghiệp vụ, đúng Framework, đúng Coding Convention
- Ưu tiên tái sử dụng — kiểm tra class tồn tại / tên tương đồng trước khi tạo mới
- Giảm duplicate locator, action, test data
- Không hardcode URL, token, credential

Thứ tự ưu tiên khi có xung đột:

1. **USER Request**
2. **`RULE_GLOBAL.md`**
3. **`AGENTS.md`**
4. **`docs/`**
5. **`.agent/`** (rules, skills, workflows)
6. **Source code hiện có**

---

## Kiến trúc phần AI

```
USER Prompt / prompt-templates
        │
        ▼
   AGENTS.md          ← Vai trò, phạm vi, quy trình tổng
        │
        ▼
 RULE_GLOBAL.md       ← Quy tắc bắt buộc
        │
        ▼
 .agent/workflows     ← Quy trình từng loại việc
        │
        ▼
 .agent/skills        ← Chuyên gia theo từng lớp (Locator, PO, API…)
        │
        ▼
 .agent/rules         ← Chiến lược locator / Selenium / Appium / Playwright
        │
        ▼
 docs/ + source code  ← Framework thật để generate đúng
```

---

## Thành phần AI và vai trò

### 1. Tài liệu điều khiển Agent (gốc project)

| File | Vai trò |
|------|---------|
| [`AGENTS.md`](AGENTS.md) | Mô tả **vai trò AI**, công việc được phép làm, quy trình generate (PageUI → PO → Test), template Web/Mobile/API |
| [`RULE_GLOBAL.md`](RULE_GLOBAL.md) | **Quy tắc bắt buộc**: ưu tiên nguồn truth, không sửa core tùy tiện, locator chỉ trong PageUI, không commit khi chưa được yêu cầu |

### 2. `.agent/` — Bộ não vận hành

| Thư mục | Vai trò |
|---------|---------|
| [`.agent/workflows/`](.agent/workflows/) | **Quy trình end-to-end** cho từng loại yêu cầu (sinh locator, PO, test, review, debug…) |
| [`.agent/skills/`](.agent/skills/) | **Skill chuyên môn** — mỗi agent một trách nhiệm rõ (không làm chồng chéo) |
| [`.agent/rules/`](.agent/rules/) | **Quy tắc kỹ thuật** chi tiết cho locator và từng tool (Selenium / Appium / Playwright) |

#### Workflows (`.agent/workflows/`)

| Workflow | Vai trò |
|----------|---------|
| `analyze_requirement.md` | Phân tích requirement / website → phạm vi test, business rule |
| `generate_locator.md` | Đọc DOM / UI hierarchy → sinh / mở rộng PageUI |
| `generate_pageobject.md` | Sinh / mở rộng PageObject từ PageUI (không Verify) |
| `generate_testscript.md` | Sinh Test Script orchestration theo BaseTest |
| `manual_to_auto.md` | Chuyển Manual TC → Automation cả module |
| `generate_testdata.md` | Chuẩn bị / sinh test data |
| `generate_api_script.md` | Sinh API client theo BaseApi + ApiFactory |
| `generate_framework_component.md` | Sinh / cập nhật component framework (không sinh test) |
| `review_code.md` | Review convention, duplicate, maintainability |
| `debug_failed_test.md` | Phân tích fail, root cause, sửa an toàn |
| `refactor_framework.md` | Refactor giảm duplicate, tăng tái sử dụng |
| `run_full_automation.md` | Chạy full pipeline: env → test → báo cáo |

#### Skills (`.agent/skills/`)

| Skill | Vai trò |
|-------|---------|
| `framework_architect` | Hiểu kiến trúc Framework trước khi agent khác generate |
| `smart_locator_agent` | Sinh locator ổn định; kiểm tra PageUI tên đúng / tương đồng |
| `pageobject_agent` | Sinh / mở rộng PageObject; không Locator, không Verify |
| `automation_script_agent` | Sinh Test Script từ PO + TC đã có |
| `manual_to_auto_agent` | Điều phối chuyển Manual → Auto |
| `mobile_agent` | Automation Mobile (Appium); **không** tự sinh locator |
| `api_agent` | API client / API test theo BaseApi |
| `code_review_agent` | Review chất lượng trước khi bàn giao |
| `ui_debug_agent` | Hỗ trợ debug UI (DOM, screenshot, log) |
| `jira_integration` | Tích hợp đọc/ghi thông tin Jira (khi được cấu hình) |

#### Rules (`.agent/rules/`)

| Rule | Vai trò |
|------|---------|
| `locator_strategy.md` | Bản đồ ưu tiên locator dùng chung mọi framework |
| `selenium_rules.md` | Quy tắc locator / wait cho Selenium (Web) |
| `appium_rules.md` | Quy tắc locator / gesture cho Appium (Mobile) |
| `playwright_rules.md` | Quy tắc locator semantic cho Playwright / Browser MCP |
| `automation_rules.md` | Best practice automation tổng quát |

### 3. `prompt-templates/` — Prompt mẫu gọi Agent

| Thành phần | Vai trò |
|------------|---------|
| [`prompt-templates/`](prompt-templates/) | Prompt sẵn dùng — copy, thay placeholder, gửi cho Agent |
| [`prompt-templates/README.md`](prompt-templates/README.md) | Danh sách 12 prompt + workflow slash tương ứng |

Ví dụ luồng thường dùng:

```
Requirements → Testcases → Locator → PageObject → Test Script → Review
     (01)         (02)       (03)        (04)          (05/06)     (07)
```

Chi tiết từng file: xem [prompt-templates/README.md](prompt-templates/README.md).

### 4. `docs/` — Kiến thức Framework (AI bắt buộc đọc)

| File | Vai trò với AI |
|------|----------------|
| [`docs/Framework.md`](docs/Framework.md) | Component core, utility, lệnh chạy |
| [`docs/Architecture.md`](docs/Architecture.md) | Kiến trúc, design pattern, luồng Web/Mobile/API |
| [`docs/CodingConvention.md`](docs/CodingConvention.md) | Naming, POM, locator, import |
| [`docs/AutomationProcess.md`](docs/AutomationProcess.md) | Quy trình tạo test, checklist |
| [`docs/Environment.md`](docs/Environment.md) | JDK, Maven, Appium, troubleshooting |

### 5. `practices/` — Đầu vào nghiệp vụ cho AI

| Thư mục | Vai trò |
|---------|---------|
| `practices/requirements/` | Requirement module (vd: `Login.md`) |
| `practices/testcases/` | Manual testcase Excel (cột Automation = Yes/No) |
| `practices/testdata/` | Dữ liệu test (vd: `user.json`) |

AI ưu tiên đọc từ đây khi generate module / testcase / testdata.

### 6. MCP / Browser Agent (tuỳ cấu hình)

| Thành phần | Vai trò |
|------------|---------|
| [`.cursor/mcp.json`](.cursor/mcp.json) | Cấu hình MCP (Filesystem, Playwright…) để Agent mở URL, đọc DOM |
| ADB + uiautomator | Dump UI hierarchy Android khi generate mobile locator |

---

## Cách dùng nhanh

1. Chọn prompt trong [`prompt-templates/`](prompt-templates/).
2. Thay `{URL}`, `{ModuleName}`, `{TestCaseID}`…
3. Gửi vào chat Cursor Agent.
4. Agent đọc `RULE_GLOBAL` → `AGENTS` → workflow/skill → `docs/` → source → generate.

Hoặc mô tả tự do; Agent vẫn phải tuân thủ cùng thứ tự ưu tiên ở trên.

---

## Phạm vi Framework (ngắn)

```
src/main/java/   → BasePage, BrowserFactory, MobileFactory, BaseApi, utilities
src/test/java/
  commons/       → BaseTest, PageGenerator, ApiFactory
  demo/web/      → pageUIs, pageObjects, testScripts
  demo/mobile/   → pageUIs, pageObjects, testScripts
  demo/api/      → API clients
src/test/resources/
  runTestCase.xml, config.properties, devices.json
```

---

## Nguyên tắc quan trọng khi làm việc với AI

- **Không** tạo PageUI / PageObject trùng nghiệp vụ nếu đã có tên đúng hoặc tên tương đồng (vd: `LoginPage` ↔ `DangNhapPO`) — phải mở rộng file cũ.
- Locator **chỉ** nằm trong `*PageUI`; PageObject **không** chứa Assert/Verify/Test data.
- **Không** sửa file core (`BasePage`, `BrowserFactory`…) trừ khi USER yêu cầu.
- **Không** commit / push trừ khi USER yêu cầu rõ.
