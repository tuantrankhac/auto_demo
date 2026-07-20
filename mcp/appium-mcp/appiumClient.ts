/**
 * Thin HTTP client for Appium / W3C WebDriver protocol.
 * Default Appium server: http://127.0.0.1:4723
 */

export type AppiumCaps = Record<string, unknown>;

export interface CreateSessionOptions {
  capabilities: AppiumCaps;
  appiumUrl?: string;
}

export class AppiumClient {
  readonly baseUrl: string;
  private sessionId: string | null = null;

  constructor(appiumUrl = process.env.APPIUM_URL || "http://127.0.0.1:4723") {
    this.baseUrl = appiumUrl.replace(/\/$/, "");
  }

  getSessionId(): string | null {
    return this.sessionId;
  }

  setSessionId(sessionId: string | null): void {
    this.sessionId = sessionId;
  }

  private sessionPath(path = ""): string {
    if (!this.sessionId) {
      throw new Error("Chưa có Appium session. Hãy gọi appium_launch_app trước.");
    }
    return `/session/${this.sessionId}${path}`;
  }

  async request<T = unknown>(
    method: string,
    path: string,
    body?: unknown
  ): Promise<T> {
    const url = `${this.baseUrl}${path}`;
    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: body === undefined ? undefined : JSON.stringify(body),
    });

    const text = await res.text();
    let json: { value?: T; error?: string; message?: string } | null = null;
    try {
      json = text ? JSON.parse(text) : null;
    } catch {
      throw new Error(`Appium response không phải JSON (${res.status}): ${text}`);
    }

    if (!res.ok) {
      const msg =
        (json && (json.message || json.error || JSON.stringify(json))) ||
        `HTTP ${res.status}`;
      throw new Error(`Appium error: ${msg}`);
    }

    return (json?.value ?? json) as T;
  }

  async createSession(capabilities: AppiumCaps): Promise<string> {
    // Appium 2 / W3C style
    const payload = {
      capabilities: {
        alwaysMatch: capabilities,
        firstMatch: [{}],
      },
    };

    const url = `${this.baseUrl}/session`;
    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    const text = await res.text();
    let json: Record<string, unknown>;
    try {
      json = text ? JSON.parse(text) : {};
    } catch {
      throw new Error(`Appium createSession không phải JSON (${res.status}): ${text}`);
    }
    if (!res.ok) {
      throw new Error(
        `Appium createSession failed: ${JSON.stringify(json)}`
      );
    }

    // Shapes: { value: { sessionId } } | { sessionId } | { value: { value: { sessionId } } }
    const value = (json.value ?? json) as Record<string, unknown>;
    const nested = (value.value ?? value) as Record<string, unknown>;
    const sid =
      (typeof value.sessionId === "string" && value.sessionId) ||
      (typeof nested.sessionId === "string" && nested.sessionId) ||
      (typeof json.sessionId === "string" && json.sessionId) ||
      null;

    if (!sid) {
      throw new Error(
        `Không lấy được sessionId từ Appium. Response: ${JSON.stringify(json)}`
      );
    }

    this.sessionId = sid;
    return sid;
  }

  async deleteSession(): Promise<void> {
    if (!this.sessionId) return;
    try {
      await this.request("DELETE", this.sessionPath());
    } finally {
      this.sessionId = null;
    }
  }

  async findElement(strategy: string, selector: string): Promise<string> {
    const value = await this.request<{ ELEMENT?: string; "element-6066-11e4-a52e-4f735466cecf"?: string }>(
      "POST",
      this.sessionPath("/element"),
      { using: strategy, value: selector }
    );
    const id =
      value["element-6066-11e4-a52e-4f735466cecf"] || value.ELEMENT;
    if (!id) {
      throw new Error(`Không tìm thấy element: ${strategy}=${selector}`);
    }
    return id;
  }

  async click(elementId: string): Promise<void> {
    await this.request("POST", this.sessionPath(`/element/${elementId}/click`));
  }

  async clear(elementId: string): Promise<void> {
    await this.request("POST", this.sessionPath(`/element/${elementId}/clear`));
  }

  async sendKeys(elementId: string, text: string): Promise<void> {
    await this.request("POST", this.sessionPath(`/element/${elementId}/value`), {
      text,
      value: text.split(""),
    });
  }

  async getPageSource(): Promise<string> {
    return this.request<string>("GET", this.sessionPath("/source"));
  }

  async takeScreenshot(): Promise<string> {
    // returns base64 PNG
    return this.request<string>("GET", this.sessionPath("/screenshot"));
  }

  async executeScript(script: string, args: unknown[] = []): Promise<unknown> {
    return this.request("POST", this.sessionPath("/execute/sync"), {
      script,
      args,
    });
  }

  async getWindowSize(): Promise<{ width: number; height: number }> {
    return this.request("GET", this.sessionPath("/window/rect")).then(
      (rect: unknown) => {
        const r = rect as { width: number; height: number };
        return { width: r.width, height: r.height };
      }
    );
  }

  async performActions(actions: unknown[]): Promise<void> {
    await this.request("POST", this.sessionPath("/actions"), { actions });
  }

  async activateApp(appId: string): Promise<void> {
    await this.request("POST", this.sessionPath("/appium/device/activate_app"), {
      appId,
    });
  }

  async terminateApp(appId: string): Promise<void> {
    await this.request("POST", this.sessionPath("/appium/device/terminate_app"), {
      appId,
    });
  }

  async status(): Promise<unknown> {
    return this.request("GET", "/status");
  }
}
