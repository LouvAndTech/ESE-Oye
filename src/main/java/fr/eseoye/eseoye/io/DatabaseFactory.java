package fr.eseoye.eseoye.io;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.ITable;

public class DatabaseFactory {

	private static final String SEPARATOR = "/";
	
	private static volatile DatabaseFactory instance = null;
	
	private DatabaseFactory() {}
	
	public static DatabaseFactory getInstance() {
		return instance;
	}
	
	public Connection getConnection(DatabaseCredentials credentials) throws SQLException {
		return DriverManager.getConnection(
				credentials.getDatabaseType().getBaseUrl()+credentials.getFullUrl()+SEPARATOR+credentials.getDatabaseName(), 
				credentials.getUsername(), 
				credentials.getPassword());
	}
	
	public <E extends ITable> E getTable(Class<E> cls, DatabaseCredentials credentials) {
		E object = null;
		try {
			Constructor<E> constructor = cls.getConstructor(DatabaseFactory.class, DatabaseCredentials.class);
			
			object = constructor.newInstance(this, credentials);
		}catch(Exception e) {
			System.err.println(e);
		}
		
		return object;
	}
	
//	public UserTable getUserTable(DatabaseCredentials credentials) {
//		switch (credentials.getDatabaseType()) {
//		case MARIADB:
//			return new UserTable(this, credentials);
//		default:
//			throw new IllegalArgumentException("Unexpected value: " + credentials.getDatabaseType());
//		}
//	}
//	
//	public PostTable getPostTable(DatabaseCredentials credentials) {
//		switch (credentials.getDatabaseType()) {
//		case MARIADB:
//			return new PostTable(this, credentials);
//		default:
//			throw new IllegalArgumentException("Unexpected value: " + credentials.getDatabaseType());
//		}
//	}
//	
//	public PostCategoryTable getPostCategoryTable(DatabaseCredentials credentials) {
//		switch (credentials.getDatabaseType()) {
//		case MARIADB:
//			return new PostCategoryTable(this, credentials);
//		default:
//			throw new IllegalArgumentException("Unexpected value: " + credentials.getDatabaseType());
//		}
//	}
//	
//	public PostStateTable getPostStateTable(DatabaseCredentials credentials) {
//		switch (credentials.getDatabaseType()) {
//		case MARIADB:
//			return new PostStateTable(this, credentials);
//		default:
//			throw new IllegalArgumentException("Unexpected value: " + credentials.getDatabaseType());
//		}
//	}

}

