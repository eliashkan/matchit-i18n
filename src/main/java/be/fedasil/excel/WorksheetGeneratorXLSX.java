package be.fedasil.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

public class WorksheetGeneratorXLSX {
	
	public static void generate(Map<String, Map<String, String>> dictionary, File file) {
		
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			
			dictionary.forEach((language, map) -> {
				Sheet sheet = makeSheet(workbook, language);
				makeHeader(workbook, sheet, language);
				// TreeMap to sort entries by key's natural order
				appendAllMapEntries(new TreeMap<>(map), sheet);
				
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
			});
			
			outputFile(file, workbook);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void outputFile(File file, XSSFWorkbook workbook) {
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
	
	private static void makeHeader(XSSFWorkbook workbook, Sheet sheet, String language) {
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
		headerCell.setCellValue(language);
		headerCell.setCellStyle(headerStyle);
	}
	
	private static Sheet makeSheet(XSSFWorkbook workbook, String title) {
		Sheet sheet = workbook.createSheet(title);
		sheet.setColumnWidth(0, 25 * 256);
		sheet.setColumnWidth(1, 25 * 256);
		return sheet;
	}
	
	// private constructor
	private WorksheetGeneratorXLSX() { }
}
