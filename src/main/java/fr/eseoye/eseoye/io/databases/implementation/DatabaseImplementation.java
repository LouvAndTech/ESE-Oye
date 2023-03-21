package fr.eseoye.eseoye.io.databases.implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eseoye.eseoye.io.databases.DatabaseType;
import fr.eseoye.eseoye.utils.Tuple;

public abstract class DatabaseImplementation {
	
	public abstract void insertValues(Connection connection, String table, List<String> fields, List<Object> values) throws SQLException;
	
	public abstract void insertValues(Connection connection, String sqlRequest, List<Object> values) throws SQLException;
	
	public abstract void updateValues(Connection connection, String table, List<String> fields, List<Object> values, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException;
	
	public abstract void updateValues(Connection connection, String sqlRequest, List<Object> values) throws SQLException;
	
	public abstract ResultSet getValues(Connection connection, String table, List<String> fields) throws SQLException;
	
	public abstract ResultSet getValues(Connection connection, String table, List<String> fields, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException;
	
	public abstract ResultSet getValues(Connection connection, String sqlRequest) throws SQLException;
	
	public abstract ResultSet getValuesWithCondition(Connection connection, String sqlRequest, List<Tuple<Object, Integer>> valuesCondition) throws SQLException;
		
	public abstract int getValuesCount(Connection connection, String table, List<String> values) throws SQLException;

	public abstract int getValuesCount(Connection connection, String table, List<String> values, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException;
	
	public abstract void deleteValues(Connection connection, String table, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException;
	
	public abstract DatabaseType getDBType();
}
