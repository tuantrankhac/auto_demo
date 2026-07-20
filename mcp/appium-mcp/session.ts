import { AppiumClient, type AppiumCaps } from "./appiumClient.js";

/**
 * Singleton session holder for the MCP process lifetime.
 */
class SessionManager {
  private client: AppiumClient | null = null;

  getClient(): AppiumClient {
    if (!this.client) {
      this.client = new AppiumClient();
    }
    return this.client;
  }

  setAppiumUrl(url: string): AppiumClient {
    this.client = new AppiumClient(url);
    return this.client;
  }

  hasSession(): boolean {
    return !!this.client?.getSessionId();
  }

  async ensureSession(capabilities: AppiumCaps, appiumUrl?: string): Promise<string> {
    if (appiumUrl) {
      this.setAppiumUrl(appiumUrl);
    }
    const client = this.getClient();
    if (client.getSessionId()) {
      return client.getSessionId()!;
    }
    return client.createSession(capabilities);
  }

  async endSession(): Promise<void> {
    if (!this.client) return;
    await this.client.deleteSession();
  }
}

export const session = new SessionManager();

/** Build Android UiAutomator2 capabilities from common params. */
export function buildAndroidCaps(opts: {
  udid?: string;
  platformVersion?: string;
  appPackage?: string;
  appActivity?: string;
  app?: string;
  noReset?: boolean;
  autoGrantPermissions?: boolean;
}): AppiumCaps {
  const caps: AppiumCaps = {
    platformName: "Android",
    "appium:automationName": "UiAutomator2",
  };

  if (opts.udid) caps["appium:udid"] = opts.udid;
  if (opts.platformVersion) caps["appium:platformVersion"] = opts.platformVersion;
  if (opts.app) caps["appium:app"] = opts.app;
  if (opts.appPackage) caps["appium:appPackage"] = opts.appPackage;
  if (opts.appActivity) caps["appium:appActivity"] = opts.appActivity;
  if (opts.noReset !== undefined) caps["appium:noReset"] = opts.noReset;
  if (opts.autoGrantPermissions !== undefined) {
    caps["appium:autoGrantPermissions"] = opts.autoGrantPermissions;
  }

  return caps;
}

/** Build iOS XCUITest capabilities from common params. */
export function buildIosCaps(opts: {
  udid?: string;
  platformVersion?: string;
  bundleId?: string;
  app?: string;
  noReset?: boolean;
}): AppiumCaps {
  const caps: AppiumCaps = {
    platformName: "iOS",
    "appium:automationName": "XCUITest",
  };

  if (opts.udid) caps["appium:udid"] = opts.udid;
  if (opts.platformVersion) caps["appium:platformVersion"] = opts.platformVersion;
  if (opts.app) caps["appium:app"] = opts.app;
  if (opts.bundleId) caps["appium:bundleId"] = opts.bundleId;
  if (opts.noReset !== undefined) caps["appium:noReset"] = opts.noReset;

  return caps;
}
