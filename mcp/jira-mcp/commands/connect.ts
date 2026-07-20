import { jiraSession } from "../session.js";

export async function connect(configPath?: string) {
  return jiraSession.connect(configPath);
}
