package fr.eseoye.eseoye.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.eseoye.eseoye.databases.implementation.MariaDBImplementation;
import fr.eseoye.eseoye.databases.tables.PostTable;
import fr.eseoye.eseoye.databases.tables.UserTable;

public class DatabaseFactory {

	private static final String SEPARATOR = "/";
	
	private static volatile DatabaseFactory instance = null;
	
	private DatabaseFactory() { }
	
	public static DatabaseFactory getInstance() {
		if(instance == null) {
			synchronized (DatabaseFactory.class) {
				if(DatabaseFactory.instance == null) instance = new DatabaseFactory();
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

