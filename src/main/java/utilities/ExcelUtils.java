package utilities;

import java.util.Map;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.qameta.allure.Allure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelUtils {
    // Cache để không phải mở file nhiều lần, tăng tốc độ khi chạy CI/CD
    private static Map<String, Workbook> workbookCache = new ConcurrentHashMap<>();

    private static Workbook getWorkbook(String filePath) throws Exception {
        if (!workbookCache.containsKey(filePath)) {
            Workbook workbook = WorkbookFactory.create(new File(filePath));
            workbookCache.put(filePath, workbook);
        }
        return workbookCache.get(filePath);
    }

    /**
     * Đọc toàn bộ sheet thành List<Map>
     * Key là tên cột (Header), Value là dữ liệu ô
     */
    public static List<Map<String, String>> getSheetData(String fileName, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            String path = constant.GlobalConstants.TESTDATA_FILE_FOLDER + fileName;
            Sheet sheet = getWorkbook(path).getSheet(sheetName);
            DataFormatter formatter = new DataFormatter(); // Xử lý format ngày tháng, số...

            Row headerRow = sheet.getRow(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null)
                    continue; // Bỏ qua dòng trống

                Map<String, String> rowMap = new HashMap<>();
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String columnName = formatter.formatCellValue(headerRow.getCell(j));
                    String cellValue = formatter.formatCellValue(currentRow.getCell(j));
                    rowMap.put(columnName, cellValue);
                }
                dataList.add(rowMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Lấy và LOCK một tài khoản Available từ Excel để dùng trong parallel test
     * 
     * @return Map chứa thông tin tài khoản + rowIndex để release sau
     *         Throw exception nếu không còn tài khoản Available
     */
    public static synchronized Map<String, Object> getAndLockAvailableData(String fileName, String sheetName) {
        String fullPath = constant.GlobalConstants.TESTDATA_FILE_FOLDER + fileName;
        Map<String, Object> data = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(fullPath);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Không tìm thấy sheet: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            DataFormatter formatter = new DataFormatter();

            // Tìm cột theo tên header
            int statusColIndex = -1;
            int isUsingColIndex = -1;
            int usernameColIndex = -1;
            int passwordColIndex = -1;

            for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                String colName = formatter.formatCellValue(headerRow.getCell(j)).trim().toLowerCase();
                switch (colName) {
                    case "status":
                        statusColIndex = j;
                        break;
                    case "isusing":
                        isUsingColIndex = j;
                        break;
                    case "username":
                        usernameColIndex = j;
                        break;
                    case "password":
                        passwordColIndex = j;
                        break;
                }
            }

            // Duyệt từ dòng 1 trở xuống (bỏ header)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
    
                // 2. Tạo biến tạm final ở ĐÂY để dùng cho Lambda/Thread
                final int currentRow = i; 
            
                Row row = sheet.getRow(currentRow);
                if (row == null) continue;

                String status = formatter.formatCellValue(row.getCell(statusColIndex)).trim();
                String isUsingStr = formatter.formatCellValue(row.getCell(isUsingColIndex)).trim();

                boolean isUsing = Boolean.parseBoolean(isUsingStr) || "true".equalsIgnoreCase(isUsingStr);

                if ("available".equalsIgnoreCase(status) && !isUsing) {
                    // === LOCK tài khoản này ===
                    row.getCell(isUsingColIndex).setCellValue(true);
                    row.getCell(statusColIndex).setCellValue("InUse");

                    // Ghi file ngay lập tức để các thread khác thấy
                    try (FileOutputStream fos = new FileOutputStream(fullPath)) {
                        workbook.write(fos);
                    }

                    // Trả về thông tin
                    data.put("username", formatter.formatCellValue(row.getCell(usernameColIndex)));
                    data.put("password", formatter.formatCellValue(row.getCell(passwordColIndex)));
                    data.put("rowIndex", currentRow); // Dòng này để release sau
                    data.put("sheetName", sheetName);

                    Allure.step("Đã lock tài khoản thành công", () -> {
                        Allure.parameter("Username", data.get("username"));
                        Allure.parameter("RowIndex", currentRow);
                    });

                    return data;
                }
            }

        } catch (Exception e) {
            Allure.step("Lỗi khi lấy và lock tài khoản từ Excel", () -> Allure.parameter("Error", e.getMessage()));
            e.printStackTrace();
        }

        throw new RuntimeException("Không còn tài khoản Available nào trong file " + fileName);
    }

    
    /**
 * Giải phóng tài khoản sau khi dùng xong
 */
public static void releaseAccount(String fileName, int rowIndex, String sheetName) {
    if (rowIndex <= 0) return;

    String fullPath = constant.GlobalConstants.TESTDATA_FILE_FOLDER + fileName;

    try (FileInputStream fis = new FileInputStream(fullPath);
         Workbook workbook = new XSSFWorkbook(fis)) {

        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowIndex);

        if (row != null) {
            DataFormatter formatter = new DataFormatter();
            int statusCol = -1;
            int isUsingCol = -1;

            Row header = sheet.getRow(0);
            for (int j = 0; j < header.getLastCellNum(); j++) {
                String colName = formatter.formatCellValue(header.getCell(j)).trim().toLowerCase();
                if (colName.equals("status")) statusCol = j;
                if (colName.equals("isusing")) isUsingCol = j;
            }

            row.getCell(isUsingCol).setCellValue(false);
            row.getCell(statusCol).setCellValue("Available");

            try (FileOutputStream fos = new FileOutputStream(fullPath)) {
                workbook.write(fos);
            }

            Allure.step("Đã release tài khoản tại dòng " + rowIndex);
        }
    } catch (Exception e) {
        Allure.step("Lỗi release tài khoản", () -> Allure.parameter("RowIndex", rowIndex));
        e.printStackTrace();
    }
}


}
