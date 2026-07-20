import { jiraSession } from "../session.js";

export async function updateField(
  issueKey: string,
  fields: Record<string, unknown>
) {
  if (!fields || Object.keys(fields).length === 0) {
    throw new Error("Tham số fields không được rỗng.");
  }
  const client = jiraSession.getClient();
  await client.updateIssueFields(issueKey, fields);
  return {
    ok: true,
    issueKey,
    updatedFields: Object.keys(fields),
  };
}
