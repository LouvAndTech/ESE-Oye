package fr.eseoye.eseoye.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.eseoye.eseoye.databases.implementation.MariaDBImplementation;
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
	
	public Connection getConnection(DatabaseType dbType, String table) throws SQLException {
		return DriverManager.getConnection(dbType.getBaseUrl()+SEPARATOR+table, dbType.getUsername(), dbType.getPass());
	}
	
	public UserTable getUserTable(DatabaseType type) {
		switch (type) {
		case MARIADB:
			return new UserTable(new MariaDBImplementation(this));
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

}

