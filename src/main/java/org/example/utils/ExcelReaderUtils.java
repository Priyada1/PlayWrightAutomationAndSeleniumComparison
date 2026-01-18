package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading data from Excel files (.xlsx and .xls)
 * 
 * Usage Example:
 * <pre>
 * // Initialize Excel reader
 * ExcelReaderUtils excelReader = new ExcelReaderUtils("path/to/testdata.xlsx");
 * 
 * // Set sheet by name
 * excelReader.setSheet("Sheet1");
 * 
 * // Read cell by row and column index (0-based)
 * String cellValue = excelReader.getCellData(1, 0);
 * 
 * // Read cell by row index and column name (first row is header)
 * String value = excelReader.getCellData(0, "Username");
 * 
 * // Read entire row as Map
 * Map&lt;String, String&gt; rowData = excelReader.getRowDataAsMap(0);
 * 
 * // Read all data as List of Maps
 * List&lt;Map&lt;String, String&gt;&gt; allData = excelReader.getAllDataAsMap();
 * 
 * // Close the file when done
 * excelReader.close();
 * </pre>
 */
public class ExcelReaderUtils {
    private Workbook workbook;
    private Sheet sheet;
    private FileInputStream fileInputStream;

    /**
     * Constructor to initialize Excel file
     * @param excelFilePath Path to the Excel file
     */
    public ExcelReaderUtils(String excelFilePath) {
        try {
            File file = new File(excelFilePath);
            if (!file.exists()) {
                throw new RuntimeException("Excel file not found at path: " + excelFilePath);
            }
            fileInputStream = new FileInputStream(file);
            
            // Determine file type and create appropriate workbook
            String fileName = file.getName().toLowerCase();
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fileInputStream);
            } else if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fileInputStream);
            } else {
                throw new RuntimeException("Unsupported file format. Only .xlsx and .xls files are supported.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + excelFilePath, e);
        }
    }

    /**
     * Set the sheet to read from by sheet name
     * @param sheetName Name of the sheet
     */
    public void setSheet(String sheetName) {
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new RuntimeException("Sheet '" + sheetName + "' not found in the Excel file");
        }
    }

    /**
     * Set the sheet to read from by sheet index (0-based)
     * @param sheetIndex Index of the sheet
     */
    public void setSheet(int sheetIndex) {
        sheet = workbook.getSheetAt(sheetIndex);
        if (sheet == null) {
            throw new RuntimeException("Sheet at index " + sheetIndex + " not found in the Excel file");
        }
    }

    /**
     * Get the number of rows in the current sheet
     * @return Number of rows
     */
    public int getRowCount() {
        if (sheet == null) {
            throw new RuntimeException("Sheet not set. Please call setSheet() first.");
        }
        return sheet.getPhysicalNumberOfRows();
    }

    /**
     * Get the number of columns in a specific row
     * @param rowIndex Row index (0-based)
     * @return Number of columns
     */
    public int getColumnCount(int rowIndex) {
        if (sheet == null) {
            throw new RuntimeException("Sheet not set. Please call setSheet() first.");
        }
        Row row = sheet.getRow(rowIndex);
        return row != null ? row.getPhysicalNumberOfCells() : 0;
    }

    /**
     * Read cell value as String
     * @param rowIndex Row index (0-based)
     * @param columnIndex Column index (0-based)
     * @return Cell value as String
     */
    public String getCellData(int rowIndex, int columnIndex) {
        if (sheet == null) {
            throw new RuntimeException("Sheet not set. Please call setSheet() first.");
        }
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(columnIndex);
        return getCellValueAsString(cell);
    }

    /**
     * Read cell value as String by column name (first row is treated as header)
     * @param rowIndex Row index (0-based, excluding header row)
     * @param columnName Column name from header row
     * @return Cell value as String
     */
    public String getCellData(int rowIndex, String columnName) {
        if (sheet == null) {
            throw new RuntimeException("Sheet not set. Please call setSheet() first.");
        }
        int columnIndex = getColumnIndex(columnName);
        return getCellData(rowIndex + 1, columnIndex); // +1 to skip header row
    }

    /**
     * Get column index by column name (searches in first row)
     * @param columnName Column name to search
     * @return Column index (0-based)
     */
    public int getColumnIndex(String columnName) {
        if (sheet == null) {
            throw new RuntimeException("Sheet not set. Please call setSheet() first.");
        }
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new RuntimeException("Header row not found in the sheet");
        }
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && getCellValueAsString(cell).equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        throw new RuntimeException("Column '" + columnName + "' not found in the header row");
    }

    /**
     * Read entire row as List of Strings
     * @param rowIndex Row index (0-based)
     * @return List of cell values
     */
    public List<String> getRowData(int rowIndex) {
        if (sheet == null) {
            throw new RuntimeException("Sheet not set. Please call setSheet() first.");
        }
        List<String> rowData = new ArrayList<>();
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                rowData.add(getCellData(rowIndex, i));
            }
        }
        return rowData;
    }

    /**
     * Read entire row as Map (key-value pairs using header row as keys)
     * @param rowIndex Row index (0-based, excluding header row)
     * @return Map with column names as keys and cell values as values
     */
    public Map<String, String> getRowDataAsMap(int rowIndex) {
        if (sheet == null) {
            throw new RuntimeException("Sheet not set. Please call setSheet() first.");
        }
        Map<String, String> rowData = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new RuntimeException("Header row not found in the sheet");
        }
        Row dataRow = sheet.getRow(rowIndex + 1); // +1 to skip header row
        if (dataRow != null) {
            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                String columnName = getCellValueAsString(headerRow.getCell(i));
                String cellValue = dataRow.getCell(i) != null ? 
                    getCellValueAsString(dataRow.getCell(i)) : "";
                rowData.put(columnName, cellValue);
            }
        }
        return rowData;
    }

    /**
     * Read all data rows as List of Maps (excluding header row)
     * @return List of Maps, each Map represents a row
     */
    public List<Map<String, String>> getAllDataAsMap() {
        if (sheet == null) {
            throw new RuntimeException("Sheet not set. Please call setSheet() first.");
        }
        List<Map<String, String>> allData = new ArrayList<>();
        int rowCount = getRowCount();
        for (int i = 1; i < rowCount; i++) { // Start from 1 to skip header row
            allData.add(getRowDataAsMap(i - 1)); // Pass rowIndex excluding header
        }
        return allData;
    }

    /**
     * Read all data rows as List of Lists (including header row)
     * @return List of Lists, each inner List represents a row
     */
    public List<List<String>> getAllDataAsList() {
        if (sheet == null) {
            throw new RuntimeException("Sheet not set. Please call setSheet() first.");
        }
        List<List<String>> allData = new ArrayList<>();
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; i++) {
            allData.add(getRowData(i));
        }
        return allData;
    }

    /**
     * Get all sheet names in the workbook
     * @return List of sheet names
     */
    public List<String> getAllSheetNames() {
        List<String> sheetNames = new ArrayList<>();
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
    }

    /**
     * Convert cell value to String based on cell type
     * @param cell Cell object
     * @return Cell value as String
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Remove decimal if it's a whole number
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Evaluate formula and return result
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case STRING:
                        return cellValue.getStringValue();
                    case NUMERIC:
                        return String.valueOf(cellValue.getNumberValue());
                    case BOOLEAN:
                        return String.valueOf(cellValue.getBooleanValue());
                    default:
                        return "";
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Close the workbook and file input stream
     */
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error closing Excel file", e);
        }
    }
}

