package commons;
import api.CreateTicketV2;
import api.LoginApiWithGraphQL;
import api.LoginApiWithRest;

public class ApiFactory{
    private LoginApiWithGraphQL loginWithGraphQL;
    private LoginApiWithRest loginWithRest;
    private CreateTicketV2 createTicketV2;

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


    public CreateTicketV2 getCreateTicketV2() {
        if (createTicketV2 == null) {
            createTicketV2 = new CreateTicketV2();
        }
        return createTicketV2;
    }

    


}