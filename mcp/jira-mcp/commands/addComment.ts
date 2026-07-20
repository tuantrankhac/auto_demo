import { jiraSession } from "../session.js";

export async function addComment(issueKey: string, body: string) {
  if (!body?.trim()) {
    throw new Error("Nội dung comment không được rỗng.");
  }
  const client = jiraSession.getClient();
  const result = await client.addComment(issueKey, body);
  return {
    ok: true,
    issueKey,
    comment: result,
  };
}
