package be.fedasil.app;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static be.fedasil.compare.I18nComparators.*;
import static be.fedasil.csv.MapToCSVGenerator.exportCSV;
import static be.fedasil.transformer.JsonTransformer.transformJsonToMap;
import static org.apache.commons.compress.utils.IOUtils.toByteArray;
import static picocli.CommandLine.Command;

@Command(
		name = "matchit-i18n-generate-outgoing",
		mixinStandardHelpOptions = true,
		version = "matchit-i18n 1.0",
		description = "Generate handy files for i18n purposes to share with helpdesk."
)
public class GenerateOutgoingApp implements Callable<Integer> {
	
	private static final File OUT_PARENT_PATH = Paths.get(
			".",
			"target",
			"outgoing-csv").toFile();
	
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
		Map<String, String> untranslatedFR = getUntranslatedEntries(newFRMap);
		Map<String, String> untranslatedNL = getUntranslatedEntries(newNLMap);
		exportCSV(untranslatedFR, new File(OUT_PARENT_PATH, "untranslatedFR.csv"));
		exportCSV(untranslatedNL, new File(OUT_PARENT_PATH, "untranslatedNL.csv"));
		
		// find map with new additions, missing in old versions, filter out the ones that are in untranslated, export to csv
		// IS THIS FILTER UNNECESSARY BECAUSE OF COMBINING INTO ONE MAP AFTERWARDS?
		Map<String, String> newLabelAdditionsFR = getLabelsMissingFromOther(newFRMap, oldFRMap);
		Map<String, String> newLabelAdditionsNL = getLabelsMissingFromOther(newNLMap, oldNLMap);
//		newLabelAdditionsFR = getLabelsMissingFromOther(newLabelAdditionsFR, untranslatedFR);
//		newLabelAdditionsNL = getLabelsMissingFromOther(newLabelAdditionsNL, untranslatedNL);
		exportCSV(newLabelAdditionsFR, new File(OUT_PARENT_PATH, "newLabelAdditionsFR.csv"));
		exportCSV(newLabelAdditionsNL, new File(OUT_PARENT_PATH, "newLabelAdditionsNL.csv"));
		
		// find labels that were changed and need review, filter out the ones that are in untranslated, export to csv
		// SAME FILTER QUESTION
		Map<String, String> changedLabelsFR = getChangedLabels(oldFRMap, newFRMap);
		Map<String, String> changedLabelsNL = getChangedLabels(oldNLMap, newNLMap);
//		changedLabelsFR = getLabelsMissingFromOther(changedLabelsFR, untranslatedFR);
//		changedLabelsNL = getLabelsMissingFromOther(changedLabelsNL, untranslatedNL);
		exportCSV(changedLabelsFR, new File(OUT_PARENT_PATH, "changedLabelsFR.csv"));
		exportCSV(changedLabelsNL, new File(OUT_PARENT_PATH, "changedLabelsNL.csv"));
		
		// combine all into one map per language
		Map<String, String> combinedFR = combineMaps(untranslatedFR, newLabelAdditionsFR, changedLabelsFR);
		Map<String, String> combinedNL = combineMaps(untranslatedNL, newLabelAdditionsNL, changedLabelsNL);
		
		// add missing labels to other language for reference if it's not clear what translation should be
		// some will be FR_te_vertalen_nederlandse_tekst, but others will have dummy text without reference
		Map<String, String> keysMissingFromNL = getLabelsMissingFromOther(combinedFR, combinedNL);
		Map<String, String> keysMissingFromFR = getLabelsMissingFromOther(combinedNL, combinedFR);
		keysMissingFromNL.replaceAll((k, v) -> newNLMap.get(k));
		keysMissingFromFR.replaceAll((k, v) -> newFRMap.get(k));
		combinedFR.putAll(keysMissingFromFR);
		combinedNL.putAll(keysMissingFromNL);
		
		// export combined maps to csv
		exportCSV(combinedFR, new File(OUT_PARENT_PATH, "combinedLabelsForReviewFR.csv"));
		exportCSV(combinedNL, new File(OUT_PARENT_PATH, "combinedLabelsForReviewNL.csv"));
		
		return 0;
	}
	
	@SafeVarargs
	private final Map<String, String> combineMaps(Map<String, String>... maps) {
		HashMap<String, String> combined = new HashMap<>();
		Arrays.stream(maps).forEach(combined::putAll);
		return combined;
	}
	
	private String getJsonFromResources(String path) throws IOException {
		try (InputStream in = getClass().getResourceAsStream(path)) {
			return new String(toByteArray(in));
		}
	}
}
