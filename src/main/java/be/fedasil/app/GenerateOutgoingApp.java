package be.fedasil.app;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Callable;

import static be.fedasil.compare.I18nComparators.*;
import static be.fedasil.csv.MapToCSVGenerator.*;
import static be.fedasil.transformer.JsonTransformer.*;
import static org.apache.commons.compress.utils.IOUtils.toByteArray;
import static picocli.CommandLine.Command;

@Command(
		name = "matchit-i18n-generate-outgoing",
		mixinStandardHelpOptions = true,
		version = "matchit-i18n 1.0",
		description = "Generate handy files for i18n purposes to share with helpdesk."
)
public class GenerateOutgoingApp implements Callable<Integer> {
	
	private static final File OUT_PARENT_PATH = Paths.get(".",
			"target",
			"outgoing-csv").toFile();
	
	public static void main(String... args) {
		GenerateOutgoingApp outgoingApp = new GenerateOutgoingApp();
		int exitCode = new CommandLine(outgoingApp).execute(args);
		System.exit(exitCode);
	}
	
	@Override
	public Integer call() throws Exception {
		
		// get json strings from resources
		String oldFR = getJsonFromResources("/old/fr.json");
		String oldNL = getJsonFromResources("/old/nl.json");
		String newFR = getJsonFromResources("/new/fr.json");
		String newNL = getJsonFromResources("/new/nl.json");
		
		// transform them to maps
		Map<String, String> oldFRMap = transformJsonToMap(oldFR);
		Map<String, String> oldNLMap = transformJsonToMap(oldNL);
		Map<String, String> newFRMap = transformJsonToMap(newFR);
		Map<String, String> newNLMap = transformJsonToMap(newNL);
		
		// get map with entries present in new & missing in other
		Map<String, String> labelsMissingFromNewNL = getLabelsMissingFromOther(newFRMap, newNLMap);
		Map<String, String> labelsMissingFromNewFR = getLabelsMissingFromOther(newNLMap, newFRMap);
		
		// get map with new additions, missing in old versions
		Map<String, String> newLabelAdditionsFR = getLabelsMissingFromOther(newFRMap, oldFRMap);
		Map<String, String> newLabelAdditionsNL = getLabelsMissingFromOther(newNLMap, oldNLMap);
		
		// get entries that still need to be translated
		Map<String, String> untranslatedFR = getUntranslatedEntries(newFRMap);
		Map<String, String> untranslatedNL = getUntranslatedEntries(newNLMap);
		
		// export maps to individual CSV files
		exportCSV(labelsMissingFromNewFR, new File(OUT_PARENT_PATH, "keysMissingFromFR.csv"));
		exportCSV(labelsMissingFromNewNL, new File(OUT_PARENT_PATH, "keysMissingFromNL.csv"));
		
		exportCSV(newLabelAdditionsFR, new File(OUT_PARENT_PATH, "newLabelAdditionsFR.csv"));
		exportCSV(newLabelAdditionsNL, new File(OUT_PARENT_PATH, "newLabelAdditionsNL.csv"));
		
		exportCSV(untranslatedFR, new File(OUT_PARENT_PATH, "untranslatedFR.csv"));
		exportCSV(untranslatedNL, new File(OUT_PARENT_PATH, "untranslatedNL.csv"));
		
		return 0;
	}
	
	private String getJsonFromResources(String path) throws IOException {
		try (InputStream in = getClass().getResourceAsStream(path)) {
			return new String(toByteArray(in));
		}
	}
}
