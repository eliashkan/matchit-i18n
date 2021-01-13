package be.fedasil.compare;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.Entry;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

public class I18nComparators {
	
	public static Map<String, String> getUntranslatedEntries(Map<String, String> m) {
		return m.entrySet()
				.stream()
				.filter(entry -> entry.getValue().matches("(NL_|FR_|NL -|FR -)\\s*\\w*"))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	public static Map<String, String> getLabelsMissingFromOther(Map<String, String> mapHasEntry,
	                                                            Map<String, String> other) {
		return mapHasEntry.entrySet()
				.stream()
				.filter(entry -> isFalse(other.containsKey(entry.getKey())))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	public static Map<String, String> getChangedLabels(Map<String, String> old,
	                                                   Map<String, String> updated) {
		return updated.entrySet()
				.stream()
				.filter(entry -> isFalse(entry.getValue().equals(old.get(entry.getKey()))))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	// private constructor
	private I18nComparators() { }
}
