package be.fedasil.util;

import be.fedasil.app.GenerateOutgoing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.TreeMap;

class JsonUtilsTest {
	
	@Test
	void transformJsonToMap() throws FileNotFoundException {
		TreeMap<String, String> expected = new TreeMap<>();
		expected.put("logo.alt", "Match-IT");
		expected.put("logo.browser", "Internet Explorer");
		expected.put("HomePage.welcome", "Bienvenue dans l'application Match-IT");
		expected.put("HomePage.anObject.someText", "Something to fill up the object");
		expected.put("HomePage.anObject.hello", "Here's a greeting");
		
		Map<String, String> actual = JsonUtils.transformJsonToMap(GenerateOutgoing.class.getResourceAsStream("/test/test-fr.json"));
		
		Assertions.assertEquals(actual, expected);
	}
}
