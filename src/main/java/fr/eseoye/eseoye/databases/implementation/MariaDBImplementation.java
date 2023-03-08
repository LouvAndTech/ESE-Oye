package fr.eseoye.eseoye.databases.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.eseoye.eseoye.databases.DAOFactory;
import fr.eseoye.eseoye.databases.DatabaseType;

public class MariaDBImplementation extends DatabaseImplementation {

	private DAOFactory factory;
	private String dbName;
	
	public MariaDBImplementation(DAOFactory factory, String databaseName) {
		this.factory = factory;
		this.dbName = databaseName;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			//TODO better handling of errors
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertValues(String table, List<String> fields, List<String> values) throws SQLException {
		Connection connection = factory.getConnection(getDBType(), this.dbName); 
		PreparedStatement preparedStatement = connection
				.prepareStatement("INSERT INTO "+table+"("+convertListToDatabaseFields(fields)+") VALUES("+this.generateRequestEmptyValues(values.size())+");");
			for(int i = 0; i < values.size(); i++) preparedStatement.setString(i, values.get(i));
		preparedStatement.executeUpdate();
	}
	
	@Override
	public void insertValues(String sqlRequest, List<String> values) throws SQLException {
		//TODO check sqlRequest size and values size ?
		Connection connection = factory.getConnection(getDBType(), this.dbName); 
		PreparedStatement preparedStatement = connection
				.prepareStatement(sqlRequest);
			for(int i = 0; i < values.size(); i++) preparedStatement.setString(i, values.get(i));
		preparedStatement.executeUpdate();
		
	}

	@Override
	public void updateValues(String table, List<String> fields, List<String> values, String condition) throws SQLException {
		Connection connection = factory.getConnection(getDBType(), this.dbName); 
		PreparedStatement preparedStatement = connection
				.prepareStatement("UPDATE "+table+" SET "+convertArgumentsToUpdateFields(fields, values)+" WHERE "+condition+";");
			for(int i = 0; i < values.size(); i++) preparedStatement.setString(i, values.get(i));
		preparedStatement.executeUpdate();
	}

	@Override
	public void updateValues(String sqlRequest, List<String> values) throws SQLException {
		insertValues(sqlRequest, values);
	}
	
	@Override
	public ResultSet getValues(String table, List<String> values) throws SQLException {
		Connection connexion = factory.getConnection(getDBType(), table); 
		Statement statement = connexion.createStatement();
		return statement.executeQuery("SELECT "+convertListToDatabaseFields(values)+" FROM "+table+";");
	}
	
	@Override
	public ResultSet getValues(String table, List<String> values, String condition) throws SQLException {
		Connection connexion = factory.getConnection(getDBType(), table); 
		Statement statement = connexion.createStatement();
		return statement.executeQuery("SELECT "+convertListToDatabaseFields(values)+" FROM "+table+" WHERE "+condition+";");
	}

	@Override
	public ResultSet getValues(String sqlRequest) throws SQLException {
		Connection connexion = factory.getConnection(getDBType(), this.dbName); 
		Statement statement = connexion.createStatement();
		return statement.executeQuery(sqlRequest);
	}

	@Override
	public int getValuesCount(String table, String columnName) throws SQLException {
		final Connection connection = factory.getConnection(getDBType(), dbName);
		Statement statement = connection.createStatement();
		return statement.executeQuery("SELECT COUNT("+columnName+") FROM "+table+";").getInt(0);
	}
	
	@Override
	public DatabaseType getDBType() {
		return DatabaseType.MARIADB;
	}

}
