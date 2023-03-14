package fr.eseoye.eseoye.databases.tables;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import fr.eseoye.eseoye.databases.implementation.DatabaseImplementation;
import fr.eseoye.eseoye.utils.Ternary;
import fr.eseoye.eseoye.utils.Tuple;

public class UserTable implements ITable {
	
	private DatabaseImplementation dbImplementation;
	
	public UserTable(DatabaseImplementation dbImplementation) {
		this.dbImplementation = dbImplementation;
	}
	
	public boolean setNameSurname(String secureID, String newName, String newSurname) {
		try {
			dbImplementation.updateValues(getTableName(), Arrays.asList("name","surname"), Arrays.asList(newName, newSurname), "secure_id = "+secureID);
		} catch (SQLException e) {
			//TODO Handle exception correctly
			return false;
		}
		return true;
	}
	
	public Tuple<String, String> getNameSurname(String secureID) {
		try {
			ResultSet res = dbImplementation.getValues(getTableName(), Arrays.asList("name","surname"), "secure_id = "+secureID);
			if(res.next())
				return new Tuple<>(res.getString("name"), res.getString("surname"));
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return new Tuple<>();
	}
	
	public boolean setPassword(String secureID, String newPassword) {
		try {
			dbImplementation.updateValues(getTableName(), Arrays.asList("password"), Arrays.asList(newPassword), "secure_id = "+secureID);
		} catch (SQLException e) {
			//TODO Handle exception correctly
			return false;
		}
		return true;
	}
	
	public String getPassword(String secureID) {
		try {
			ResultSet set = dbImplementation.getValues(getTableName(), Arrays.asList("password"), "secure_id = "+secureID);
			return set.getString("password");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public boolean setPhoneNumber(String secureID, String newNumber) {
		try {
			dbImplementation.updateValues(getTableName(), Arrays.asList("phone"), Arrays.asList(newNumber), "secure_id = "+secureID);
		} catch (SQLException e) {
			//TODO Handle exception correctly
			return false;
		}
		return true;
	}
	
	public String getPhoneNumber(String secureID) {
		try {
			ResultSet set = dbImplementation.getValues(getTableName(), Arrays.asList("phone"), "secure_id = "+secureID);
			return set.getString("phone");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public boolean setBirthDate(String secureID, String newBirthDate) {
		try {
			dbImplementation.updateValues(getTableName(), Arrays.asList("birth"), Arrays.asList(newBirthDate), "secure_id = "+secureID);
		} catch (SQLException e) {
			//TODO Handle exception correctly
			return false;
		}
		return true;
	}

	public Date getBirthDate(String secureID) {
		try {
			ResultSet set = dbImplementation.getValues(getTableName(), Arrays.asList("birth"), "secure_id = "+secureID);
			return set.getDate("birth");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public boolean setMail(String secureID, String newMail) {
		try {
			dbImplementation.updateValues(getTableName(), Arrays.asList("mail"), Arrays.asList(newMail), "secure_id = "+secureID);
		} catch (SQLException e) {
			//TODO Handle exception correctly
			return false;
		}
		return true;
	}

	public String getMail(String secureID) {
		try {
			ResultSet res = dbImplementation.getValues(getTableName(), Arrays.asList("mail"), "secure_id = "+secureID);
			if(res.next()) return res.getString("mail");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public Ternary isMailAlreadyUsed(String mail) {
		try {
			return (dbImplementation.getValuesCount(getTableName(), mail, "mail = \'"+mail+"\'") != 0 ? Ternary.TRUE : Ternary.FALSE);
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return Ternary.UNDEFINED;
	}
	
	@Override
	public String getTableName() {
		return "User";
	}
}
