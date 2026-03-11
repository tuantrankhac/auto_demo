package commons;
import api.LoginApiWithGraphQL;
import api.LoginApiWithRest;

public class ApiFactory{
    private LoginApiWithGraphQL loginWithGraphQL;
    private LoginApiWithRest loginWithRest;

    // Chỉ khởi tạo khi Test Case thực sự gọi đến
    public LoginApiWithGraphQL getLoginGraphQL() {
        if (loginWithGraphQL == null) {
            loginWithGraphQL = new LoginApiWithGraphQL();
        }
        return loginWithGraphQL;
    }

    public LoginApiWithRest getLoginRest() {
        if (loginWithRest == null) {
            loginWithRest = new LoginApiWithRest();
        }
        return loginWithRest;
    }

    


}