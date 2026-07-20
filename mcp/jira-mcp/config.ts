import { existsSync, readFileSync } from "node:fs";
import { resolve, dirname } from "node:path";
import { fileURLToPath } from "node:url";
import { config as loadDotenv } from "dotenv";
import yaml from "js-yaml";

export type TestManagement = "xray" | "zephyr" | "jira";

export interface JiraConfig {
  url: string;
  email: string;
  apiToken: string;
  projectKey: string;
  testManagement: TestManagement;
  defaultJQL: string;
}

const __dirname = dirname(fileURLToPath(import.meta.url));
const PROJECT_ROOT = resolve(__dirname, "../..");

function loadSecretsEnv(): void {
  const candidates = [
    resolve(PROJECT_ROOT, "knowledge/secrets/.env"),
    resolve(PROJECT_ROOT, ".env"),
  ];
  for (const p of candidates) {
    if (existsSync(p)) {
      loadDotenv({ path: p });
      return;
    }
  }
}

/** Replace ${VAR} placeholders with process.env values. */
function resolveEnvPlaceholders(input: string): string {
  return input.replace(/\$\{([A-Z0-9_]+)\}/g, (_, key: string) => {
    const value = process.env[key];
    if (value === undefined || value === "") {
      throw new Error(
        `Thiếu biến môi trường ${key}. Hãy tạo knowledge/secrets/.env từ .env.example và điền giá trị.`
      );
    }
    return value;
  });
}

function deepResolve(value: unknown): unknown {
  if (typeof value === "string") {
    return resolveEnvPlaceholders(value);
  }
  if (Array.isArray(value)) {
    return value.map(deepResolve);
  }
  if (value && typeof value === "object") {
    const out: Record<string, unknown> = {};
    for (const [k, v] of Object.entries(value as Record<string, unknown>)) {
      out[k] = deepResolve(v);
    }
    return out;
  }
  return value;
}

export function getProjectRoot(): string {
  return PROJECT_ROOT;
}

export function loadJiraConfig(configPath?: string): JiraConfig {
  loadSecretsEnv();

  const path =
    configPath ||
    process.env.JIRA_CONFIG_PATH ||
    resolve(PROJECT_ROOT, "knowledge/config/jira.yaml");

  if (!existsSync(path)) {
    throw new Error(`Không tìm thấy file config: ${path}`);
  }

  const raw = yaml.load(readFileSync(path, "utf8")) as {
    jira?: Record<string, unknown>;
  };

  if (!raw?.jira) {
    throw new Error(`File ${path} thiếu key "jira"`);
  }

  const resolved = deepResolve(raw.jira) as Record<string, string>;

  // Allow env overrides
  const url = (process.env.JIRA_BASE_URL || resolved.url || "").replace(/\/$/, "");
  const email = process.env.JIRA_EMAIL || resolved.email || "";
  const apiToken = process.env.JIRA_API_TOKEN || resolved.apiToken || "";
  const projectKey = process.env.JIRA_PROJECT_KEY || resolved.projectKey || "";
  const testManagement = (
    process.env.JIRA_TEST_MANAGEMENT ||
    resolved.testManagement ||
    "xray"
  ).toLowerCase() as TestManagement;
  const defaultJQL = (resolved.defaultJQL || "").trim();

  if (!url || !email || !apiToken) {
    throw new Error(
      "Thiếu url / email / apiToken. Kiểm tra knowledge/config/jira.yaml và knowledge/secrets/.env"
    );
  }

  if (!["xray", "zephyr", "jira"].includes(testManagement)) {
    throw new Error(
      `testManagement không hợp lệ: ${testManagement}. Dùng xray | zephyr | jira`
    );
  }

  return {
    url,
    email,
    apiToken,
    projectKey,
    testManagement,
    defaultJQL,
  };
}
