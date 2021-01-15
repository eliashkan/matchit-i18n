package be.fedasil.app;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static be.fedasil.util.CsvUtils.exportCSV;
import static be.fedasil.excel.ExcelWorksheetGenerator.generateXLS;
import static be.fedasil.excel.ExcelWorksheetGenerator.generateXLSX;
import static be.fedasil.util.JsonUtils.transformJsonToMap;
import static be.fedasil.util.MapUtils.*;
import static org.apache.commons.compress.utils.IOUtils.toByteArray;
import static picocli.CommandLine.Command;

@Command(
		name = "matchit-i18n-generate-outgoing",
		mixinStandardHelpOptions = true,
		version = "matchit-i18n 1.0",
		description = "Generate handy files for i18n purposes to share with helpdesk."
)
public class GenerateOutgoingApp implements Callable<Integer> {
	
	private static final File OUT_PARENT_PATH = Paths.get(".", "target", "generated-i18n-files").toFile();
	private static final File OUT_PARENT_PATH_CSV = Paths.get(OUT_PARENT_PATH.getPath(), "csv").toFile();
	private static final File RESOURCES_PATH = Paths.get(".", "src", "main", "resources").toFile();
	
	
	public static void main(String... args) {
		int exitCode = new CommandLine(new GenerateOutgoingApp()).execute(args);
		System.exit(exitCode);
	}
	
	@Override
	public Integer call() throws Exception {
		
		// get json strings from resources and transform to maps
		Map<String, String> oldFRMap = transformJsonToMap(getJsonFromResources("/old/fr.json"));
		Map<String, String> oldNLMap = transformJsonToMap(getJsonFromResources("/old/nl.json"));
		Map<String, String> newFRMap = transformJsonToMap(getJsonFromResources("/new/fr.json"));
		Map<String, String> newNLMap = transformJsonToMap(getJsonFromResources("/new/nl.json"));
		
		// get map with labels present in new & missing in other language
		Map<String, String> labelsMissingFromNewNL = getLabelsMissingFromOther(newFRMap, newNLMap);
		Map<String, String> labelsMissingFromNewFR = getLabelsMissingFromOther(newNLMap, newFRMap);
		
		// replace values with "TRANSLATION HERE PLEASE" dummy text
		labelsMissingFromNewFR.replaceAll((k, v) -> "FR_TRADUCTION_FRANCAIS_ICI_SVP");
		labelsMissingFromNewNL.replaceAll((k, v) -> "NL_NEDERLANDSE_VERTALING_HIER_AUB");
		
		// add missing keys to other language's map
		newFRMap.putAll(labelsMissingFromNewFR);
		newNLMap.putAll(labelsMissingFromNewNL);
		
		// find labels that still need to be translated, export to csv
		Map<String, String> untranslatedFR = getUntranslatedLabels(newFRMap);
		Map<String, String> untranslatedNL = getUntranslatedLabels(newNLMap);
		exportCSV(untranslatedFR, new File(OUT_PARENT_PATH_CSV, "untranslatedFR.csv"));
		exportCSV(untranslatedNL, new File(OUT_PARENT_PATH_CSV, "untranslatedNL.csv"));
		
		// find labels with new additions, missing in old versions, export to csv
		Map<String, String> newLabelAdditionsFR = getLabelsMissingFromOther(newFRMap, oldFRMap);
		Map<String, String> newLabelAdditionsNL = getLabelsMissingFromOther(newNLMap, oldNLMap);
		exportCSV(newLabelAdditionsFR, new File(OUT_PARENT_PATH_CSV, "newLabelAdditionsFR.csv"));
		exportCSV(newLabelAdditionsNL, new File(OUT_PARENT_PATH_CSV, "newLabelAdditionsNL.csv"));
		
		// find labels that were changed and need review, export to csv
		Map<String, String> changedLabelsFR = getChangedLabels(oldFRMap, newFRMap);
		Map<String, String> changedLabelsNL = getChangedLabels(oldNLMap, newNLMap);
		exportCSV(changedLabelsFR, new File(OUT_PARENT_PATH_CSV, "changedLabelsFR.csv"));
		exportCSV(changedLabelsNL, new File(OUT_PARENT_PATH_CSV, "changedLabelsNL.csv"));
		
		// combine all into one map per language
		Map<String, String> combinedFR = combineMaps(untranslatedFR, newLabelAdditionsFR, changedLabelsFR);
		Map<String, String> combinedNL = combineMaps(untranslatedNL, newLabelAdditionsNL, changedLabelsNL);
		
		// add missing labels to other language's combined map for reference (if it's not clear what translation should be)
		// some will be FR_te_vertalen_nederlandse_tekst, but others will have dummy text without reference
		Map<String, String> labelsMissingFromNL = getLabelsMissingFromOther(combinedFR, combinedNL);
		Map<String, String> labelsMissingFromFR = getLabelsMissingFromOther(combinedNL, combinedFR);
		labelsMissingFromNL.replaceAll((k, v) -> newNLMap.get(k));
		labelsMissingFromFR.replaceAll((k, v) -> newFRMap.get(k));
		combinedFR.putAll(labelsMissingFromFR);
		combinedNL.putAll(labelsMissingFromNL);
		
		// export combined maps to csv
		exportCSV(combinedFR, new File(OUT_PARENT_PATH_CSV, "combinedLabelsForReviewFR.csv"));
		exportCSV(combinedNL, new File(OUT_PARENT_PATH_CSV, "combinedLabelsForReviewNL.csv"));
		
		// find labels that are not in combined maps, export to csv in target and resources to make sure they are not lost after mvn clean
		Map<String, String> labelsNotSelectedForReviewFR = getLabelsMissingFromOther(newFRMap, combinedFR);
		Map<String, String> labelsNotSelectedForReviewNL = getLabelsMissingFromOther(newNLMap, combinedNL);
		exportCSV(labelsNotSelectedForReviewFR, new File(OUT_PARENT_PATH_CSV, "labelsNotSelectedForReviewFR.csv"));
		exportCSV(labelsNotSelectedForReviewNL, new File(OUT_PARENT_PATH_CSV, "labelsNotSelectedForReviewNL.csv"));
		exportCSV(labelsNotSelectedForReviewFR, new File(RESOURCES_PATH, "labelsNotSelectedForReviewFR.csv"));
		exportCSV(labelsNotSelectedForReviewNL, new File(RESOURCES_PATH, "labelsNotSelectedForReviewNL.csv"));
		
		// combine maps into one map for easier identification of each its language
		Map<String, Map<String, String>> dictionary = new HashMap<>();
		dictionary.put("FR", combinedFR);
		dictionary.put("NL", combinedNL);
		
		// export combined excel, in .xls and .xlsx for max compatibility
		generateXLS(dictionary, new File(OUT_PARENT_PATH, "labelsForReview.xls"));
		generateXLSX(dictionary, new File(OUT_PARENT_PATH, "labelsForReview.xlsx"));
		
		return 0;
	}
	
	private String getJsonFromResources(String path) throws IOException {
		try (InputStream in = getClass().getResourceAsStream(path)) {
			return new String(toByteArray(in));
		}
	}
}
