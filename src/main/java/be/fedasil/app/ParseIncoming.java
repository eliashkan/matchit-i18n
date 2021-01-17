package be.fedasil.app;

import picocli.CommandLine;
import pl.jalokim.propertiestojson.util.PropertiesToJsonConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Callable;

import static be.fedasil.excel.ExcelToDictionary.getDictionaryFromExcelInputStream;
import static be.fedasil.util.CsvUtils.getMapFromCSVInputStream;
import static be.fedasil.util.JsonUtils.writeStringToPath;
import static be.fedasil.util.MapUtils.combineMaps;
import static picocli.CommandLine.Command;

@Command(
		name = "in",
		description = "Parse incoming i18n files reviewed by helpdesk.",
		synopsisSubcommandLabel = "COMMAND"
)
public class ParseIncoming implements Callable<Integer> {
	
	private static final Path OUT_PARENT_PATH = Paths.get("target", "generated-i18n-files", "parsed-incoming-labels-json");
	
	public static void main(String... args) {
		int exitCode = new CommandLine(new ParseIncoming()).execute(args);
		System.exit(exitCode);
	}
	
	@Override
	public Integer call() throws Exception {
		
		InputStream xlsInputStream = getClass().getClassLoader().getResourceAsStream("labelsForReview.xls");
		InputStream xlsxInputStream = getClass().getClassLoader().getResourceAsStream("labelsForReview.xlsx");
		
		InputStream notSelectedForReviewFRInputStream = getClass().getClassLoader().getResourceAsStream("labelsNotSelectedForReviewFR.csv");
		InputStream notSelectedForReviewNLInputStream = getClass().getClassLoader().getResourceAsStream("labelsNotSelectedForReviewNL.csv");
		
		if (notSelectedForReviewFRInputStream != null &&
				notSelectedForReviewNLInputStream != null) {
			
			if (xlsInputStream != null) {
				processIncoming(xlsInputStream, notSelectedForReviewFRInputStream, notSelectedForReviewNLInputStream);
			} else if (xlsxInputStream != null) {
				processIncoming(xlsxInputStream, notSelectedForReviewFRInputStream, notSelectedForReviewNLInputStream);
			} else {
				throw new FileNotFoundException("labelsForReview.xls/xlsx not found in src/main/resources/ !!");
			}
			
		} else {
			throw new FileNotFoundException("labelsNotSelectedForReviewFR/NL.csv not found in src/main/resources/ !!");
		}
		
		return 0;
	}
	
	private void processIncoming(InputStream excelInStream,
	                             InputStream notSelectedForReviewFRInStream,
	                             InputStream notSelectedForReviewNLInStream) {
		
		try {
			// get two maps from incoming excel
			Map<String, Map<String, String>> dictionaryFromIncomingMaps = getDictionaryFromExcelInputStream(excelInStream);
			
			// get two maps from CSV notSelectedForReviewFR + NL
			Map<String, String> notSelectedForReviewFRMap = getMapFromCSVInputStream(notSelectedForReviewFRInStream);
			Map<String, String> notSelectedForReviewNLMap = getMapFromCSVInputStream(notSelectedForReviewNLInStream);
			
			// combine maps
			Map<String, String> combinedMapFR = combineMaps(notSelectedForReviewFRMap, dictionaryFromIncomingMaps.get("FR"));
			Map<String, String> combinedMapNL = combineMaps(notSelectedForReviewNLMap, dictionaryFromIncomingMaps.get("NL"));
			
			// maps > json
			String jsonFR = new PropertiesToJsonConverter().convertToJson(combinedMapFR);
			String jsonNL = new PropertiesToJsonConverter().convertToJson(combinedMapNL);
			
			// output resulting json strings
			writeStringToPath(OUT_PARENT_PATH.resolve("fr.json"), jsonFR);
			writeStringToPath(OUT_PARENT_PATH.resolve("nl.json"), jsonNL);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
