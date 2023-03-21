package fr.eseoye.eseoye.io.ftp;

public class SFTPCredentials {

    private final String url;
    private final String username;
    private final String password;
    private final int port;

    public SFTPCredentials(String url, long port, String username, String password) {
        this.url = url;
        this.port = Long.valueOf(port).intValue();
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
