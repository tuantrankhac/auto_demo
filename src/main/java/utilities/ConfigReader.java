package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc file config.properties", e);
        }
    }

    /**
     * Lấy giá trị từ config theo key
     * 
     * @param key tên key (ví dụ "auth.token", "base.url")
     * @return giá trị String, hoặc null nếu không tìm thấy
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Lấy giá trị với default nếu không tìm thấy
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
