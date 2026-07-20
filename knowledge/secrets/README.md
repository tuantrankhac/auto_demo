# Secrets management

Thư mục quản lý biến môi trường / token nhạy cảm cho project.

## File

| File | Vai trò |
|------|---------|
| `.env.example` | Template — **được** commit |
| `.env` | Giá trị thật — **không** commit (đã có trong `.gitignore`) |

## Setup

```bash
cd knowledge/secrets
copy .env.example .env   # Windows
# cp .env.example .env   # macOS/Linux
```

Điền `JIRA_API_TOKEN` trong `.env`.

## Liên kết với config

`knowledge/config/jira.yaml` dùng placeholder:

```yaml
apiToken: ${JIRA_API_TOKEN}
```

Jira MCP (`mcp/jira-mcp`) sẽ:

1. Load `knowledge/secrets/.env`
2. Đọc `knowledge/config/jira.yaml`
3. Resolve `${JIRA_API_TOKEN}` → giá trị thật từ env

## Biến quan trọng

| Biến | Bắt buộc | Mô tả |
|------|----------|--------|
| `JIRA_API_TOKEN` | Có | Atlassian API token (Cloud) |
| `XRAY_CLIENT_ID` | Khi dùng Xray Cloud | Client ID |
| `XRAY_CLIENT_SECRET` | Khi dùng Xray Cloud | Client Secret |
