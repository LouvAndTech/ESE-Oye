package fr.eseoye.eseoye.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {

	private static final String SEPARATOR = "/";
	
	private static volatile DAOFactory instance = null;
	
	private String URL;
	private String user;
	private String password;

	private DAOFactory() { }
	
	public static DAOFactory getInstance() {
		if(instance == null) {
			synchronized (DAOFactory.class) {
				if(DAOFactory.instance == null) instance = new DAOFactory();
			}
		}
		return instance;
	}
	
	public Connection getConnection(String table) throws SQLException {
		return DriverManager.getConnection(this.URL+SEPARATOR+table, this.user, this.password);
	}
	
//	private void setMariaDBParameters(String url, String username, String password) {
//		this.URL = url;
//		this.user = username;
//		this.password = password;
//		try {
//			Class.forName("org.mariadb.jdbc.Driver");
//		}catch(ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public IDAONames getNamesDAO(DatabaseType type) {
//		switch (type) {
//		case MARIADB:
//			setMariaDBParameters("jdbc:mariadb://localhost:3306/td5", "root", "clementest49");
//			return new NamesMariaDBImplementation(this);
//		default:
//			return null;
//		}
//	}

}

