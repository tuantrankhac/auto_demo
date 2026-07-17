package utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.qameta.allure.Allure;

import java.io.File;

public class EnvironmentManager {
    private static JsonNode envConfig;

    // Hàm này sẽ được gọi 1 lần duy nhất ở @BeforeSuite
    public static void loadEnvironment() {
        try {
            // Lấy giá trị từ lệnh Maven (-Denv=...). Nếu không truyền, mặc định là "staging"
            String env = System.getProperty("env", "stg").toLowerCase();
            Allure.step("🚀 Bắt đầu chạy Test trên môi trường: " + env.toUpperCase());

            // Đọc file JSON tương ứng
            String filePath = "src/test/resources/environments/" + env + ".json";
            ObjectMapper mapper = new ObjectMapper();
            envConfig = mapper.readTree(new File(filePath));

        } catch (Exception e) {
            throw new RuntimeException("Lỗi không đọc được file cấu hình môi trường!", e);
        }
    }

    // Các hàm Get để lấy Data ra dùng
    public static JsonNode getEnvConfig() {
        if (envConfig == null) {
            loadEnvironment();
        }
        return envConfig;
    }
    
    // Ví dụ lấy API URL để dùng chung cho việc gọi API tạo data
    public static String getApiUrl() {
        return getEnvConfig().get("apiUrl").asText();
    }
}