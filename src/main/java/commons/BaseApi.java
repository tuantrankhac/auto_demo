package commons;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import constant.GlobalConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Class BaseAPI - Base class cho tất cả các API request.
 * - Cấu hình chung: base URI, headers, logging, Allure report.
 * - Các method tiện ích cho GET/POST/PUT/DELETE.
 */
public class BaseApi {
    protected static final Logger log = LogManager.getLogger(BaseApi.class);
    protected String baseUrl;
    // Dùng Map để quản lý headers linh hoạt hơn
    protected Map<String, String> requestHeaders = new HashMap<>();

    public BaseApi() {
        this.baseUrl = GlobalConstants.API_BASE_URL;
        requestHeaders.put("Content-Type", "application/json");
        requestHeaders.put("Accept", "application/json");
    }

    // Thêm hàm này vào BaseAPI để API Client có thể thêm Header tùy ý (như Origin,
    // Referer của GraphQL)
    public void setAdditionalHeaders(Map<String, String> customHeaders) {
        this.requestHeaders.putAll(customHeaders);
    }

    // Hàm này tạo ra RequestSpecification "tươi mới" cho mỗi lần gọi
    protected RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .headers(requestHeaders)
                .filters(new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new AllureRestAssured());
    }

    @Step("Set Authorization token")
    public void setAuthToken(String token) {
        requestHeaders.put("Authorization", "Bearer " + token);
    }

    // ------------------- GET -------------------
    @Step("Send GET request to {endpoint}")
    protected Response get(String endpoint) {
        return getRequestSpec().get(endpoint);
    }

    @Step("Send GET request to {endpoint} with params {params}")
    protected Response get(String endpoint, Map<String, ?> params) {
        return getRequestSpec().queryParams(params).get(endpoint);
    }

    // ------------------- POST -------------------
    @Step("Send POST request to {endpoint} with body {body}")
    protected Response post(String endpoint, Object body) {
        return getRequestSpec().body(body).post(endpoint);
    }

    @Step("Send POST request to {endpoint} with body {body} and params {params}")
    protected Response post(String endpoint, Object body, Map<String, ?> params) {
        return getRequestSpec().queryParams(params).body(body).post(endpoint);
    }

    // ------------------- PUT -------------------
    @Step("Send PUT request to {endpoint} with body {body}")
    protected Response put(String endpoint, Object body) {
        return getRequestSpec().body(body).put(endpoint);
    }

    // ------------------- DELETE -------------------
    @Step("Send DELETE request to {endpoint}")
    protected Response delete(String endpoint) {
        return getRequestSpec().delete(endpoint);
    }

    // ------------------- Verify Response -------------------
    @Step("Verify status code is {expectedStatusCode}")
    protected void verifyStatusCode(Response response, int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
        log.info("Status code verified: {}", expectedStatusCode);
    }

    @Step("Get response body as string")
    protected String getResponseBody(Response response) {
        return response.getBody().asString();
    }

    @Step("Verify response contains key {jsonPath} with value {expectedValue}")
    protected void verifyJsonPath(Response response, String jsonPath, Object expectedValue) {
        response.then().body(jsonPath, equalTo(expectedValue));
    }

    /**
     * Hàm helper dùng chung để trích xuất bất kỳ giá trị nào từ Response
     * 
     * @param response Kết quả trả về từ API
     * @param jsonPath Đường dẫn tới field cần lấy: trong response, đi từ ngoài vào
     *                 trong, qua mỗi ngoặc nhọn là một dấu "."
     * @return Giá trị của field đó (String, Integer, List...)
     */
    @Step("Extract value from response using JSON path: {jsonPath}")
    public <T> T extractValueFromResponse(Response response, String jsonPath) {
        T value = response.jsonPath().get(jsonPath);
        // Ghi trực tiếp giá trị lấy được vào Allure Report
        if (value != null) {
            Allure.addAttachment("Dữ liệu trích xuất được [" + jsonPath + "]: ", value.toString());
        }
        return value;
    }

}