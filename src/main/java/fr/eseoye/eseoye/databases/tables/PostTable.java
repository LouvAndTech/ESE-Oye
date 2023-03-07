package fr.eseoye.eseoye.databases.tables;

import java.util.List;

import fr.eseoye.eseoye.databases.implementation.DatabaseImplementation;

public class PostTable implements ITable {

	private DatabaseImplementation dbImplementation;
	
	public PostTable(DatabaseImplementation dbImplementation) {
		this.dbImplementation = dbImplementation;
	}
	
	public List<String> fetchPosts(int postNumber, int pageNumber) {
		return null;
	}
	
	@Override
	public String getTableName() {
		return "Post";
	}

}
