import { session } from "../session.js";
import type { LocatorStrategy } from "./click.js";

export interface InputArgs {
  strategy: LocatorStrategy;
  selector: string;
  text: string;
  clear?: boolean;
}

export async function input(args: InputArgs): Promise<void> {
  const client = session.getClient();
  const elementId = await client.findElement(args.strategy, args.selector);
  if (args.clear !== false) {
    try {
      await client.clear(elementId);
    } catch {
      // some elements do not support clear
    }
  }
  await client.sendKeys(elementId, args.text);
}
