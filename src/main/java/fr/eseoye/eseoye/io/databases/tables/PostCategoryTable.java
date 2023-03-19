package fr.eseoye.eseoye.io.databases.tables;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.request.DatabaseRequest;

public class PostCategoryTable implements ITable {

	private DatabaseFactory factory;
	private DatabaseCredentials credentials;
	
	public PostCategoryTable(DatabaseFactory factory, DatabaseCredentials credentials) {
		this.factory = factory;
		this.credentials = credentials;
	}
	
	public List<Category> fetchAllCategory() {
		final List<Category> result = new ArrayList<>();
		try {
			CachedRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("*"));
			while(res.next())
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
