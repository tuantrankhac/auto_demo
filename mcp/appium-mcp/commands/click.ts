import { session } from "../session.js";

export type LocatorStrategy =
  | "accessibility id"
  | "id"
  | "xpath"
  | "class name"
  | "-android uiautomator"
  | "-ios predicate string"
  | "-ios class chain";

export interface ClickArgs {
  strategy: LocatorStrategy;
  selector: string;
}

export async function click(args: ClickArgs): Promise<void> {
  const client = session.getClient();
  const elementId = await client.findElement(args.strategy, args.selector);
  await client.click(elementId);
}
