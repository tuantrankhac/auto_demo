#!/usr/bin/env node
/**
 * Jira MCP Server
 *
 * Tools:
 *  - jira_connect
 *  - jira_get_issue
 *  - jira_search_issues
 *  - jira_get_testcase
 *  - jira_get_test_steps
 *  - jira_update_field
 *  - jira_add_comment
 *  - jira_transition_issue
 */

import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";

import { connect } from "./commands/connect.js";
import { getIssue } from "./commands/getIssue.js";
import { searchIssues } from "./commands/searchIssues.js";
import { getTestcase } from "./commands/getTestcase.js";
import { getTestSteps } from "./commands/getTestSteps.js";
import { updateField } from "./commands/updateField.js";
import { addComment } from "./commands/addComment.js";
import {
  transitionIssue,
  listTransitions,
} from "./commands/transitionIssue.js";

function textResult(data: unknown) {
  return {
    content: [
      {
        type: "text" as const,
        text: typeof data === "string" ? data : JSON.stringify(data, null, 2),
      },
    ],
  };
}

function errorResult(err: unknown) {
  const message = err instanceof Error ? err.message : String(err);
  return {
    content: [{ type: "text" as const, text: `Error: ${message}` }],
    isError: true as const,
  };
}

const server = new McpServer({
  name: "jira-mcp",
  version: "1.0.0",
});

server.tool(
  "jira_connect",
  "Kết nối Jira bằng knowledge/config/jira.yaml + token từ knowledge/secrets/.env (${JIRA_API_TOKEN}).",
  {
    configPath: z
      .string()
      .optional()
      .describe("Đường dẫn jira.yaml (mặc định knowledge/config/jira.yaml)"),
  },
  async (args) => {
    try {
      const result = await connect(args.configPath);
      return textResult(result);
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "jira_get_issue",
  "Lấy chi tiết 1 Jira issue theo key (vd: CRM-123).",
  {
    issueKey: z.string().describe("Issue key, vd: CRM-123"),
    fields: z
      .array(z.string())
      .optional()
      .describe("Danh sách fields cần lấy (optional)"),
  },
  async (args) => {
    try {
      return textResult(await getIssue(args.issueKey, args.fields));
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "jira_search_issues",
  "Search issues bằng JQL. Nếu không truyền jql → dùng defaultJQL trong jira.yaml.",
  {
    jql: z.string().optional().describe("JQL query"),
    maxResults: z.number().int().positive().max(100).optional(),
    fields: z.array(z.string()).optional(),
  },
  async (args) => {
    try {
      return textResult(await searchIssues(args));
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "jira_get_testcase",
  "Lấy Test Case theo testManagement (xray | zephyr | jira).",
  {
    testKey: z.string().describe("Test key, vd: CRM-456"),
  },
  async (args) => {
    try {
      return textResult(await getTestcase(args.testKey));
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "jira_get_test_steps",
  "Lấy các bước (steps) của Test Case — hỗ trợ Xray / Zephyr.",
  {
    testKey: z.string().describe("Test key, vd: CRM-456"),
  },
  async (args) => {
    try {
      return textResult(await getTestSteps(args.testKey));
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "jira_update_field",
  "Cập nhật fields của issue (Jira REST fields object).",
  {
    issueKey: z.string().describe("Issue key"),
    fields: z
      .record(z.unknown())
      .describe('Object fields, vd: {"summary":"New title","labels":["Automation"]}'),
  },
  async (args) => {
    try {
      return textResult(await updateField(args.issueKey, args.fields));
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "jira_add_comment",
  "Thêm comment vào issue.",
  {
    issueKey: z.string().describe("Issue key"),
    body: z.string().describe("Nội dung comment (plain text)"),
  },
  async (args) => {
    try {
      return textResult(await addComment(args.issueKey, args.body));
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "jira_transition_issue",
  "Chuyển trạng thái issue. Có thể truyền transitionId hoặc transitionName.",
  {
    issueKey: z.string().describe("Issue key"),
    transitionId: z.string().optional().describe("ID transition"),
    transitionName: z
      .string()
      .optional()
      .describe('Tên transition, vd: "In Progress", "Done"'),
    comment: z.string().optional().describe("Comment kèm theo transition"),
    listOnly: z
      .boolean()
      .optional()
      .describe("Nếu true: chỉ liệt kê transitions khả dụng, không chuyển"),
  },
  async (args) => {
    try {
      if (args.listOnly) {
        return textResult(await listTransitions(args.issueKey));
      }
      return textResult(
        await transitionIssue({
          issueKey: args.issueKey,
          transitionId: args.transitionId,
          transitionName: args.transitionName,
          comment: args.comment,
        })
      );
    } catch (e) {
      return errorResult(e);
    }
  }
);

async function main() {
  const transport = new StdioServerTransport();
  await server.connect(transport);
}

main().catch((err) => {
  console.error("Jira MCP failed to start:", err);
  process.exit(1);
});
