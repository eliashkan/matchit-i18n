package be.fedasil.util;

import java.util.Map;

import static java.util.Map.Entry;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

public class MapUtils {
	
	public static Map<String, String> getUntranslatedEntries(Map<String, String> m) {
		return m.entrySet()
				.stream()
				.filter(entry -> entry.getValue().matches("(NL_|FR_|NL -|FR -).*"))
				.collect(toMap(Entry::getKey, Entry::getValue));
	}
	
	public static Map<String, String> getLabelsMissingFromOther(Map<String, String> mapHasEntry,
	                                                            Map<String, String> other) {
		return mapHasEntry.entrySet()
				.stream()
				.filter(entry -> isFalse(other.containsKey(entry.getKey())))
				.collect(toMap(Entry::getKey, Entry::getValue));
	}
	
	public static Map<String, String> getChangedLabels(Map<String, String> old,
	                                                   Map<String, String> updated) {
		return updated.entrySet()
				.stream()
				.filter(entry -> old.containsKey(entry.getKey()) &&
						isFalse(entry.getValue().equals(old.get(entry.getKey()))))
				.collect(toMap(Entry::getKey, Entry::getValue));
	}
	
	// private constructor
	private MapUtils() { }
}
