package be.fedasil.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JsonToProps {
	
	public static void convertToProps(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> props = null;
		try {
			props = transformJsonToMap(objectMapper.readTree(json), null);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		assert props != null;
	}
	
	public static Map<String, String> transformJsonToMap(JsonNode node, String prefix) {
		
		Map<String, String> jsonMap = new HashMap<>();
		
		if (node.isContainerNode()) {
			String curPrefixWithDot = (prefix == null || prefix.trim().length() == 0) ? "" : prefix + ".";
			node.fieldNames().forEachRemaining(fieldName -> {
				JsonNode fieldValue = node.get(fieldName);
				jsonMap.putAll(transformJsonToMap(fieldValue, curPrefixWithDot + fieldName));
			});
			
		} else {
			jsonMap.put(prefix, node.asText());
			System.out.println(prefix + "=" + node.asText());
		}
		
		return jsonMap;
		
	}
}
