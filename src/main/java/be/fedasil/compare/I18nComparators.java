package be.fedasil.compare;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.*;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

public class I18nComparators {
	
	private I18nComparators() { }
	
	public static Map<String, String> getUntranslatedEntries(Map<String, String> m) {
		return m.entrySet()
				.stream()
				.filter(entry -> entry.getValue().matches("(NL_|FR_|NL -|FR -)\\s*\\w*"))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	public static Map<String, String> getKeysMissingFromOther(Map<String, String> a, Map<String, String> other) {
		return a.entrySet()
				.stream()
				.filter(entryA -> isFalse(other.containsKey(entryA.getKey())))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
}
