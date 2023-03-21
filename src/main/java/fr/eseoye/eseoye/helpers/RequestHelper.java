package fr.eseoye.eseoye.helpers;

import java.util.List;

public class RequestHelper {

	public static String generateRequestEmptyValues(int valuesListSize) {
		final StringBuilder sb = new StringBuilder();
		for(int i = 0; i < valuesListSize; i++) sb.append("?, ");
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	
	public static String convertListToDatabaseFields(List<String> values) {
		if(values.size() == 1) return values.get(0);
		final StringBuilder sb = new StringBuilder();
		values.forEach(v -> sb.append("`"+v+"`, "));
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	
	public static String convertArgumentsToUpdateFields(List<String> keys) {
		final StringBuilder sb = new StringBuilder();
		for(int i = 0; i < keys.size(); i++) sb.append("`"+keys.get(i)+"`=?, ");
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	
}
