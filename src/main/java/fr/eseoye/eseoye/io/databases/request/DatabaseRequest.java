package fr.eseoye.eseoye.io.databases.request;

import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.implementation.DatabaseImplementation;
import fr.eseoye.eseoye.utils.Tuple;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseRequest {

    private final DatabaseImplementation dbImplementation;
    private Connection dbConnection;

    private final DatabaseFactory factory;
    private final DatabaseCredentials credentials;

    private final boolean instantClose;

    public DatabaseRequest(DatabaseFactory factory, DatabaseCredentials credentials, boolean instantClose) throws SQLException {
        this.dbImplementation = credentials.getDatabaseType().getImplementation();
        this.factory = factory;
        this.credentials = credentials;

        this.openConnection();

        this.instantClose = instantClose;
    }

    public DatabaseRequest(DatabaseFactory factory, DatabaseCredentials credentials) throws SQLException {
        this(factory, credentials, false);
    }

    public void openConnection() throws SQLException {
        if (this.dbConnection != null && !this.dbConnection.isClosed())
            throw new SQLException("Couldn't open an already opened connection to the database");
        this.dbConnection = factory.getConnection(this.credentials);
    }

    public void insertValues(String table, List<String> fields, List<Object> values) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        this.dbImplementation.insertValues(dbConnection, table, fields, values);

        if (instantClose) this.dbConnection.close();
    }

    public void insertValues(String sqlRequest, List<Object> values) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        this.dbImplementation.updateValues(dbConnection, sqlRequest, values);

        if (instantClose) this.dbConnection.close();
    }

    public void updateValues(String table, List<String> fields, List<Object> values, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        this.dbImplementation.updateValues(dbConnection, table, fields, values, condition, valuesCondition);

        if (instantClose) this.dbConnection.close();
    }

    public void updateValues(String sqlRequest, List<Object> values) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        this.dbImplementation.updateValues(dbConnection, sqlRequest, values);

        if (instantClose) this.dbConnection.close();
    }

    public ResultSetWrappingSqlRowSet getValues(String table, List<String> fields) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        final ResultSetWrappingSqlRowSet result = new ResultSetWrappingSqlRowSet(this.dbImplementation.getValues(dbConnection, table, fields));

        if (instantClose) this.dbConnection.close();

        return result;
    }

    public ResultSetWrappingSqlRowSet getValues(String table, List<String> fields, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        final ResultSetWrappingSqlRowSet result = new ResultSetWrappingSqlRowSet(this.dbImplementation.getValues(dbConnection, table, fields, condition, valuesCondition));

        if (instantClose) this.dbConnection.close();

        return result;
    }

    public ResultSetWrappingSqlRowSet getValues(String sqlRequest) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        final ResultSetWrappingSqlRowSet result = new ResultSetWrappingSqlRowSet(this.dbImplementation.getValues(dbConnection, sqlRequest));

        if (instantClose) this.dbConnection.close();

        return result;
    }

    public ResultSetWrappingSqlRowSet getValuesWithCondition(String sqlRequest, List<Tuple<Object, Integer>> valuesCondition) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        final ResultSetWrappingSqlRowSet result = new ResultSetWrappingSqlRowSet(this.dbImplementation.getValuesWithCondition(dbConnection, sqlRequest, valuesCondition));

        if (instantClose) this.dbConnection.close();

        return result;
    }

    public int getValuesCount(String table, List<String> values) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        int result = this.dbImplementation.getValuesCount(dbConnection, table, values);

        if (instantClose) this.dbConnection.close();

        return result;
    }

    public int getValuesCount(String table, List<String> values, String condition, List<Tuple<Object, Integer>> conditionValues) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        int result = this.dbImplementation.getValuesCount(dbConnection, table, values, condition, conditionValues);

        if (instantClose) this.dbConnection.close();

        return result;
    }

    public void deleteValues(String table, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException {
        if (this.dbConnection.isClosed())
            throw new SQLException("Couldn't execute the method because no connection to the database was found.");
        this.dbImplementation.deleteValues(dbConnection, table, condition, valuesCondition);

        if (instantClose) this.dbConnection.close();
    }

    public void closeConnection() throws SQLException {
        if (this.dbConnection != null && this.dbConnection.isClosed())
            throw new SQLException("Couldn't close an already closed connection to the database");
        this.dbConnection.close();
    }

}
