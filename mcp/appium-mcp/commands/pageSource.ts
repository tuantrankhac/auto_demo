import { session } from "../session.js";
import { writeFileSync, mkdirSync } from "node:fs";
import { dirname, resolve } from "node:path";

export interface PageSourceArgs {
  /** Optional path to save XML (relative to project or absolute) */
  savePath?: string;
}

export async function pageSource(args: PageSourceArgs = {}): Promise<string> {
  const client = session.getClient();
  const xml = await client.getPageSource();

  if (args.savePath) {
    const full = resolve(args.savePath);
    mkdirSync(dirname(full), { recursive: true });
    writeFileSync(full, xml, "utf8");
  }

  return xml;
}
