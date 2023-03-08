package fr.eseoye.eseoye.databases.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import fr.eseoye.eseoye.databases.implementation.DatabaseImplementation;

public class UserDAO implements IDAO {

	private DatabaseImplementation dbImplementation;
	
	public UserDAO(DatabaseImplementation dbImplementation) {
		this.dbImplementation = dbImplementation;
	}
	
	//Example method - this method will be reworked and more methods will be added
	public Date getUserBirthDate(String name, String surname) {
		try {
			ResultSet set = dbImplementation.getValues(getTableName(), Arrays.asList("birth"));
			return set.getDate("birth");
		} catch (SQLException e) {
			e.fillInStackTrace();
		}
		return null;
	}

	@Override
	public String getTableName() {
		return "User";
	}
}
