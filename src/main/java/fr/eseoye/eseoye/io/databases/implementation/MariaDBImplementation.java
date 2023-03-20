package fr.eseoye.eseoye.io.databases.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.eseoye.eseoye.io.databases.DatabaseType;

public class MariaDBImplementation extends DatabaseImplementation {
	
	public MariaDBImplementation() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			//TODO better handling of errors
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertValues(Connection connection, String table, List<String> fields, List<Object> values) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("INSERT INTO "+table+"("+convertListToDatabaseFields(fields)+") VALUES("+this.generateRequestEmptyValues(values.size())+");");
			for(int i = 0; i < values.size(); i++) preparedStatement.setObject(i, values.get(i));
		preparedStatement.executeUpdate();
	}
	
	@Override
	public void insertValues(Connection connection, String sqlRequest, List<Object> values) throws SQLException {
		//TODO check sqlRequest size and values size ?
		PreparedStatement preparedStatement = connection
				.prepareStatement(sqlRequest);
			for(int i = 0; i < values.size(); i++) preparedStatement.setObject(i, values.get(i));
		preparedStatement.executeUpdate();
		
	}

	@Override
	public void updateValues(Connection connection, String table, List<String> fields, List<String> values, String condition, List<Object> valuesCondition) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("UPDATE "+table+" SET "+convertArgumentsToUpdateFields(fields, values)+" WHERE "+condition+";");
			for(int i = 0; i < values.size(); i++) preparedStatement.setString(i, values.get(i));
			for(int i = values.size(); i < values.size()+valuesCondition.size(); i++) preparedStatement.setObject(i, valuesCondition.get(i-values.size()));
		preparedStatement.executeUpdate();
	}

	@Override
	public void updateValues(Connection connection, String sqlRequest, List<Object> values) throws SQLException {
		insertValues(connection, sqlRequest, values);
	}
	
	@Override
	public ResultSet getValues(Connection connection, String table, List<String> fields) throws SQLException {
		Statement statement = connection.createStatement();
		return statement.executeQuery("SELECT "+convertListToDatabaseFields(fields)+" FROM "+table+";");
	}
	
	@Override
	public ResultSet getValues(Connection connection, String table, List<String> fields, String condition, List<Object> valuesCondition) throws SQLException {
		//TODO check condition size and valuesCondition size ?
		PreparedStatement statement = connection.prepareStatement("SELECT "+convertListToDatabaseFields(fields)+" FROM "+table+" WHERE "+condition+";");
		for(int i = 0; i < valuesCondition.size(); i++) statement.setObject(i, valuesCondition.get(i));
		
		return statement.executeQuery();
	}

	@Override
	public ResultSet getValues(Connection connection, String sqlRequest) throws SQLException {
		Statement statement = connection.createStatement();
		return statement.executeQuery(sqlRequest);
	}
	
	@Override
	public ResultSet getValuesWithCondition(Connection connection, String sqlRequest, List<Object> valuesCondition) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sqlRequest);
		for(int i = 0; i < valuesCondition.size(); i++) statement.setObject(i, valuesCondition.get(i));
		return statement.executeQuery(sqlRequest);
	}

	@Override
	public int getValuesCount(Connection connection, String table, List<String> columnsName) throws SQLException {
		Statement statement = connection.createStatement();
		return statement.executeQuery("SELECT COUNT("+convertListToDatabaseFields(columnsName)+") FROM "+table+";").getInt(0);
	}
	
	@Override
	public int getValuesCount(Connection connection, String table, List<String> columnsName, String condition, List<Object> valuesCondition) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT COUNT("+convertListToDatabaseFields(columnsName)+") FROM "+table+" WHERE "+condition+";");
		for(int i = 0; i < valuesCondition.size(); i++) statement.setObject(i, valuesCondition.get(i));
		return statement.executeQuery().getInt(0);
	}
	
	@Override
	public DatabaseType getDBType() {
		return DatabaseType.MARIADB;
	}

}