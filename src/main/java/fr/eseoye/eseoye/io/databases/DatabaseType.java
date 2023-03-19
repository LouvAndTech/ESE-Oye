package fr.eseoye.eseoye.io.databases;

import fr.eseoye.eseoye.io.databases.implementation.DatabaseImplementation;
import fr.eseoye.eseoye.io.databases.implementation.MariaDBImplementation;

public enum DatabaseType {
	
	//TODO Add connection information
	MARIADB("jdbc:mariadb://");
	
	private String baseUrl;
	private DatabaseType(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String getBaseUrl() {
		return this.baseUrl;
	}

	public DatabaseImplementation getImplementation() {
		switch(this) {
			case MARIADB:
				return new MariaDBImplementation();
			default:
				return null;
		}
	}
}
