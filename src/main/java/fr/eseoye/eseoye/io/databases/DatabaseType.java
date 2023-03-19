package fr.eseoye.eseoye.io.databases;

public enum DatabaseType {
	
	//TODO Add connection information
	MARIADB("jdbc:mariadb://elouan-lerissel.fr:5555","root","JR*Efj#C^8Cs4AUbBjf#XK");
	
	private String urlBase, username, pass;
	private DatabaseType(String url, String username, String pass) {
		this.urlBase = url;
		this.username = username;
		this.pass = pass;
	}
	
	public String getPass() {
		return this.pass;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getBaseUrl() {
		return this.urlBase;
	}
}
