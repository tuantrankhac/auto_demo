# Prompt Templates

Thư mục này chứa **các prompt mẫu** dùng để gọi AI Agent thực hiện đúng từng workflow của project Demo Framework.

Cách dùng:

1. Mở file prompt tương ứng.
2. Thay các placeholder `{URL}`, `{ModuleName}`, `{JiraKey}`... bằng giá trị thật.
3. Copy toàn bộ nội dung prompt vào chat Agent (hoặc gắn với slash workflow liên quan).
4. Agent sẽ đọc skill/workflow tương ứng trong `.agent/` rồi thực thi.
5. Với prompt generate automation (05/06/13): **ưu tiên lấy TestCase ID từ Jira MCP** trước khi generate.

---

## Danh Sách Prompt

| # | File | Mục đích | Workflow liên quan |
|---|------|----------|--------------------|
| 01 | [`prompt_01_generate_requirements.txt`](prompt_01_generate_requirements.txt) | Phân tích website sinh Requirements | `/generate_requirements_from_website` |
| 02 | [`prompt_02_generate_testcases.txt`](prompt_02_generate_testcases.txt) | Sinh bộ Manual Testcase (Excel) từ Requirement | `/generate_testcases` |
| 03 | [`prompt_03_generate_locator.txt`](prompt_03_generate_locator.txt) | Sinh / mở rộng Locator (PageUI) từ DOM Web | `/generate_locator` |
| 04 | [`prompt_04_generate_pageobject.txt`](prompt_04_generate_pageobject.txt) | Sinh / mở rộng PageObject từ PageUI hiện có | `/generate_pageobject` |
| 05 | [`prompt_05_generate_testscript.txt`](prompt_05_generate_testscript.txt) | Sinh Automation cho 1 testcase — **lấy ID/steps từ Jira MCP trước** | `/generate_testscript` |
| 06 | [`prompt_06_generate_module.txt`](prompt_06_generate_module.txt) | Sinh Automation cả module — **search Jira → lấy từng ID/steps** | `/manual_to_auto` |
| 07 | [`prompt_07_review_automation.txt`](prompt_07_review_automation.txt) | Review Locator / PageObject / Test Script của module | `/review_code` |
| 08 | [`prompt_08_fix_failed_test.txt`](prompt_08_fix_failed_test.txt) | Phân tích và sửa Automation Test bị fail | `/debug_failed_test` |
| 09 | [`prompt_09_generate_testdata.txt`](prompt_09_generate_testdata.txt) | Sinh / cập nhật Test Data (JSON, Excel…) | `/generate_testdata` |
| 10 | [`prompt_10_generate_api_script.txt`](prompt_10_generate_api_script.txt) | Sinh API client / API test theo BaseApi | `/generate_api_script` |
| 11 | [`prompt_11_refactor_framework.txt`](prompt_11_refactor_framework.txt) | Phân tích và refactor framework / module | `/refactor_framework` |
| 12 | [`prompt_12_generate_mobile_locator.txt`](prompt_12_generate_mobile_locator.txt) | Sinh Locator Mobile từ UI hierarchy Android (adb) | `/generate_locator` |
| 13 | [`prompt_13_generate_db_verification.txt`](prompt_13_generate_db_verification.txt) | Bổ sung bước verify Database — **lấy steps từ Jira trước** | `/generate_db_verification` |

---

## Nguồn Testcase (sau khi có Jira MCP)

```
{JiraKey} / search Jira
        │
        ▼
jira_connect → jira_get_testcase → jira_get_test_steps
        │
        ▼
Generate Locator / PageObject / Test Script / DB verify
```

Fallback: `practices/testcases` (Excel) chỉ khi USER chỉ định hoặc Jira MCP không khả dụng.

---

## Placeholder thường dùng

| Placeholder | Ý nghĩa | Ví dụ |
|-------------|---------|--------|
| `{URL}` | Đường dẫn màn hình Web | `https://stg-crm.smarthiz.vn/login` |
| `{ModuleName}` | Tên module / màn hình | `Login` |
| `{JiraKey}` | Jira / Xray Test Case key (ưu tiên) | `CRM-123` |
| `{TestCaseID}` | ID local/Excel (fallback) | `TC_Login_01` |
| `{ApiName}` | Tên API / endpoint | `LoginApiWithRest` |
| `{PhạmVi}` | Phạm vi refactor | `module Login` / `pageObjects` |
| `{TestName}` | Class#method test bị fail | `LoginTest#TC_Login_01` |

---

## Lưu ý

- Prompt chỉ là **đầu vào mẫu** — Agent vẫn phải tuân thủ `RULE_GLOBAL.md`, `AGENTS.md`, `docs/` và skill trong `.agent/skills/`.
- Generate automation: **phải có TestCase ID** (Jira Key) rồi mới lấy steps — không bỏ qua bước Jira MCP khi nguồn là Jira.
- Trước khi mở Web/Mobile: **phải đọc** `practices/browsers/browser.md` hoặc `practices/mobile/android.md` / `ios.md`.
- Với Locator / PageObject: luôn kiểm tra **tên đúng + tên tương đồng** trước khi tạo mới (mở rộng nếu cùng nghiệp vụ).
- Không commit / push trừ khi USER yêu cầu rõ trong prompt hoặc chat.
- Secret Jira: `knowledge/secrets/.env` (không commit). Config: `knowledge/config/jira.yaml`.
