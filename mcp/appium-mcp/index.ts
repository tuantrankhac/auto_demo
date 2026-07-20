#!/usr/bin/env node
/**
 * Appium MCP Server
 * Exposes mobile automation tools to Cursor AI Agent via Model Context Protocol.
 *
 * Tools:
 *  - appium_launch_app
 *  - appium_close_app
 *  - appium_click
 *  - appium_input
 *  - appium_scroll
 *  - appium_page_source
 *  - appium_screenshot
 *  - appium_status
 */

import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";

import { launchApp, closeApp } from "./commands/launchApp.js";
import { click } from "./commands/click.js";
import { input } from "./commands/input.js";
import { scroll } from "./commands/scroll.js";
import { pageSource } from "./commands/pageSource.js";
import { screenshot } from "./commands/screenshot.js";
import { session } from "./session.js";

const locatorStrategySchema = z.enum([
  "accessibility id",
  "id",
  "xpath",
  "class name",
  "-android uiautomator",
  "-ios predicate string",
  "-ios class chain",
]);

function textResult(text: string) {
  return { content: [{ type: "text" as const, text }] };
}

function errorResult(err: unknown) {
  const message = err instanceof Error ? err.message : String(err);
  return {
    content: [{ type: "text" as const, text: `Error: ${message}` }],
    isError: true as const,
  };
}

const server = new McpServer({
  name: "appium-mcp",
  version: "1.0.0",
});

server.tool(
  "appium_launch_app",
  "Khởi tạo Appium session và mở app (Android/iOS). Cần Appium server đang chạy.",
  {
    platform: z.enum(["android", "ios"]).describe("Nền tảng mobile"),
    appiumUrl: z
      .string()
      .optional()
      .describe("Appium server URL (mặc định http://127.0.0.1:4723)"),
    udid: z.string().optional().describe("UDID / device id"),
    platformVersion: z.string().optional().describe("Version OS, vd: 13.0"),
    appPackage: z.string().optional().describe("Android appPackage"),
    appActivity: z.string().optional().describe("Android appActivity"),
    bundleId: z.string().optional().describe("iOS bundleId"),
    app: z.string().optional().describe("Đường dẫn file .apk / .ipa / .app"),
    noReset: z.boolean().optional().describe("Giữ data app (mặc định true)"),
    autoGrantPermissions: z
      .boolean()
      .optional()
      .describe("Android: auto grant permissions (mặc định true)"),
  },
  async (args) => {
    try {
      const sessionId = await launchApp(args);
      return textResult(
        JSON.stringify(
          {
            ok: true,
            sessionId,
            platform: args.platform,
            appiumUrl: args.appiumUrl || "http://127.0.0.1:4723",
          },
          null,
          2
        )
      );
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "appium_close_app",
  "Đóng Appium session hiện tại.",
  {},
  async () => {
    try {
      await closeApp();
      return textResult(JSON.stringify({ ok: true, message: "Session closed" }));
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "appium_click",
  "Click / tap vào element trên màn hình hiện tại.",
  {
    strategy: locatorStrategySchema.describe("Chiến lược locator"),
    selector: z.string().describe("Giá trị locator"),
  },
  async (args) => {
    try {
      await click(args);
      return textResult(
        JSON.stringify({
          ok: true,
          action: "click",
          strategy: args.strategy,
          selector: args.selector,
        })
      );
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "appium_input",
  "Nhập text vào element (clear trước khi nhập, mặc định bật).",
  {
    strategy: locatorStrategySchema.describe("Chiến lược locator"),
    selector: z.string().describe("Giá trị locator"),
    text: z.string().describe("Nội dung cần nhập"),
    clear: z.boolean().optional().describe("Clear trước khi nhập (mặc định true)"),
  },
  async (args) => {
    try {
      await input(args);
      return textResult(
        JSON.stringify({
          ok: true,
          action: "input",
          strategy: args.strategy,
          selector: args.selector,
          textLength: args.text.length,
        })
      );
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "appium_scroll",
  "Scroll / swipe trên màn hình. Android hỗ trợ scrollIntoView theo text.",
  {
    direction: z
      .enum(["up", "down", "left", "right"])
      .optional()
      .describe("Hướng scroll (mặc định down)"),
    percent: z
      .number()
      .optional()
      .describe("Độ dài swipe 0.1–0.9 (mặc định 0.6)"),
    androidScrollToText: z
      .string()
      .optional()
      .describe("Android: scroll đến element chứa text này (UiScrollable)"),
  },
  async (args) => {
    try {
      await scroll(args);
      return textResult(JSON.stringify({ ok: true, action: "scroll", ...args }));
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "appium_page_source",
  "Lấy UI hierarchy (page source XML) của màn hình hiện tại — dùng để generate locator.",
  {
    savePath: z
      .string()
      .optional()
      .describe("Đường dẫn lưu file XML (vd: tmp/ui.xml)"),
  },
  async (args) => {
    try {
      const xml = await pageSource(args);
      const preview =
        xml.length > 12000
          ? `${xml.slice(0, 12000)}\n\n...[truncated ${xml.length - 12000} chars]`
          : xml;
      return textResult(
        args.savePath
          ? `Saved to: ${args.savePath}\n\n${preview}`
          : preview
      );
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "appium_screenshot",
  "Chụp screenshot màn hình hiện tại (base64 PNG, có thể lưu file).",
  {
    savePath: z
      .string()
      .optional()
      .describe("Đường dẫn lưu PNG (vd: tmp/screen.png)"),
  },
  async (args) => {
    try {
      const result = await screenshot(args);
      return textResult(
        JSON.stringify(
          {
            ok: true,
            savedTo: result.savedTo,
            base64Length: result.base64.length,
            // Không trả full base64 vào chat nếu quá dài — Agent nên dùng savePath
            note: result.savedTo
              ? "Screenshot đã lưu file."
              : "Dùng savePath để lưu file thay vì đọc base64 trong chat.",
          },
          null,
          2
        )
      );
    } catch (e) {
      return errorResult(e);
    }
  }
);

server.tool(
  "appium_status",
  "Kiểm tra trạng thái Appium server và session hiện tại.",
  {},
  async () => {
    try {
      const client = session.getClient();
      const status = await client.status();
      return textResult(
        JSON.stringify(
          {
            ok: true,
            appiumUrl: client.baseUrl,
            sessionId: client.getSessionId(),
            status,
          },
          null,
          2
        )
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
  console.error("Appium MCP failed to start:", err);
  process.exit(1);
});
