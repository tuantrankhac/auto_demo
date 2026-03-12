package demo.testSuites;
import org.testng.annotations.Test;

import io.restassured.response.Response;

import commons.BaseTest;

public class TestApi extends BaseTest{

    @Test
    public void TC01_testAPILogin() {
        // Lấy response từ API login
        String userName = "tongdai3@gmail.com";
        String password = "123456";
        String orgId = "a9e98734-e5d2-4851-84f3-5f1c9a9cd2a7";
        Response loginRes = apiFactory.getLoginGraphQL().loginWithGraphQL(userName, password, orgId);
        String token = apiFactory.getLoginGraphQL().extractValueFromResponse(loginRes, "data.crmUserLogin.accessToken");
        
        Response createTicket = apiFactory.getCreateTicketV2().createTicketV2(token);
        Integer noTicket = apiFactory.getCreateTicketV2().extractValueFromResponse(createTicket, "data.crmIncidentCreateV2.no");
        
    }
    

}
