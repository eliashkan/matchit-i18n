package be.fedasil.compare;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

public class I18nComparators {
	
	private I18nComparators() { }
	
	public static List<Map.Entry<String, String>> getUntranslatedEntries(Map<String, String> m) {
		return m.entrySet()
				.stream()
				.filter(entry -> entry.getValue().matches("(NL_|FR_|NL -|FR -)\\s*\\w*"))
				.collect(Collectors.toList());
	}
	
	public static List<String> getKeysMissingFromOther(Map<String, String> a, Map<String, String> other) {
		return a.keySet()
				.stream()
				.filter(keyA -> isFalse(other.containsKey(keyA)))
				.collect(Collectors.toList());
	}
}
