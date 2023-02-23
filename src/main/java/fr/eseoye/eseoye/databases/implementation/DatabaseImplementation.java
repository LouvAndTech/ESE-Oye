package fr.eseoye.eseoye.databases.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class DatabaseImplementation {
	
	public abstract void insertValues(String table, List<String> fields, List<String> values) throws SQLException;
	
	public abstract void insertValues(String table, String sqlRequest, List<String> values) throws SQLException;
	
	public abstract void updateValues(String table, String fields, List<String> values) throws SQLException;
	
	public abstract void updateValues(String sqlRequest, List<String> values) throws SQLException;
	
	public abstract ResultSet getValues(String table, List<String> values) throws SQLException;
	
	public abstract ResultSet getValues(String table, String sqlRequest) throws SQLException;
	
	public abstract int getValuesCount(String table, String values) throws SQLException;
	
	public abstract ResultSet join(String tableA, String tableB, String valueA, String valueB, JoinType type) throws SQLException;
	
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
	
	public enum JoinType {
		INNER,
		LEFT,
		FULL;
	}
}
