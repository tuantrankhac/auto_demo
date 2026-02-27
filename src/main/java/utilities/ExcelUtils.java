package utilities;

import java.util.Map;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.*;
import java.io.File;



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
                if (currentRow == null) continue; // Bỏ qua dòng trống

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


}
