package demo.api;
import commons.BaseApi;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class LoginApiWithRest extends BaseApi{

    private final String REST_LOGIN_PATH = "/api/v1/login";
    public Response loginWithRest(String email, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        return post(REST_LOGIN_PATH, body);
    }
    

}
