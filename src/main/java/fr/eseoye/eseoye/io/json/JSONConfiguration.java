package fr.eseoye.eseoye.io.json;

import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.DatabaseType;
import fr.eseoye.eseoye.io.ftp.SFTPCredentials;

import java.util.HashMap;

import static fr.eseoye.eseoye.io.json.JSONAssertion.assertInstanceof;

public class JSONConfiguration extends JSONFile {

    public JSONConfiguration(String path) {
        super(path, "config.json");
    }

    public String getDatabaseURL() {
        return (String) getData("db_credentials").get("url");
    }

    public DatabaseCredentials getDatabaseCredentials() {
        return new DatabaseCredentials(
                (String) getData("db_credentials").get("url"),
                (long) getData("db_credentials").get("port"),
                (String) getData("db_credentials").get("username"),
                (String) getData("db_credentials").get("password"),
                (String) getData("db_credentials").get("name"),
                DatabaseType.of((String) getData("db_credentials").get("type")));
    }

    public SFTPCredentials getSFTPCredentials() {
        return new SFTPCredentials(
                (String) getData("ftp_credentials").get("url"),
                (long) getData("ftp_credentials").get("port"),
                (String) getData("ftp_credentials").get("username"),
                (String) getData("ftp_credentials").get("password"));
    }

    @Override
    public void reviewFormat() {
        System.out.println(getData());
        //assertInstanceof(getData().get("db_credentials"), HashMap.class, "db_credentials");
        assertInstanceof(getData("db_credentials").get("url"), String.class, "db_credentials.url");
        assertInstanceof(getData("db_credentials").get("username"), String.class, "db_credentials.username");
        assertInstanceof(getData("db_credentials").get("password"), String.class, "db_credentials.password");

        assertInstanceof(getData().get("ftp_credentials"), HashMap.class, "ftp_credentials");
        assertInstanceof(getData("ftp_credentials").get("url"), String.class, "ftp_credentials.url");
        assertInstanceof(getData("ftp_credentials").get("username"), String.class, "ftp_credentials.username");
        assertInstanceof(getData("ftp_credentials").get("password"), String.class, "ftp_credentials.password");
    }

    @Override
    public void preSave() {
    }


}