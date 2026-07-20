import { session } from "../session.js";

export type ScrollDirection = "up" | "down" | "left" | "right";

export interface ScrollArgs {
  direction?: ScrollDirection;
  percent?: number;
  /** Optional: scroll into view by Android UiScrollable text */
  androidScrollToText?: string;
}

export async function scroll(args: ScrollArgs): Promise<void> {
  const client = session.getClient();

  if (args.androidScrollToText) {
    const text = args.androidScrollToText.replace(/"/g, '\\"');
    await client.findElement(
      "-android uiautomator",
      `new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains("${text}"))`
    );
    return;
  }

  const direction = args.direction ?? "down";
  const percent = Math.min(Math.max(args.percent ?? 0.6, 0.1), 0.9);
  const size = await client.getWindowSize();
  const midX = Math.floor(size.width / 2);
  const midY = Math.floor(size.height / 2);
  const deltaX = Math.floor((size.width * percent) / 2);
  const deltaY = Math.floor((size.height * percent) / 2);

  let start = { x: midX, y: midY };
  let end = { x: midX, y: midY };

  switch (direction) {
    case "down":
      start = { x: midX, y: midY + deltaY };
      end = { x: midX, y: midY - deltaY };
      break;
    case "up":
      start = { x: midX, y: midY - deltaY };
      end = { x: midX, y: midY + deltaY };
      break;
    case "left":
      start = { x: midX - deltaX, y: midY };
      end = { x: midX + deltaX, y: midY };
      break;
    case "right":
      start = { x: midX + deltaX, y: midY };
      end = { x: midX - deltaX, y: midY };
      break;
  }

  await client.performActions([
    {
      type: "pointer",
      id: "finger1",
      parameters: { pointerType: "touch" },
      actions: [
        { type: "pointerMove", duration: 0, x: start.x, y: start.y },
        { type: "pointerDown", button: 0 },
        { type: "pause", duration: 200 },
        { type: "pointerMove", duration: 600, x: end.x, y: end.y },
        { type: "pointerUp", button: 0 },
      ],
    },
  ]);
}
