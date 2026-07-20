import { buildAndroidCaps, buildIosCaps, session } from "../session.js";

export interface LaunchAppArgs {
  platform: "android" | "ios";
  appiumUrl?: string;
  udid?: string;
  platformVersion?: string;
  appPackage?: string;
  appActivity?: string;
  bundleId?: string;
  app?: string;
  noReset?: boolean;
  autoGrantPermissions?: boolean;
}

export async function launchApp(args: LaunchAppArgs): Promise<string> {
  const caps =
    args.platform === "android"
      ? buildAndroidCaps({
          udid: args.udid,
          platformVersion: args.platformVersion,
          appPackage: args.appPackage,
          appActivity: args.appActivity,
          app: args.app,
          noReset: args.noReset ?? true,
          autoGrantPermissions: args.autoGrantPermissions ?? true,
        })
      : buildIosCaps({
          udid: args.udid,
          platformVersion: args.platformVersion,
          bundleId: args.bundleId,
          app: args.app,
          noReset: args.noReset ?? true,
        });

  // End previous session if any, then create new
  if (session.hasSession()) {
    await session.endSession();
  }

  const sessionId = await session.ensureSession(caps, args.appiumUrl);

  // Activate app if package/bundle provided (when session already running app)
  const appId = args.platform === "android" ? args.appPackage : args.bundleId;
  if (appId) {
    try {
      await session.getClient().activateApp(appId);
    } catch {
      // activate may fail if app just launched via caps — ignore
    }
  }

  return sessionId;
}

export async function closeApp(): Promise<void> {
  await session.endSession();
}
