package be.fedasil.excel;

import java.util.List;
import java.util.Map;

@FunctionalInterface
interface WorksheetGenerator {

	void generate(List<Map.Entry<String, String>> entries);

}
