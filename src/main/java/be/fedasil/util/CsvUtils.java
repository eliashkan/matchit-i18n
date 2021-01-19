package be.fedasil.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import static org.apache.commons.csv.CSVParser.parse;

public class CsvUtils {
	
	static final String[] HEADER = {"label", "value"};
	
	// private constructor
	private CsvUtils() {
	}
	
	public static void exportCSV(Map<String, String> map, File file) {
		
		createTargetDir(file);
		
		try {
			FileWriter out = new FileWriter(file);
			try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADER).withDelimiter('='))) {
				map.forEach((label, value) -> {
					try {
						printer.printRecord(label, value);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createTargetDir(File file) {
		JsonUtils.createTargetDir(file);
	}
	
	public static Map<String, String> getMapFromCSVInputStream(InputStream csvInStream) throws IOException {
		CSVParser parser = parse(csvInStream,
				StandardCharsets.UTF_8,
				CSVFormat.DEFAULT.withDelimiter('=').withFirstRecordAsHeader());
		Map<String, String> map = new TreeMap<>();
		parser.getRecords().forEach(record -> map.put(record.get(0), record.get(1)));
		return map;
	}
}
