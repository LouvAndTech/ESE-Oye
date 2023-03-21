package fr.eseoye.eseoye.io.databases;

import fr.eseoye.eseoye.io.databases.implementation.DatabaseImplementation;
import fr.eseoye.eseoye.io.databases.implementation.MariaDBImplementation;

import java.util.Arrays;

public enum DatabaseType {

    //TODO Add connection information
    MARIADB("jdbc:mariadb://");

    private final String baseUrl;

    DatabaseType(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public DatabaseImplementation getImplementation() {
        if (this == DatabaseType.MARIADB) {
            return new MariaDBImplementation();
        }
        return null;
    }

    public static DatabaseType of(String name) {
        return Arrays.asList(DatabaseType.values()).stream().filter(type -> type.toString().equalsIgnoreCase(name)).findFirst().get();
    }
}
