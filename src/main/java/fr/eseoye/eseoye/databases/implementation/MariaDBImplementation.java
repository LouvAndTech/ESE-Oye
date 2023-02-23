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
	
	public MariaDBImplementation(DAOFactory factory) {
		this.factory = factory;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			//TODO better handling of errors
			e.printStackTrace();
		}
	}
	
	@Override
	public void insertValues(String table, List<String> fields, List<String> values) throws SQLException {
		Connection connection = factory.getConnection(DatabaseType.MARIADB, table); 
		PreparedStatement preparedStatement = connection
				.prepareStatement("INSERT INTO "+table+"("+convertListToDatabaseFields(fields)+") VALUES("+this.generateRequestEmptyValues(values.size())+");");
			for(int i = 0; i < values.size(); i++) preparedStatement.setString(i, values.get(i));
		preparedStatement.executeUpdate();
	}
	
	@Override
	public void insertValues(String table, String sqlRequest, List<String> values) throws SQLException {
		//TODO check sqlRequest size and values size ?
		Connection connection = factory.getConnection(DatabaseType.MARIADB, table); 
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
		Connection connexion = factory.getConnection(DatabaseType.MARIADB, table); 
		Statement statement = connexion.createStatement();
		return statement.executeQuery("SELECT "+convertListToDatabaseFields(values)+" FROM "+table+";");
	}

	@Override
	public ResultSet getValues(String table, String sqlRequest) throws SQLException {
		Connection connexion = factory.getConnection(DatabaseType.MARIADB, table); 
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
