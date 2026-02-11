package utilities;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.qameta.allure.Allure;

public class DbConnection {

    // PostgreSQL
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/your_database_name";
    private static final String USERNAME = "postgres"; // thường là postgres hoặc user bạn tạo
    private static final String PASSWORD = "your_password"; // mật khẩu bạn nhập trong DBeaver
    private static final String DRIVER_CLASS = "org.postgresql.Driver";

    // Nếu dùng MySQL
    // private static final String DB_URL =
    // "jdbc:mysql://localhost:3306/your_database_name?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    // private static final String USERNAME = "root";
    // private static final String PASSWORD = "your_mysql_password";
    // private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private static Connection connection;

    // Kết nối DB
    public static void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(DRIVER_CLASS); // Load driver
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver không tìm thấy: " + DRIVER_CLASS, e);
            }
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("Kết nối DB thành công!");
        }
    }

    // Đóng kết nối (nên gọi ở @AfterMethod hoặc @AfterClass)
    public static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Đóng kết nối DB.");
        }
    }


    /**
     * Lấy giá trị mới nhất của 1 cột theo tên bảng và tên cột.
     * 
     * @param tableName  Tên bảng DB
     * @param columnName Tên cột muốn lấy giá trị
     * @param columnSort Tên cột muốn sắp xếp
     * @return Giá trị mới nhất (Object, có thể là String/Long/Date...)
     * @throws SQLException Nếu có lỗi thực thi SQL
     */
    public Object getLatestColumnValue(String tableName, String columnName, String columnSort) throws SQLException {
        String sql = String.format(
                "SELECT %s FROM %s ORDER BY %s DESC LIMIT 1",
                columnName, tableName, columnSort);
        connect(); // ensure connected

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getObject(columnName);
            }
            return null;
        }
    }


    /**
     * Query lấy một bản ghi (LIMIT 1) với các điều kiện linh hoạt.
     * Trả về Map<String, Object> chứa các cột và giá trị.
     *
     * @param queryParams Map chứa các tham số:
     *                    - "selectColumns": List<String> hoặc String (các cột cần
     *                    lấy, ví dụ: "t.id, t.status, u.username")
     *                    - "fromTable": String (bảng chính, ví dụ: "tickets t")
     *                    - "joins": List<String> (các câu JOIN, ví dụ: "JOIN users
     *                    u ON t.created_by = u.id")
     *                    - "whereCondition": String (điều kiện WHERE, ví dụ:
     *                    "t.business_key = 'INC-20260201'")
     *                    - "orderBy": String (ví dụ: "t.created_at DESC")
     *                    - "parameters": List<Object> (các giá trị cho ? nếu dùng
     *                    PreparedStatement)
     * @return Map chứa tên cột → giá trị của bản ghi mới nhất, hoặc null nếu không
     *         có
     * @throws SQLException
     */
    public static Map<String, Object> getValueRecord(Map<String, Object> queryParams) throws SQLException {
        Allure.step("Query vào db lấy dữ liệu");
        connect(); // đảm bảo kết nối

        // Build câu SQL
        StringBuilder sql = new StringBuilder("SELECT ");

        // select columns queryParams.put("selectColumns", "status");
        Object selectObj = queryParams.get("selectColumns");

        if 
        // select nhiều cột
        (selectObj instanceof List) {
            sql.append(String.join(", ", (List<String>) selectObj));
        }
        // select 1 cột
        else if (selectObj instanceof String) {
            sql.append((String) selectObj);
        } else {
            throw new IllegalArgumentException("selectColumns phải là List<String> hoặc String");
        }
        Allure.step("SELECT: " + selectObj);


        sql.append(" FROM ").append(queryParams.get("fromTable"));
        Allure.step("FROM: " + queryParams.get("fromTable"));

        // joins
        if (queryParams.containsKey("joins")) {
            Object joinsObj = queryParams.get("joins");
            if (joinsObj instanceof List) {
                for (String join : (List<String>) joinsObj) {
                    sql.append(" ").append(join);
                }
            } else if (joinsObj instanceof String) {
                sql.append(" ").append(joinsObj);
            }
            Allure.step("JOIN: " + joinsObj);
        }

        // where
        if (queryParams.containsKey("whereCondition")) {
            sql.append(" WHERE ").append(queryParams.get("whereCondition"));
            Allure.step("WHERE: " + queryParams.get("whereCondition"));
        }

        // order by
        if (queryParams.containsKey("orderBy")) {
            sql.append(" ORDER BY ").append(queryParams.get("orderBy"));
            Allure.step("ORDER BY: "+ queryParams.get("orderBy"));
        }

        sql.append(" LIMIT 1");

        // Thực thi query
        String finalSql = sql.toString();
        Allure.step("Thực thi SQL: " + finalSql);
        
        try (PreparedStatement stmt = connection.prepareStatement(finalSql)) {
            // Bind parameters nếu có (dùng ? trong whereCondition)
            if (queryParams.containsKey("parameters")) {
                List<Object> params = (List<Object>) queryParams.get("parameters");
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // Nếu tìm thấy ít nhất 1 dòng dữ liệu
                    ResultSetMetaData meta = rs.getMetaData();
                    int columnCount = meta.getColumnCount();
                    Map<String, Object> result = new LinkedHashMap<>();

                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = meta.getColumnLabel(i);
                        Object value = rs.getObject(i);
                        result.put(columnName, value);
                    }
                    return result;
                }
                return null; // không tìm thấy
            }
        }
    }

}