package fr.eseoye.eseoye.io.databases.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eseoye.eseoye.io.databases.DatabaseType;

public abstract class DatabaseImplementation {
	
	public abstract void insertValues(Connection connection, String table, List<String> fields, List<Object> values) throws SQLException;
	
	public abstract void insertValues(Connection connection, String sqlRequest, List<Object> values) throws SQLException;
	
	public abstract void updateValues(Connection connection, String table, List<String> fields, List<Object> values, String condition, List<Object> valuesCondition) throws SQLException;
	
	public abstract void updateValues(Connection connection, String sqlRequest, List<Object> values) throws SQLException;
	
	public abstract ResultSet getValues(Connection connection, String table, List<String> fields) throws SQLException;
	
	public abstract ResultSet getValues(Connection connection, String table, List<String> fields, String condition, List<Object> valuesCondition) throws SQLException;
	
	public abstract ResultSet getValues(Connection connection, String sqlRequest) throws SQLException;
	
	public abstract ResultSet getValuesWithCondition(Connection connection, String sqlRequest, List<Object> valuesCondition) throws SQLException;
	
	public abstract int getValuesCount(Connection connection, String table, List<String> values) throws SQLException;

	public abstract int getValuesCount(Connection connection, String table, List<String> values, String condition, List<Object> valuesCondition) throws SQLException;
	
	public abstract void deleteValues(Connection connection, String table, String condition, List<Object> valuesCondition) throws SQLException;
	
	protected String generateRequestEmptyValues(int valuesListSize) {
		final StringBuilder sb = new StringBuilder();
		for(int i = 0; i < valuesListSize; i++) sb.append("?, ");
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	
	protected String convertListToDatabaseFields(List<String> values) {
		if(values.size() == 1) return values.get(0);
		final StringBuilder sb = new StringBuilder();
		values.forEach(v -> sb.append("`"+v+"`, "));
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	
	protected String convertArgumentsToUpdateFields(List<String> keys) {
		final StringBuilder sb = new StringBuilder();
		for(int i = 0; i < keys.size(); i++) sb.append(keys.get(i)+"=?, ");
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	
	public abstract DatabaseType getDBType();
}
