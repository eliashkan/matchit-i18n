package be.fedasil.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Map;

@FunctionalInterface
interface WorksheetGenerator {
	
	XSSFWorkbook generate(Map<String, String> map);
	
}
