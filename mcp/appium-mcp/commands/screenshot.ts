import { session } from "../session.js";
import { writeFileSync, mkdirSync } from "node:fs";
import { dirname, resolve } from "node:path";

export interface ScreenshotArgs {
  /** Optional path to save PNG (relative to project or absolute) */
  savePath?: string;
}

export async function screenshot(args: ScreenshotArgs = {}): Promise<{
  base64: string;
  savedTo?: string;
}> {
  const client = session.getClient();
  const base64 = await client.takeScreenshot();

  let savedTo: string | undefined;
  if (args.savePath) {
    const full = resolve(args.savePath);
    mkdirSync(dirname(full), { recursive: true });
    writeFileSync(full, Buffer.from(base64, "base64"));
    savedTo = full;
  }

  return { base64, savedTo };
}
