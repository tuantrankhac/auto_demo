import { jiraSession } from "../session.js";

/**
 * Lấy thông tin Test Case theo test management:
 * - xray: Xray Cloud GraphQL / REST getTest
 * - jira: issue type Test (native) qua Jira REST
 * - zephyr: Zephyr Scale (TM4J) test case API
 */
export async function getTestcase(testKey: string) {
  const client = jiraSession.getClient();
  const { testManagement } = client.config;

  if (testManagement === "xray") {
    // Xray Cloud GraphQL — getTest by issue id/key
    const query = `
      query($jql: String!) {
        getTests(jql: $jql, limit: 1) {
          total
          results {
            issueId
            projectId
            jira(fields: ["key", "summary", "status", "issuetype", "labels"])
          }
        }
      }
    `;
    const data = await client.xrayRequest<{
      data?: {
        getTests?: {
          total: number;
          results: Array<Record<string, unknown>>;
        };
      };
    }>("POST", "/api/v2/graphql", {
      query,
      variables: { jql: `key = ${testKey}` },
    });

    const results = data?.data?.getTests?.results || [];
    if (!results.length) {
      // Fallback: đọc issue Jira + ghi chú testManagement
      const issue = await client.getIssue(testKey);
      return {
        testManagement: "xray",
        source: "jira-fallback",
        testKey,
        issue,
        note: "Không lấy được từ Xray GraphQL getTests — trả về Jira issue.",
      };
    }

    return {
      testManagement: "xray",
      source: "xray-graphql",
      testKey,
      test: results[0],
    };
  }

  if (testManagement === "zephyr") {
    // Zephyr Scale Cloud: /rest/atm/1.0/testcase/{key}
    const data = await client.request(
      "GET",
      `/rest/atm/1.0/testcase/${encodeURIComponent(testKey)}`
    );
    return {
      testManagement: "zephyr",
      source: "zephyr-scale",
      testKey,
      test: data,
    };
  }

  // jira native — treat as regular issue (Test issue type)
  const issue = await client.getIssue(testKey);
  return {
    testManagement: "jira",
    source: "jira-rest",
    testKey,
    test: issue,
  };
}
