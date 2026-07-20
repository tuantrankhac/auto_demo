# Jira MCP

MCP server để Cursor AI Agent làm việc với **Jira** (+ Xray / Zephyr Test Management).

## Cấu trúc

```
mcp/jira-mcp/
├── package.json
├── tsconfig.json
├── index.ts
├── config.ts              # Load jira.yaml + resolve ${JIRA_API_TOKEN}
├── jiraClient.ts
├── session.ts
└── commands/
    ├── connect.ts
    ├── getIssue.ts
    ├── searchIssues.ts
    ├── getTestcase.ts
    ├── getTestSteps.ts
    ├── updateField.ts
    ├── addComment.ts
    └── transitionIssue.ts
```

## Tools (8)

| Tool | Mô tả |
|------|--------|
| `jira_connect` | Kết nối + verify credentials |
| `jira_get_issue` | Lấy 1 issue |
| `jira_search_issues` | Search theo JQL (mặc định `defaultJQL`) |
| `jira_get_testcase` | Lấy Test Case (xray / zephyr / jira) |
| `jira_get_test_steps` | Lấy steps của Test Case |
| `jira_update_field` | Update fields issue |
| `jira_add_comment` | Thêm comment |
| `jira_transition_issue` | Transition status (hoặc list transitions) |

## Config & Secrets

| File | Vai trò |
|------|---------|
| `knowledge/config/jira.yaml` | URL, email, projectKey, testManagement, defaultJQL |
| `knowledge/secrets/.env` | `JIRA_API_TOKEN` (và Xray keys nếu cần) |
| `knowledge/secrets/.env.example` | Template |

`jira.yaml`:

```yaml
apiToken: ${JIRA_API_TOKEN}
```

MCP resolve `${JIRA_API_TOKEN}` từ `knowledge/secrets/.env`.

## Setup

```bash
# 1. Secrets
cd knowledge/secrets
copy .env.example .env
# Điền JIRA_API_TOKEN

# 2. Dependencies
cd ../../mcp/jira-mcp
npm install

# 3. Sửa knowledge/config/jira.yaml cho đúng URL/email/project
```

Reload MCP trong Cursor sau khi cấu hình.

## Biến môi trường (tuỳ chọn)

| Biến | Mô tả |
|------|--------|
| `JIRA_API_TOKEN` | Token Atlassian (bắt buộc) |
| `JIRA_BASE_URL` | Override URL |
| `JIRA_EMAIL` | Override email |
| `JIRA_PROJECT_KEY` | Override project |
| `XRAY_CLIENT_ID` / `XRAY_CLIENT_SECRET` | Khi `testManagement: xray` |
