package be.fedasil.app;

import be.fedasil.util.CsvUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static be.fedasil.util.JsonUtils.transformJsonToMap;

class GenerateOutgoingTest {
	
	@Test
	void generateOutgoing() throws IOException {
		
		Map<String, String> oldFRMap = transformJsonToMap(new FileInputStream("/Users/Elias/Documents/matchit/matchit-i18n/src/main/resources/test/old/fr.json"));
		Map<String, String> oldNLMap = transformJsonToMap(new FileInputStream("/Users/Elias/Documents/matchit/matchit-i18n/src/main/resources/test/old/nl.json"));
		Map<String, String> newFRMap = transformJsonToMap(new FileInputStream("/Users/Elias/Documents/matchit/matchit-i18n/src/main/resources/test/new/fr.json"));
		Map<String, String> newNLMap = transformJsonToMap(new FileInputStream("/Users/Elias/Documents/matchit/matchit-i18n/src/main/resources/test/new/nl.json"));
		
		GenerateOutgoing.generateOutgoing(oldFRMap, oldNLMap, newFRMap, newNLMap);
		
		FileInputStream selectedNLInStream = new FileInputStream("/Users/Elias/Documents/matchit/matchit-i18n/target/generated-i18n-files/csv/combinedLabelsForReviewNL.csv");
		Map<String, String> selectedMapActualNL = CsvUtils.getMapFromCSVInputStream(selectedNLInStream);
		
		FileInputStream selectedFRInStream = new FileInputStream("/Users/Elias/Documents/matchit/matchit-i18n/target/generated-i18n-files/csv/combinedLabelsForReviewFR.csv");
		Map<String, String> selectedMapActualFR = CsvUtils.getMapFromCSVInputStream(selectedFRInStream);
		
		Map<String, String> expectedNL = new TreeMap<>();
		expectedNL.put("HomePage.anObject.someText", "veranderd tegenover oude nl");
		expectedNL.put("HomePage.NieuweToevoeging", "Deze zit niet in de fr");
		expectedNL.put("new.label1", "NL_NEDERLANDSE_VERTALING_HIER_AUB");
		expectedNL.put("new.label2", "NL_NEDERLANDSE_VERTALING_HIER_AUB");
		
		Map<String, String> expectedFR = new TreeMap<>();
		expectedFR.put("HomePage.anObject.someText", "quelque chose");
		expectedFR.put("HomePage.NieuweToevoeging", "FR_TRADUCTION_FRANCAIS_ICI_SVP");
		expectedFR.put("new.label1", "Du text a traduire");
		expectedFR.put("new.label2", "Plus a traduire");
		
		Assertions.assertEquals(expectedFR, selectedMapActualFR);
		Assertions.assertEquals(expectedNL, selectedMapActualNL);
		
		// not selected for review:
		
		Map<String, String> expectedNotSelectedNL = new TreeMap<>();
		expectedNotSelectedNL.put("logo.alt", "Match-IT");
		expectedNotSelectedNL.put("logo.browser", "Internet Explorer");
		expectedNotSelectedNL.put("HomePage.welcome", "Welkom in de applicatie van Match-IT");
		expectedNotSelectedNL.put("HomePage.anObject.hello", "Welkom!");
		
		Map<String, String> expectedNotSelectedFR = new TreeMap<>();
		expectedNotSelectedFR.put("logo.alt", "Match-IT");
		expectedNotSelectedFR.put("logo.browser", "Internet Explorer");
		expectedNotSelectedFR.put("HomePage.welcome", "Bienvenue dans l'application Match-IT");
		expectedNotSelectedFR.put("HomePage.anObject.hello", "Bienvenue");
		
		FileInputStream actualNotSelectedNLInStream = new FileInputStream("/Users/Elias/Documents/matchit/matchit-i18n/target/generated-i18n-files/csv/labelsNotSelectedForReviewNL.csv");
		Map<String, String> actualNotSelectedNLMap = CsvUtils.getMapFromCSVInputStream(actualNotSelectedNLInStream);
		
		FileInputStream actualNotSelectedFRInStream = new FileInputStream("/Users/Elias/Documents/matchit/matchit-i18n/target/generated-i18n-files/csv/labelsNotSelectedForReviewFR.csv");
		Map<String, String> actualNotSelectedFRMap = CsvUtils.getMapFromCSVInputStream(actualNotSelectedFRInStream);
		
		Assertions.assertEquals(expectedNotSelectedNL, actualNotSelectedNLMap);
		Assertions.assertEquals(expectedNotSelectedFR, actualNotSelectedFRMap);
		
	}
}
