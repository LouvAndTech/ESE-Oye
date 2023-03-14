package fr.eseoye.eseoye.databases.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.eseoye.eseoye.beans.Post;
import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.beans.User;
import fr.eseoye.eseoye.databases.implementation.DatabaseImplementation;

public class PostTable implements ITable {

	private static final String userTableName = "User";
	
	private DatabaseImplementation db;
	
	public PostTable(DatabaseImplementation dbImplementation) {
		this.db = dbImplementation;
	}
	
	public List<Post> fetchShortPost(int postNumber, int pageNumber) {
		final List<Post> post = new ArrayList<>();
		try {
			final ResultSet res = db.getValues("SELECT "+getTableName()+".secure_id, "+getTableName()+".title, "+userTableName+".name, "+getTableName()+".price, "+getTableName()+".date FROM "+getTableName()+" INNER JOIN "+userTableName+" ON "+getTableName()+".user = "+userTableName+".id LIMIT "+postNumber+" OFFSET "+(pageNumber*postNumber)+";");
			while(res.next()) {
				final User u = new User(null, res.getString("name"), null, null, null, null, null, null);
				post.add(new Post(res.getString("secure_id"), res.getString("title"), u, res.getInt("price"), res.getDate("date")));
			}
		} catch (SQLException e) {
			//TODO Handle exception
			return null;
		}
		return post;
	}
	
	public PostComplete fetchEntirePost(String postID) {
		PostComplete pc = null;
		try {
			final ResultSet res = db.getValues("SELECT "+getTableName()+".secure_id, "+getTableName()+".title, "+userTableName+".name, "+getTableName()+".price, "+getTableName()+".date, "+getTableName()+".content FROM "+getTableName()+" INNER JOIN "+userTableName+" ON "+getTableName()+".user = "+userTableName+".id WHERE "+getTableName()+".secure_id = "+postID);
			if(res.next()) {
				final User u = new User(null, res.getString("name"), null, null, null, null, null, null);
				pc = new PostComplete(res.getString("secure_id"), res.getString("title"), u, res.getFloat("price"), res.getDate("date"), res.getString("content"));
			}
		} catch (SQLException e) {
			//TODO Handle exception
			return null;
		}
		return pc;
	}
	
	@Override
	public String getTableName() {
		return "Post";
	}

}
