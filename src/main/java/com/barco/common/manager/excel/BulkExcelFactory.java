package com.barco.common.manager.excel;

import com.barco.common.utility.BarcoUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
@Component
public class BulkExcelFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkExcelFactory.class);

    private static final String FONT_NAME = "Calibri";
    private static final short FONT_SIZE = 11;
    private XSSFWorkbook workbook;

    public BulkExcelFactory() {}

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
        LOGGER.debug("Workbook set: {}", workbook != null);
    }

    /**
     * Fills header row in Excel.
     * @param sheet The Excel sheet to fill.
     * @param rowIndex The index of the header row.
     * @param headers The list of header values.
     */
    public void fillBulkHeader(XSSFSheet sheet, int rowIndex, List<String> headers) {
        LOGGER.debug("fillBulkHeader rowIndex={}, headersCount={}", rowIndex, !BarcoUtil.isNull(headers) ? headers.size() : 0);
        Row headerRow = sheet.createRow(rowIndex);
        CellStyle headerStyle = createHeaderStyle();
        for (int index=0; index<headers.size(); index++) {
            this.createHeaderCell(sheet, headerRow, index, headerStyle, headers.get(index));
        }
        LOGGER.debug("Header row created at index {}", rowIndex);
    }

    /**
     * Fills body row in Excel.
     * @param sheet The Excel sheet to fill.
     * @param rowIndex The index of the body row.
     * @param data The list of body values.
     */
    public void fillBulkBody(XSSFSheet sheet, int rowIndex, List<String> data) {
        LOGGER.debug("fillBulkBody rowIndex={}, dataCount={}", rowIndex, !BarcoUtil.isNull(data) ? data.size() : 0);
        Row bodyRow = sheet.createRow(rowIndex);
        for (int index=0; index<data.size(); index++) {
            this.createCell(bodyRow, index, data.get(index));
        }
        LOGGER.debug("Body row created at index {}", rowIndex);
    }

    /**
     * Creates a cell style for header cells with bold font and grey background.
     * @return The created CellStyle.
     */
    private CellStyle createHeaderStyle() {
        LOGGER.debug("Creating header style");
        CellStyle style = this.workbook.createCellStyle();
        style.setFont(createHeaderFont());
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    /**
     * Creates a bold font for header cells.
     * @return The created Font.
     */
    private Font createHeaderFont() {
        LOGGER.debug("Creating header font name={} size={}", FONT_NAME, FONT_SIZE);
        Font font = this.workbook.createFont();
        font.setFontName(FONT_NAME);
        font.setBold(true);
        font.setFontHeightInPoints(FONT_SIZE);
        return font;
    }

    /**
     * Creates a header cell with specified style and value, and sets column width.
     * @param sheet The Excel sheet to modify.
     * @param row The row to create the cell in.
     * @param colIndex The column index for the cell.
     * @param style The CellStyle to apply to the cell.
     * @param value The value to set in the cell.
     * */
    private void createHeaderCell(XSSFSheet sheet, Row row, int colIndex, CellStyle style, String value) {
        LOGGER.debug("Creating header cell at row={} colIndex={} value={}", !BarcoUtil.isNull(row) ? row.getRowNum() : -1, colIndex, value);
        Cell cell = row.createCell(colIndex);
        cell.setCellStyle(style);
        cell.setCellValue(!BarcoUtil.isNull(value) ? value.toUpperCase() : "");
        sheet.setColumnWidth(colIndex, 30 * 256);
    }

    /**
     * Creates a regular cell with specified value.
     * @param row The row to create the cell in.
     * @param colIndex The column index for the cell.
     * @param value The value to set in the cell.
     * */
    private void createCell(Row row, int colIndex, String value) {
        LOGGER.debug("Creating cell at row={} colIndex={} value={}", !BarcoUtil.isNull(row) ? row.getRowNum() : -1, colIndex, value);
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(!BarcoUtil.isNull(value) ? value : "");
    }

    /**
     * Safely reads a cell value as String.
     * @param row The row containing the cell.
     * @param index The column index of the cell to read.
     * @return The cell value as String, or empty string if cell is null or of unsupported type.
     */
    public String getCellDetail(Row row, Integer index) {
        if (BarcoUtil.isNull(row)) {
            LOGGER.warn("getCellDetail called with null row, index={}", index);
            return "";
        }
        try {
            Cell currentCell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            currentCell.setCellType(CellType.STRING);
            String value = currentCell.getStringCellValue();
            LOGGER.debug("getCellDetail row={} index={} value={}", row.getRowNum(), index, value);
            return value;
        } catch (Exception e) {
            LOGGER.error("Error reading cell detail row={} index={} - returning empty string", row.getRowNum(), index, e);
            return "";
        }
    }

    /**
     * Adds dropdown validation to a cell.
     * @param sheet The Excel sheet to modify.
     * @param row The row index of the cell to add validation to.
     * @param col The column index of the cell to add validation to.
     * @param dropList The list of valid values for the dropdown.
     */
    public void fillDropDownValue(XSSFSheet sheet, int row, int col, String[] dropList) {
        LOGGER.debug("fillDropDownValue sheetName={} row={} col={} items={}", !BarcoUtil.isNull(sheet)
            ? sheet.getSheetName() : "null", row, col, !BarcoUtil.isNull(dropList) ? dropList.length : 0);
        XSSFDataValidationHelper helper = new XSSFDataValidationHelper(sheet);
        XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) helper.createExplicitListConstraint(dropList);
        CellRangeAddressList addressList = new CellRangeAddressList(row, row, col, col);
        XSSFDataValidation validation = (XSSFDataValidation) helper.createValidation(constraint, addressList);
        validation.setShowErrorBox(false);
        sheet.addValidationData(validation);
        LOGGER.debug("Dropdown validation added at row={} col={}", row, col);
    }

}
