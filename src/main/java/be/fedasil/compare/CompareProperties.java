package be.fedasil.compare;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

public class CompareProperties {
	
	public static List<Map.Entry<String, String>> getUntranslated(Map<String, String> m) {
		return m.entrySet()
				.stream()
				.filter(entry -> entry.getValue().matches("(NL_|FR_)\\w*"))
				.collect(Collectors.toList());
	}
	
	public static List<String> getMissingKeys(Map<String, String> a, Map<String, String> b) {
		return a.keySet()
				.stream()
				.filter(keyA -> isFalse(b.containsKey(keyA)))
				.collect(Collectors.toList());
	}
}
