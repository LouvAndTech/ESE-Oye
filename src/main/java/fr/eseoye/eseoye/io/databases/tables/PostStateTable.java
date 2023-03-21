package fr.eseoye.eseoye.io.databases.tables;

import fr.eseoye.eseoye.beans.PostState;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.request.DatabaseRequest;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostStateTable implements ITable {

    private final DatabaseFactory factory;
    private final DatabaseCredentials credentials;

    public PostStateTable(DatabaseFactory factory, DatabaseCredentials credentials) {
        this.factory = factory;
        this.credentials = credentials;
    }

    public List<PostState> fetchAllState() {
        final List<PostState> result = new ArrayList<>();
        try {
            final ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), List.of("*"));
            while (res.next())
                result.add(new PostState(res.getInt("id"), res.getString("name")));

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO Handle exception
            return null;
        }
    }

    @Override
    public String getTableName() {
        return "Post_State";
    }

}
