package fr.eseoye.eseoye.io.databases.tables;

import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.request.DatabaseRequest;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostCategoryTable implements ITable {

    private final DatabaseFactory factory;
    private final DatabaseCredentials credentials;

    public PostCategoryTable(DatabaseFactory factory, DatabaseCredentials credentials) {
        this.factory = factory;
        this.credentials = credentials;
    }

    public List<Category> fetchAllCategory() {
        final List<Category> result = new ArrayList<>();
        try {
            ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), List.of("*"));
            while (res.next())
                result.add(new Category(res.getInt("id"), res.getString("name")));

            return result;
        } catch (SQLException e) {
            //TODO Handle exception
            return null;
        }
    }

    @Override
    public String getTableName() {
        return "Post_Category";
    }

}
