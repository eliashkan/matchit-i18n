package be.fedasil.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.compress.utils.IOUtils.toByteArray;

public class JsonTransformer {
	
	public static Properties transformJsonToProperties(String json) {
		Map<String, String> propsMap = transformJsonToMap(json);
		Properties properties = new Properties();
		properties.putAll(propsMap);
		return properties;
	}
	
	public static Map<String, String> transformJsonToMap(String json) {
		Map<String, String> propsMap = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(json);
			propsMap = transformJsonNodeToMap(jsonNode, null);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return propsMap;
	}
	
	private static Map<String, String> transformJsonNodeToMap(JsonNode node, String prefix) {
		
		Map<String, String> jsonMap = new HashMap<>();
		
		if (node.isContainerNode()) {
			String curPrefixWithDot = (prefix == null || prefix.trim().length() == 0) ? "" : prefix + ".";
			node.fieldNames().forEachRemaining(fieldName -> {
				JsonNode fieldValue = node.get(fieldName);
				String fullPrefix = curPrefixWithDot + fieldName;
				Map<String, String> nodeToMap = transformJsonNodeToMap(fieldValue, fullPrefix);
				jsonMap.putAll(nodeToMap);
			});
		} else {
			jsonMap.put(prefix, node.asText());
		}
		
		return jsonMap;
	}
	
	// private constructor
	private JsonTransformer() {	}
}
