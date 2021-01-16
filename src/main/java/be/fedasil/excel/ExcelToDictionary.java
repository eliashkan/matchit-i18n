package be.fedasil.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelToDictionary {
	
	private ExcelToDictionary() {
	}
	
	public static Map<String, Map<String, String>> getDictionaryFromExcelInputStream(InputStream excelInStream)
			throws IOException {
		
		Map<String, String> frMap;
		Map<String, String> nlMap;
		
		try (Workbook workbook = WorkbookFactory.create(excelInStream)) {
			frMap = getMapFromSheet(workbook, "FR");
			nlMap = getMapFromSheet(workbook, "NL");
		}
		
		Map<String, Map<String, String>> dictionary = new HashMap<>();
		dictionary.put("FR", frMap);
		dictionary.put("NL", nlMap);
		
		return dictionary;
	}
	
	private static Map<String, String> getMapFromSheet(Workbook workbook, String sheetLanguage) {
		Sheet sheet = workbook.getSheet(sheetLanguage);
		
		// remove header row
		sheet.removeRow(sheet.getRow(0));
		
		// put in map
		Map<String, String> map = new HashMap<>();
		sheet.forEach(row -> map.put(
				row.getCell(0).getStringCellValue(),
				row.getCell(1).getStringCellValue())
		);
		
		return map;
	}
}
