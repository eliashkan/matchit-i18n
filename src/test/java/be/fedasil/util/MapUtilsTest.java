package be.fedasil.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

class MapUtilsTest {
	
	@Test
	void getUntranslatedLabels() {
		Map<String, String> input = new TreeMap<>();
		input.put("logo.alt", "Matchit");
		input.put("logo.browser", "Internet explorer");
		input.put("logo.nldash", "FR - FRANCAIS");
		input.put("logo.frunderscore", "FR_FRANCAIS");
		input.put("something.nldash", "NL - NEDERLANDS");
		input.put("something.more.translate", "NL_NEDERLANDS");
		
		Map<String, String> expected = new TreeMap<>();
		expected.put("logo.nldash", "FR - FRANCAIS");
		expected.put("logo.frunderscore", "FR_FRANCAIS");
		expected.put("something.nldash", "NL - NEDERLANDS");
		expected.put("something.more.translate", "NL_NEDERLANDS");
		
		Map<String, String> actual = MapUtils.getUntranslatedLabels(input);
		
		Assertions.assertEquals(actual, expected);
	}
	
	@Test
	void getLabelsMissingFromOther() {
		Map<String, String> inputHas = new TreeMap<>();
		inputHas.put("logo.alt", "Matchit");
		inputHas.put("logo.browser", "Internet explorer");
		inputHas.put("logo.nldash", "FR - FRANCAIS");
		inputHas.put("logo.frunderscore", "FR_FRANCAIS");
		inputHas.put("something.nldash", "NL - NEDERLANDS");
		inputHas.put("something.more.translate", "NL_NEDERLANDS");
		
		Map<String, String> inputMissingOther = new TreeMap<>();
		inputMissingOther.put("logo.nldash", "FR - FRANCAIS");
		inputMissingOther.put("logo.frunderscore", "FR_FRANCAIS");
		inputMissingOther.put("something.nldash", "NL - NEDERLANDS");
		inputMissingOther.put("something.more.translate", "NL_NEDERLANDS");
		
		Map<String, String> expected = new TreeMap<>();
		expected.put("logo.alt", "Matchit");
		expected.put("logo.browser", "Internet explorer");
		
		Map<String, String> actual = MapUtils.getLabelsMissingFromOther(inputHas, inputMissingOther);
		
		Assertions.assertEquals(actual, expected);
	}
	
	@Test
	void getChangedLabels() {
		Map<String, String> inputOld = new TreeMap<>();
		inputOld.put("logo.alt", "Matchit");
		inputOld.put("logo.browser", "Internet explorer");
		
		Map<String, String> inputNew = new TreeMap<>();
		inputNew.put("logo.alt", "CHANGED");
		inputNew.put("logo.browser", "Internet explorer");
		inputNew.put("logo.nldash", "FR - FRANCAIS");
		
		Map<String, String> expected = new TreeMap<>();
		expected.put("logo.alt", "CHANGED");
		
		Map<String, String> actual = MapUtils.getChangedLabels(inputOld, inputNew);
		
		Assertions.assertEquals(actual, expected);
	}
	
	@Test
	void combineMaps() {
		Map<String, String> input1 = new TreeMap<>();
		input1.put("logo.alt", "Matchit");
		input1.put("logo.browser", "Internet explorer");
		
		Map<String, String> input2 = new TreeMap<>();
		input2.put("logo.nldash", "FR - FRANCAIS");
		
		Map<String, String> input3 = new TreeMap<>();
		input3.put("logo.label", "LABEL");
		
		Map<String, String> expected = new TreeMap<>();
		expected.put("logo.alt", "Matchit");
		expected.put("logo.browser", "Internet explorer");
		expected.put("logo.nldash", "FR - FRANCAIS");
		expected.put("logo.label", "LABEL");
		
		Map<String, String> actual = MapUtils.combineMaps(input1, input2, input3);
		
		Assertions.assertEquals(actual, expected);
	}
}
