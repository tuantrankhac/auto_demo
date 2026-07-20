import type { JiraConfig } from "./config.js";

export class JiraClient {
  readonly config: JiraConfig;
  private xrayToken: string | null = null;

  constructor(config: JiraConfig) {
    this.config = config;
  }

  private authHeader(): string {
    const token = Buffer.from(
      `${this.config.email}:${this.config.apiToken}`
    ).toString("base64");
    return `Basic ${token}`;
  }

  async request<T = unknown>(
    method: string,
    path: string,
    body?: unknown
  ): Promise<T> {
    const url = path.startsWith("http")
      ? path
      : `${this.config.url}${path.startsWith("/") ? "" : "/"}${path}`;

    const res = await fetch(url, {
      method,
      headers: {
        Authorization: this.authHeader(),
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: body === undefined ? undefined : JSON.stringify(body),
    });

    const text = await res.text();
    let json: unknown = null;
    if (text) {
      try {
        json = JSON.parse(text);
      } catch {
        json = text;
      }
    }

    if (!res.ok) {
      throw new Error(
        `Jira API ${method} ${path} → ${res.status}: ${
          typeof json === "string" ? json : JSON.stringify(json)
        }`
      );
    }

    return json as T;
  }

  /** GET /rest/api/3/myself — verify credentials */
  async myself(): Promise<unknown> {
    return this.request("GET", "/rest/api/3/myself");
  }

  async getIssue(
    issueKey: string,
    fields?: string[]
  ): Promise<unknown> {
    const q =
      fields && fields.length
        ? `?fields=${encodeURIComponent(fields.join(","))}`
        : "";
    return this.request("GET", `/rest/api/3/issue/${issueKey}${q}`);
  }

  async searchIssues(jql: string, maxResults = 50, fields?: string[]): Promise<unknown> {
    return this.request("POST", "/rest/api/3/search/jql", {
      jql,
      maxResults,
      fields: fields || [
        "summary",
        "status",
        "issuetype",
        "priority",
        "assignee",
        "labels",
        "created",
        "updated",
      ],
    });
  }

  async updateIssueFields(
    issueKey: string,
    fields: Record<string, unknown>
  ): Promise<void> {
    await this.request("PUT", `/rest/api/3/issue/${issueKey}`, { fields });
  }

  async addComment(issueKey: string, body: string): Promise<unknown> {
    // ADF (Atlassian Document Format) for Cloud API v3
    return this.request("POST", `/rest/api/3/issue/${issueKey}/comment`, {
      body: {
        type: "doc",
        version: 1,
        content: [
          {
            type: "paragraph",
            content: [{ type: "text", text: body }],
          },
        ],
      },
    });
  }

  async getTransitions(issueKey: string): Promise<unknown> {
    return this.request("GET", `/rest/api/3/issue/${issueKey}/transitions`);
  }

  async transitionIssue(
    issueKey: string,
    transitionId: string,
    comment?: string
  ): Promise<void> {
    const payload: Record<string, unknown> = {
      transition: { id: transitionId },
    };
    if (comment) {
      payload.update = {
        comment: [
          {
            add: {
              body: {
                type: "doc",
                version: 1,
                content: [
                  {
                    type: "paragraph",
                    content: [{ type: "text", text: comment }],
                  },
                ],
              },
            },
          },
        ],
      };
    }
    await this.request(
      "POST",
      `/rest/api/3/issue/${issueKey}/transitions`,
      payload
    );
  }

  // --- Xray Cloud helpers ---

  async getXrayToken(): Promise<string> {
    if (this.xrayToken) return this.xrayToken;

    const clientId = process.env.XRAY_CLIENT_ID;
    const clientSecret = process.env.XRAY_CLIENT_SECRET;
    if (!clientId || !clientSecret) {
      throw new Error(
        "Thiếu XRAY_CLIENT_ID / XRAY_CLIENT_SECRET trong knowledge/secrets/.env (cần cho testManagement: xray)"
      );
    }

    const res = await fetch("https://xray.cloud.getxray.app/api/v2/authenticate", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ client_id: clientId, client_secret: clientSecret }),
    });
    const token = (await res.text()).replace(/^"|"$/g, "");
    if (!res.ok || !token) {
      throw new Error(`Xray authenticate failed: ${token}`);
    }
    this.xrayToken = token;
    return token;
  }

  async xrayRequest<T = unknown>(
    method: string,
    path: string,
    body?: unknown
  ): Promise<T> {
    const token = await this.getXrayToken();
    const url = path.startsWith("http")
      ? path
      : `https://xray.cloud.getxray.app${path.startsWith("/") ? "" : "/"}${path}`;

    const res = await fetch(url, {
      method,
      headers: {
        Authorization: `Bearer ${token}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: body === undefined ? undefined : JSON.stringify(body),
    });

    const text = await res.text();
    let json: unknown = text;
    try {
      json = text ? JSON.parse(text) : null;
    } catch {
      /* keep text */
    }

    if (!res.ok) {
      throw new Error(
        `Xray API ${method} ${path} → ${res.status}: ${
          typeof json === "string" ? json : JSON.stringify(json)
        }`
      );
    }
    return json as T;
  }
}
