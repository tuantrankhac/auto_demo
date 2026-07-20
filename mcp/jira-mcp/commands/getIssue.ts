import { jiraSession } from "../session.js";

export async function getIssue(issueKey: string, fields?: string[]) {
  const client = jiraSession.getClient();
  const issue = (await client.getIssue(issueKey, fields)) as {
    key: string;
    id: string;
    fields: Record<string, unknown>;
  };

  const f = issue.fields || {};
  const status = f.status as { name?: string } | undefined;
  const issuetype = f.issuetype as { name?: string } | undefined;
  const assignee = f.assignee as { displayName?: string } | undefined;
  const priority = f.priority as { name?: string } | undefined;

  return {
    key: issue.key,
    id: issue.id,
    summary: f.summary,
    status: status?.name,
    issuetype: issuetype?.name,
    priority: priority?.name,
    assignee: assignee?.displayName || null,
    labels: f.labels || [],
    description: f.description,
    raw: fields ? issue : undefined,
  };
}
