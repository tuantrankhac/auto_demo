package api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import commons.BaseApi;
import constant.GlobalConstants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class CreateTicketV2 extends BaseApi {

    @Step("Call API crmIncidentCreateV2: Tạo ticket")
    public Response createTicketV2(String token) {

        // 1. Headers từ cURL
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer " + token);
        headers.put("origin", GlobalConstants.FRONTEND_URL);
        headers.put("referer", GlobalConstants.FRONTEND_URL + "/");
        this.requestHeaders.putAll(headers);         


        // 2. Đóng gói object "arguments"
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("name", "Tuấn Oxii 17");
        arguments.put("phone", "0342992917");
        arguments.put("phones", List.of("0354121536", "0969133718"));
        arguments.put("priorityId", "bfe1393e-7570-4318-9a7b-49617f2899a4");
        arguments.put("addressZoneId", "7c3df33a-39b8-4c63-926c-8ea8d5a7541a");
        arguments.put("address", "127 ");
        arguments.put("requestGroupId", "e436d61f-d61f-4edc-ad8c-6c302f0c90d8");
        arguments.put("requestTypeId", "b3806ff6-c53e-416e-8536-acc81630bd85");
        arguments.put("sourceId", "a75623d2-dbf6-4c2e-a784-6a2786538ffd");
        arguments.put("appointmentAt", 1773286699085L);
        arguments.put("productBrandId", "b43acda7-9814-4a6b-9bd1-3ed8e5562c32");
        arguments.put("productTypeId", "1a7aa558-6eca-4c8b-8853-7f94aebd9e12");
        arguments.put("productCategoryId", "d98a5996-bde1-4866-b250-7dec620b1d19");
        arguments.put("productSerial", "SN23KLJS990KL");
        arguments.put("description", "Oxii Test");
        arguments.put("technicianId", "99939312-fc73-4314-8c94-deea89a060e8");
        arguments.put("incidentTypeCode", "INCIDENT_TYPE_FORWARDED_TO_TECHNICIAN");
        arguments.put("productPurchaseAt", 1762325184815L);
        arguments.put("productManufactureAt", 1762102800000L);
        arguments.put("productId", "4490d346-2a89-4eea-bae6-573cb0834383");
        arguments.put("optionItemIds", List.of("0bc7d8b0-6f2a-43b0-ad59-592d3ead6cf0"));
        arguments.put("complainPriority", null);
        arguments.put("isDuplicate", true);
        arguments.put("znsToCustomer", false);
        arguments.put("incidentDuplicateId", "d4e38747-3ecc-4e73-b98d-2c08b48f1493");

        // Field động (Tạm thời để rỗng để test flow chính)
        arguments.put("ticketDynamicFieldValue", List.of());
        arguments.put("invoiceFileCmsUrls", List.of());

        // 3. Payload tổng thể
        Map<String, Object> variables = new HashMap<>();
        variables.put("arguments", arguments);

        Map<String, Object> payload = new HashMap<>();
        payload.put("operationName", "crmIncidentCreateV2");
        payload.put("variables", variables);
        payload.put("query", "mutation crmIncidentCreateV2($arguments: IncidentArgs!) {\n" +
                "  crmIncidentCreateV2(arguments: $arguments) {\n" +
                "    id\n" +
                "    no\n" +
                "    progress\n" +
                "    __typename\n" +
                "  }\n" +
                "}");

        // D. Gọi hàm post() từ BaseAPI
        return post(GlobalConstants.GRAPHQL_PATH, payload);
    }

}
