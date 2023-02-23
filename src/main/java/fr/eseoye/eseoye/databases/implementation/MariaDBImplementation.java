package fr.eseoye.eseoye.databases.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.eseoye.eseoye.databases.DAOFactory;

public class MariaDBImplementation extends DatabaseImplementation {

	private DAOFactory factory;
	
	public MariaDBImplementation(DAOFactory factory) {
		this.factory = factory; 
	}
	
	@Override
	public void insertValues(String table, List<String> fields, List<String> values) throws SQLException {
		Connection connection = factory.getConnection(table); 
		PreparedStatement preparedStatement = connection
				.prepareStatement("INSERT INTO "+table+"("+convertListToDatabaseFields(fields)+") VALUES("+this.generateRequestEmptyValues(values.size())+");");
			for(int i = 0; i < values.size(); i++) preparedStatement.setString(i, values.get(i));
		preparedStatement.executeUpdate();
	}
	
	@Override
	public void insertValues(String table, String sqlRequest, List<String> values) throws SQLException {
		//TODO check sqlRequest size and values size ?
		Connection connection = factory.getConnection(table); 
		PreparedStatement preparedStatement = connection
				.prepareStatement(sqlRequest);
			for(int i = 0; i < values.size(); i++) preparedStatement.setString(i, values.get(i));
		preparedStatement.executeUpdate();
		
	}

	@Override
	public void updateValues(String table, String fields, List<String> values) {
		// TODO create method (easy)
		
	}

	@Override
	public void updateValues(String sqlRequest, List<String> values) {
		// TODO create method (easy)
	
	}
	
	@Override
	public ResultSet getValues(String table, List<String> values) throws SQLException {
		Connection connexion = factory.getConnection(table); 
		Statement statement = connexion.createStatement();
		return statement.executeQuery("SELECT "+convertListToDatabaseFields(values)+" FROM "+table+";");
	}

	@Override
	public ResultSet getValues(String table, String sqlRequest) throws SQLException {
		Connection connexion = factory.getConnection(table); 
		Statement statement = connexion.createStatement();
		return statement.executeQuery(sqlRequest);
	}

	@Override
	public int getValuesCount(String table, String values) {
		//TODO create method (easy)
		return 0;
	}

	@Override
	public ResultSet join(String tableA, String tableB, String valueA, String valueB, JoinType type) {
		// TODO create method (easay)
		return null;
	}

}
