package fr.eseoye.eseoye.io.json;

import static fr.eseoye.eseoye.io.JSONAssertion.assertInstanceof;

import java.util.HashMap;

import fr.eseoye.eseoye.io.JSONFile;
import fr.eseoye.eseoye.utils.Tuple;

public class JSONConfiguration extends JSONFile {

	public JSONConfiguration(String path) {
		super(path, "config.json");
	}

	public String getDatabaseURL() {
		return (String)getData("db_credentials").get("url");
	}
	
	public Tuple<String, String> getDatabaseCredentials() {
		return new Tuple<>((String)getData("db_credentials").get("username"),(String)getData("db_credentials").get("password"));
	}
	
	public String getStorageServerURL() {
		return (String)getData("ftp_credentials").get("url");
	}
	
	public Tuple<String, String> getStorageServerCredentials() {
		return new Tuple<>((String)getData("ftp_credentials").get("username"),(String)getData("ftp_credentials").get("password"));
	}
	
	@Override
	public void reviewFormat() {
		assertInstanceof(getData().get("db_credentials"), HashMap.class, "db_credentials");
		assertInstanceof(getData("db_credentials").get("url"), String.class, "db_credentials.url");
		assertInstanceof(getData("db_credentials").get("username"), String.class, "db_credentials.username");
		assertInstanceof(getData("db_credentials").get("password"), String.class, "db_credentials.password");
		
		assertInstanceof(getData().get("ftp_credentials"), HashMap.class, "ftp_credentials");
		assertInstanceof(getData("ftp_credentials").get("url"), String.class, "ftp_credentials.url");
		assertInstanceof(getData("ftp_credentials").get("username"), String.class, "ftp_credentials.username");
		assertInstanceof(getData("ftp_credentials").get("password"), String.class, "ftp_credentials.password");
	}
	
	@Override
	public void preSave() { }


}