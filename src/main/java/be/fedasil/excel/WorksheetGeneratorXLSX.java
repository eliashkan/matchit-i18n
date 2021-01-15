package be.fedasil.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

public class WorksheetGeneratorXLSX {
	
	// private constructor
	private WorksheetGeneratorXLSX() {
	}
	
	public static void generateXLSX(Map<String, Map<String, String>> dictionary, File file) {
		try (Workbook workbookXLSX = new XSSFWorkbook()) {
			// create & export .xlsx
			fillWorkbook(dictionary, workbookXLSX);
			outputFile(file, workbookXLSX);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void generateXLS(Map<String, Map<String, String>> dictionary, File file) {
		try (Workbook workbookXLS = new HSSFWorkbook()) {
			// create & export .xls
			fillWorkbook(dictionary, workbookXLS);
			outputFile(file, workbookXLS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void fillWorkbook(Map<String, Map<String, String>> dictionary, Workbook workbook) {
		dictionary.forEach((language, map) -> {
			Sheet sheet = makeSheet(workbook, language);
			makeHeader(workbook, sheet, language);
			
			// TreeMap to sort entries by key's natural order
			appendAllMapEntries(new TreeMap<>(map), sheet);
			
			// autosize columns to fit contents
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
		});
	}
	
	private static void outputFile(File file, Workbook workbook) {
		try (OutputStream fileOut = new FileOutputStream(file)) {
			workbook.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void appendAllMapEntries(TreeMap<String, String> map, Sheet sheet) {
		map.forEach((key, val) -> {
			int lastRow = sheet.getLastRowNum();
			Row row = sheet.createRow(++lastRow);
			row.createCell(0).setCellValue(key);
			row.createCell(1).setCellValue(val);
		});
	}
	
	private static void makeHeader(Workbook workbook, Sheet sheet, String language) {
		Row header = sheet.createRow(0);
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Label");
		headerCell.setCellStyle(headerStyle);
		
		headerCell = header.createCell(1);
		headerCell.setCellValue(language);
		headerCell.setCellStyle(headerStyle);
	}
	
	private static Sheet makeSheet(Workbook workbook, String title) {
		Sheet sheet = workbook.createSheet(title);
		sheet.setColumnWidth(0, 25 * 256);
		sheet.setColumnWidth(1, 25 * 256);
		return sheet;
	}
}
