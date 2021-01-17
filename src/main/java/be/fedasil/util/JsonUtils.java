package be.fedasil.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JsonUtils {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	// private constructor
	private JsonUtils() {
	}
	
	public static Map<String, String> transformJsonToMap(String resourcePath) {
		Map<String, String> propsMap = null;
		try {
			String json = getStringFromResources(resourcePath);
			JsonNode jsonNode = objectMapper.readTree(json);
			propsMap = transformJsonNodeToMap(jsonNode, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propsMap;
	}
	
	private static String getStringFromResources(String path) throws IOException {
		try (InputStream in = JsonUtils.class.getResourceAsStream(path)) {
			return IOUtils.toString(in, UTF_8);
		}
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
	
	public static void writeStringToPath(Path path, String string) throws IOException {
		createTargetDir(path.toFile());
		Files.write(path, string.getBytes(UTF_8));
	}
	
	static void createTargetDir(File file) {
		try {
			file.getParentFile().mkdirs();
		} catch (SecurityException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
}
