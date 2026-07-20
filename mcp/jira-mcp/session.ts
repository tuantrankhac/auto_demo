import { loadJiraConfig, type JiraConfig } from "./config.js";
import { JiraClient } from "./jiraClient.js";

class JiraSession {
  private client: JiraClient | null = null;
  private connected = false;

  getConfig(): JiraConfig {
    return this.requireClient().config;
  }

  getClient(): JiraClient {
    return this.requireClient();
  }

  isConnected(): boolean {
    return this.connected && !!this.client;
  }

  async connect(configPath?: string): Promise<{
    ok: boolean;
    url: string;
    email: string;
    projectKey: string;
    testManagement: string;
    accountId?: string;
    displayName?: string;
  }> {
    const config = loadJiraConfig(configPath);
    this.client = new JiraClient(config);
    const me = (await this.client.myself()) as {
      accountId?: string;
      displayName?: string;
      emailAddress?: string;
    };
    this.connected = true;
    return {
      ok: true,
      url: config.url,
      email: config.email,
      projectKey: config.projectKey,
      testManagement: config.testManagement,
      accountId: me.accountId,
      displayName: me.displayName,
    };
  }

  private requireClient(): JiraClient {
    if (!this.client || !this.connected) {
      throw new Error(
        "Chưa kết nối Jira. Hãy gọi jira_connect trước."
      );
    }
    return this.client;
  }
}

export const jiraSession = new JiraSession();
