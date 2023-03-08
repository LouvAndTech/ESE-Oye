package fr.eseoye.eseoye.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.eseoye.eseoye.databases.implementation.MariaDBImplementation;
import fr.eseoye.eseoye.databases.tables.PostTable;
import fr.eseoye.eseoye.databases.tables.UserTable;

public class DAOFactory {

	private static final String SEPARATOR = "/";
	
	private static volatile DAOFactory instance = null;
	
	private DAOFactory() { }
	
	public static DAOFactory getInstance() {
		if(instance == null) {
			synchronized (DAOFactory.class) {
				if(DAOFactory.instance == null) instance = new DAOFactory();
			}
		}
		return instance;
	}
	
	public Connection getConnection(DatabaseType dbType, String dbName) throws SQLException {
		return DriverManager.getConnection(dbType.getBaseUrl()+SEPARATOR+dbName, dbType.getUsername(), dbType.getPass());
	}
	
	public UserTable getUserTable(DatabaseType type, String databaseName) {
		switch (type) {
		case MARIADB:
			return new UserTable(new MariaDBImplementation(this, databaseName));
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}
	
	public PostTable getPostTable(DatabaseType type, String databaseName) {
		switch (type) {
		case MARIADB:
			return new PostTable(new MariaDBImplementation(this, databaseName));
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}


}

