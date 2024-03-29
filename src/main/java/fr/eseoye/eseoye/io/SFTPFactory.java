package fr.eseoye.eseoye.io;

import fr.eseoye.eseoye.io.ftp.SFTPConnection;
import fr.eseoye.eseoye.io.ftp.SFTPCredentials;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.IOException;

public class SFTPFactory {

    private static volatile SFTPFactory instance = null;

    private final SFTPCredentials credentials;

    private SFTPFactory(SFTPCredentials credentials) {
        this.credentials = credentials;
    }

    public static SFTPFactory getInstance() {
        return instance;
    }

    public static SFTPFactory createInstance(SFTPCredentials credentials) {
        if (instance == null) {
            synchronized (SFTPFactory.class) {
                if (SFTPFactory.instance == null) instance = new SFTPFactory(credentials);
            }
        }
        return instance;
    }

    public SSHClient createSSHClient() throws IOException {
        final SSHClient client = new SSHClient();
        client.setTimeout(120);
        client.loadKnownHosts();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect(credentials.getUrl(), credentials.getPort());
        client.authPassword(credentials.getUsername(), credentials.getPassword());

        return client;
    }

    public SFTPConnection createNewConnection() {
        return new SFTPConnection(this);
    }


}
