package api;

import java.util.HashMap;
import java.util.Map;
import commons.BaseApi;
import constant.GlobalConstants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class LoginApiWithGraphQL extends BaseApi{
    
    @Step("Call API crmUserLogin: Login với GraphQL [Email: {email}]")
    public Response loginWithGraphQL(String email, String password, String orgId) {
        // A. Thêm các Header đặc thù từ cURL của bạn
        Map<String, String> headers = new HashMap<>();
        headers.put("origin", GlobalConstants.FRONTEND_URL);
        headers.put("referer", GlobalConstants.FRONTEND_URL + "/");
        // Giả sử bạn đã thêm hàm này vào BaseAPI như mình thảo luận trước đó
        this.requestHeaders.putAll(headers); 

        // B. Đóng gói Variables
        Map<String, Object> credential = new HashMap<>();
        credential.put("email", email);
        credential.put("password", password);
        credential.put("organizationId", orgId);

        Map<String, Object> variables = new HashMap<>();
        variables.put("credential", credential);

        // C. Tạo Payload theo cấu trúc GraphQL
        Map<String, Object> payload = new HashMap<>();
        payload.put("operationName", "crmUserLogin");
        payload.put("variables", variables);
        
        // Query copy từ cURL (nên để trong file riêng nếu query quá dài)
        String query = "mutation crmUserLogin($credential: CrmUserLoginArgs!) {\n" +
                "  crmUserLogin(credential: $credential) {\n" +
                "    accessToken\n" +
                "    refreshToken\n" +
                "    user {\n" +
                "      id\n" +
                "      fullname\n" +
                "    }\n" +
                "  }\n" +
                "}";
        payload.put("query", query);

        // D. Gọi hàm post() từ BaseAPI
        return post(GlobalConstants.GRAPHQL_PATH, payload);
    }

    
    
}
