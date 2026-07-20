import { jiraSession } from "../session.js";

export async function searchIssues(opts: {
  jql?: string;
  maxResults?: number;
  fields?: string[];
}) {
  const client = jiraSession.getClient();
  const config = jiraSession.getConfig();
  const jql = (opts.jql || config.defaultJQL || `project = ${config.projectKey}`).trim();

  const result = (await client.searchIssues(
    jql,
    opts.maxResults ?? 50,
    opts.fields
  )) as {
    issues?: Array<{
      key: string;
      fields?: Record<string, unknown>;
    }>;
    total?: number;
  };

  const issues = (result.issues || []).map((i) => {
    const f = i.fields || {};
    const status = f.status as { name?: string } | undefined;
    const issuetype = f.issuetype as { name?: string } | undefined;
    return {
      key: i.key,
      summary: f.summary,
      status: status?.name,
      issuetype: issuetype?.name,
      labels: f.labels || [],
    };
  });

  return {
    jql,
    total: result.total ?? issues.length,
    count: issues.length,
    issues,
  };
}
