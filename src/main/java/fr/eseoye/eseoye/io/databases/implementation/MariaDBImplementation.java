package fr.eseoye.eseoye.io.databases.implementation;

import fr.eseoye.eseoye.helpers.RequestHelper;
import fr.eseoye.eseoye.io.databases.DatabaseType;
import fr.eseoye.eseoye.utils.Tuple;

import java.sql.*;
import java.util.List;

public class MariaDBImplementation extends DatabaseImplementation {

    public MariaDBImplementation() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //TODO better handling of errors
            e.printStackTrace();
        }
    }

    @Override
    public void insertValues(Connection connection, String table, List<String> fields, List<Object> values) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO " + table + " (" + RequestHelper.convertListToDatabaseFields(fields) + ") VALUES (" + RequestHelper.generateRequestEmptyValues(values.size()) + ");");
        for (int i = 0; i < values.size(); i++) preparedStatement.setObject(i + 1, values.get(i));
        preparedStatement.executeUpdate();
    }

    @Override
    public void insertValues(Connection connection, String sqlRequest, List<Object> values) throws SQLException {
        //TODO check sqlRequest size and values size ?
        PreparedStatement preparedStatement = connection
                .prepareStatement(sqlRequest);
        for (int i = 0; i < values.size(); i++) preparedStatement.setObject(i + 1, values.get(i));
        preparedStatement.executeUpdate();

    }

    @Override
    public void updateValues(Connection connection, String table, List<String> fields, List<Object> values, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE " + table + " SET " + RequestHelper.convertArgumentsToUpdateFields(fields) + " WHERE " + condition + ";");
        for (int i = 0; i < values.size(); i++) preparedStatement.setObject(i + 1, values.get(i));
        for (int i = values.size(); i < values.size() + valuesCondition.size(); i++)
            preparedStatement.setObject(i + 1, valuesCondition.get(i - values.size()).getValueA(), valuesCondition.get(i - values.size()).getValueB());
        preparedStatement.executeUpdate();
    }

    @Override
    public void updateValues(Connection connection, String sqlRequest, List<Object> values) throws SQLException {
        insertValues(connection, sqlRequest, values);
    }

    @Override
    public ResultSet getValues(Connection connection, String table, List<String> fields) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery("SELECT " + RequestHelper.convertListToDatabaseFields(fields) + " FROM " + table + ";");
    }

    @Override
    public ResultSet getValues(Connection connection, String table, List<String> fields, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException {
        //TODO check condition size and valuesCondition size ?
        PreparedStatement statement = connection.prepareStatement("SELECT " + RequestHelper.convertListToDatabaseFields(fields) + " FROM " + table + " WHERE " + condition + ";");
        for (int i = 0; i < valuesCondition.size(); i++)
            statement.setObject(i + 1, valuesCondition.get(i).getValueA(), valuesCondition.get(i).getValueB());

        return statement.executeQuery();
    }

    @Override
    public ResultSet getValues(Connection connection, String sqlRequest) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlRequest);
    }

    @Override
    public ResultSet getValuesWithCondition(Connection connection, String sqlRequest, List<Tuple<Object, Integer>> valuesCondition) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlRequest);
        System.out.println(sqlRequest);
        for (int i = 0; i < valuesCondition.size(); i++)
            statement.setObject(i + 1, valuesCondition.get(i).getValueA(), valuesCondition.get(i).getValueB());

        return statement.executeQuery();
    }

    @Override
    public int getValuesCount(Connection connection, String table, List<String> columnsName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet res = statement.executeQuery("SELECT COUNT('" + RequestHelper.convertListToDatabaseFields(columnsName) + "') AS cnt FROM " + table + ";");
        return res.next() ? res.getInt("cnt") : 0;
    }

    @Override
    public int getValuesCount(Connection connection, String table, List<String> columnsName, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT('" + RequestHelper.convertListToDatabaseFields(columnsName) + "') AS cnt FROM " + table + " WHERE " + condition + ";");
        for (int i = 0; i < valuesCondition.size(); i++)
            statement.setObject(i + 1, valuesCondition.get(i).getValueA(), valuesCondition.get(i).getValueB());
        ResultSet res = statement.executeQuery();
        return res.next() ? res.getInt("cnt") : 0;
    }

    @Override
    public void deleteValues(Connection connection, String table, String condition, List<Tuple<Object, Integer>> valuesCondition) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE from `" + table + "` WHERE " + condition + ";");
        for (int i = 0; i < valuesCondition.size(); i++)
            statement.setObject(i + 1, valuesCondition.get(i).getValueA(), valuesCondition.get(i).getValueB());
        statement.executeUpdate();
    }

    @Override
    public DatabaseType getDBType() {
        return DatabaseType.MARIADB;
    }

}
