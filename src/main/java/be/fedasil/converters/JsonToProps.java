package be.fedasil.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JsonToProps {
	
	private JsonToProps() {	}
	
	public static Properties convertToProperties(String json) {
		Map<String, String> propsMap = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(json);
			propsMap = transformJsonToMap(jsonNode, null);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Properties properties = new Properties();
		properties.putAll(propsMap);
		return properties;
	}
	
	private static Map<String, String> transformJsonToMap(JsonNode node, String prefix) {
		
		Map<String, String> jsonMap = new HashMap<>();
		
		if (node.isContainerNode()) {
			String curPrefixWithDot = (prefix == null || prefix.trim().length() == 0) ? "" : prefix + ".";
			node.fieldNames().forEachRemaining(fieldName -> {
				JsonNode fieldValue = node.get(fieldName);
				jsonMap.putAll(transformJsonToMap(fieldValue, curPrefixWithDot + fieldName));
			});
			
		} else {
			jsonMap.put(prefix, node.asText());
		}
		
		return jsonMap;
	}
}
