package be.fedasil.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Map;

public class WorksheetGeneratorXLSXImpl implements WorksheetGenerator {
	@Override
	public XSSFWorkbook generate(Map<String, String> map) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = makeSheet(workbook, "To Translate");
		makeHeader(workbook, sheet);
		appendAllMapEntries(map, sheet);
		return workbook;
	}
	
	private void appendAllMapEntries(Map<String, String> map, Sheet sheet) {
		map.forEach((key, val) -> {
			int lastPhysicalRow = sheet.getPhysicalNumberOfRows();
			Row row = sheet.createRow(++lastPhysicalRow);
			row.createCell(0).setCellValue(key);
			row.createCell(1).setCellValue(val);
		});
	}
	
	private void makeHeader(XSSFWorkbook workbook, Sheet sheet) {
		Row header = sheet.createRow(0);
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Label");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(1);
		headerCell.setCellValue("NL");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(2);
		headerCell.setCellValue("FR");
		headerCell.setCellStyle(headerStyle);
	}
	
	private Sheet makeSheet(XSSFWorkbook workbook, String title) {
		Sheet sheet = workbook.createSheet(title);
		sheet.setColumnWidth(0, 25 * 256);
		sheet.setColumnWidth(1, 25 * 256);
		return sheet;
	}
}
