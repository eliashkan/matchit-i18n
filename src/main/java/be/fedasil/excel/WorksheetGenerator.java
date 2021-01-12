package be.fedasil.excel;

import java.util.Map;

@FunctionalInterface
interface WorksheetGenerator {
	
	void generate(Map<String, String> map);
	
}
