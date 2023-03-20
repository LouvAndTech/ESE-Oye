package fr.eseoye.eseoye.io.ftp;

public class SFTPCredentials {
	
	private String url, username, password;
	private int port;
	
	public SFTPCredentials(String url, int port, String username, String password) {
		this.url = url;
		this.port = port;
		this.username = username;
		this.password = password;
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
}
