import { jiraSession } from "../session.js";

/**
 * Lấy các bước test (steps) của test case.
 */
export async function getTestSteps(testKey: string) {
  const client = jiraSession.getClient();
  const { testManagement } = client.config;

  if (testManagement === "xray") {
    const query = `
      query($jql: String!) {
        getTests(jql: $jql, limit: 1) {
          results {
            issueId
            steps {
              id
              action
              data
              result
            }
          }
        }
      }
    `;
    const data = await client.xrayRequest<{
      data?: {
        getTests?: {
          results: Array<{
            issueId: string;
            steps?: Array<{
              id?: string;
              action?: string;
              data?: string;
              result?: string;
            }>;
          }>;
        };
      };
    }>("POST", "/api/v2/graphql", {
      query,
      variables: { jql: `key = ${testKey}` },
    });

    const test = data?.data?.getTests?.results?.[0];
    const steps = (test?.steps || []).map((s, idx) => ({
      index: idx + 1,
      id: s.id,
      action: s.action,
      data: s.data,
      expectedResult: s.result,
    }));

    return {
      testManagement: "xray",
      testKey,
      issueId: test?.issueId,
      stepCount: steps.length,
      steps,
    };
  }

  if (testManagement === "zephyr") {
    const data = (await client.request(
      "GET",
      `/rest/atm/1.0/testcase/${encodeURIComponent(testKey)}`
    )) as {
      testScript?: {
        type?: string;
        steps?: Array<{
          description?: string;
          testData?: string;
          expectedResult?: string;
          index?: number;
        }>;
      };
    };

    const rawSteps = data.testScript?.steps || [];
    const steps = rawSteps.map((s, idx) => ({
      index: s.index ?? idx + 1,
      action: s.description,
      data: s.testData,
      expectedResult: s.expectedResult,
    }));

    return {
      testManagement: "zephyr",
      testKey,
      stepCount: steps.length,
      steps,
    };
  }

  // jira native — không có steps chuẩn; trả về description / custom fields nếu có
  const issue = (await client.getIssue(testKey)) as {
    fields?: Record<string, unknown>;
  };
  return {
    testManagement: "jira",
    testKey,
    stepCount: 0,
    steps: [],
    note: "testManagement=jira không có Test Steps API chuẩn. Dùng description hoặc chuyển sang xray/zephyr.",
    description: issue.fields?.description ?? null,
  };
}
