import { jiraSession } from "../session.js";

export async function transitionIssue(opts: {
  issueKey: string;
  transitionId?: string;
  transitionName?: string;
  comment?: string;
}) {
  const client = jiraSession.getClient();
  let transitionId = opts.transitionId;

  if (!transitionId) {
    if (!opts.transitionName) {
      throw new Error("Cần cung cấp transitionId hoặc transitionName.");
    }
    const data = (await client.getTransitions(opts.issueKey)) as {
      transitions?: Array<{ id: string; name: string }>;
    };
    const found = (data.transitions || []).find(
      (t) => t.name.toLowerCase() === opts.transitionName!.toLowerCase()
    );
    if (!found) {
      const available = (data.transitions || []).map((t) => `${t.id}:${t.name}`);
      throw new Error(
        `Không tìm thấy transition "${opts.transitionName}". Available: ${available.join(", ")}`
      );
    }
    transitionId = found.id;
  }

  await client.transitionIssue(opts.issueKey, transitionId, opts.comment);
  return {
    ok: true,
    issueKey: opts.issueKey,
    transitionId,
    transitionName: opts.transitionName,
  };
}

export async function listTransitions(issueKey: string) {
  const client = jiraSession.getClient();
  const data = (await client.getTransitions(issueKey)) as {
    transitions?: Array<{ id: string; name: string; to?: { name?: string } }>;
  };
  return {
    issueKey,
    transitions: (data.transitions || []).map((t) => ({
      id: t.id,
      name: t.name,
      to: t.to?.name,
    })),
  };
}
