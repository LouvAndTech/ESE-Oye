package fr.eseoye.eseoye.databases.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eseoye.eseoye.databases.DatabaseType;

public abstract class DatabaseImplementation {
	
	public abstract void insertValues(String table, List<String> fields, List<String> values) throws SQLException;
	
	public abstract void insertValues(String sqlRequest, List<String> values) throws SQLException;
	
	public abstract void updateValues(String table, List<String> fields, List<String> values, String condition) throws SQLException;
	
	public abstract void updateValues(String sqlRequest, List<String> values) throws SQLException;
	
	public abstract ResultSet getValues(String table, List<String> values) throws SQLException;
	
	public abstract ResultSet getValues(String table, List<String> values, String condition) throws SQLException;
	
	public abstract ResultSet getValues(String sqlRequest) throws SQLException;
	
	public abstract int getValuesCount(String table, String values) throws SQLException;
	
	protected String generateRequestEmptyValues(int valuesListSize) {
		final StringBuilder sb = new StringBuilder();
		for(int i = 0; i < valuesListSize; i++) sb.append("?, ");
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	
	protected String convertListToDatabaseFields(List<String> values) {
		final StringBuilder sb = new StringBuilder();
		values.forEach(v -> sb.append(v+", "));
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	
	protected String convertArgumentsToUpdateFields(List<String> keys, List<String> values) {
		if(keys.size() != values.size()) return null;
		final StringBuilder sb = new StringBuilder();
		for(int i = 0; i < keys.size(); i++) sb.append(keys.get(i)+"="+values.get(i)+", ");
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	
	public abstract DatabaseType getDBType();
}
