package be.fedasil.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class MapToCSVGenerator {
	
	static final String[] HEADER = {"label", "value"};
	
	public static void exportCSV(Map<String, String> map, File file) {
		
		createTargetDir(file);
		
		try {
			FileWriter out = new FileWriter(file);
			try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADER).withDelimiter('%'))) {
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
		try {
			file.getParentFile().mkdirs();
		} catch (SecurityException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	// private constructor
	private MapToCSVGenerator() { }
}
