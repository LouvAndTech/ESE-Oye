package fr.eseoye.eseoye.io.databases;

public class DatabaseCredentials {

	private String url, username, password;
	private int port;
	
	private DatabaseType type;
	private String databaseName;
	
	public DatabaseCredentials(String url, long port, String username, String password, String databaseName, DatabaseType type) {
		this.url = url;
		this.port = Long.valueOf(port).intValue();
		this.username = username;
		this.password = password;
		
		this.databaseName = databaseName;
		this.type = type;
	}
	
	public String getFullUrl() {
		return url+":"+port;
	}
	
	public String getUrl() {
		return url;
	}

	public int getPort() {
		return port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}
	
	public DatabaseType getDatabaseType() {
		return type;
	}
}
