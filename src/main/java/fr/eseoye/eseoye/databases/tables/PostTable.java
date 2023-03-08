package fr.eseoye.eseoye.databases.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.eseoye.eseoye.beans.Post;
import fr.eseoye.eseoye.beans.PostComplete;
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
			final ResultSet res = db.getValues("SELECT "+getTableName()+".id, "+getTableName()+".title, "+userTableName+".name, "+getTableName()+".price, "+getTableName()+".date FROM "+getTableName()+" INNER JOIN "+userTableName+" ON "+getTableName()+".id = "+userTableName+".id LIMIT "+postNumber+" OFFSET "+(pageNumber*postNumber)+";");
			while(res.next()) 
				post.add(new Post(res.getInt("id"), res.getString("title"), res.getString("name"), res.getInt("price"), res.getDate("date")));
		} catch (SQLException e) {
			//TODO Handle exception
			return null;
		}
		return post;
	}
	
	public PostComplete fetchEntirePost(int postID) {
		PostComplete pc = null;
		try {
			final ResultSet res = db.getValues("SELECT "+getTableName()+".id, "+getTableName()+".title, "+userTableName+".name, "+getTableName()+".price, "+getTableName()+".date, "+getTableName()+".content FROM "+getTableName()+" INNER JOIN "+userTableName+" ON "+getTableName()+".id = "+userTableName+".id WHERE "+getTableName()+".id = "+postID);
			pc = new PostComplete(res.getInt("id"), res.getString("title"), res.getString("name"), res.getFloat("price"), res.getDate("date"), res.getString("content"));
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
