package fr.eseoye.eseoye.io.databases.tables;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import com.hierynomus.sshj.userauth.keyprovider.bcrypt.BCrypt;

import fr.eseoye.eseoye.beans.SimplifiedEntity;
import fr.eseoye.eseoye.exceptions.DataCreationException;
import fr.eseoye.eseoye.exceptions.DataCreationException.CreationExceptionReason;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.request.DatabaseRequest;
import fr.eseoye.eseoye.utils.Ternary;
import fr.eseoye.eseoye.utils.Tuple;

public class AdminTable implements ITable {

	private DatabaseFactory factory;
	private DatabaseCredentials credentials;
	
	public AdminTable(DatabaseFactory factory, DatabaseCredentials credentials) {
		this.factory = factory;
		this.credentials = credentials;
	}
	
	public String createAdminAccount(String secureID, String name, String surname, String password) throws DataCreationException {
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
						
			request.insertValues(getTableName(), 
					Arrays.asList("name","surname", "rank", "password", "secure_id"), 
					Arrays.asList(name, surname, /*ADMIN SIMPLE*/0, password, secureID));
			
			return secureID;
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}finally {
			if(request != null) {
				try {
					request.closeConnection();
				} catch (SQLException e) {
					System.err.println("Couldn't close the database correctly "+e.getMessage());
				}
			}
		}
	}
	
	public boolean deleteAdminAccount(String adminSecureID) {
		try {
			new DatabaseRequest(factory, credentials, true).deleteValues(getTableName(), "secure_id=?", Arrays.asList(new Tuple<>(adminSecureID, Types.VARCHAR)));
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public String checkAdminConnection(String login, String password) {
		try {
			String[] loginParsed = login.split(".");
			if(loginParsed.length != 2) return null;
			
			ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("name","surname","password","secure_id"), "name=? AND surname=?", Arrays.asList(new Tuple<>(loginParsed[0], Types.VARCHAR), new Tuple<>(loginParsed[1], Types.VARCHAR)));
			boolean isFound = false;
			while(!isFound && res.next()) 
				isFound = BCrypt.checkpw(password, res.getString("password"));
				
			return isFound ? res.getString("secure_id") : null;
		} catch (SQLException e) {
			e.printStackTrace();
			//TODO Handle exception correctly
		}
		return null;
	}

	public List<SimplifiedEntity> getAdminList() {
		final List<SimplifiedEntity> list = new ArrayList<>();
		
		try {
			final ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("secure_id","name","surname"));
			while(res.next())
				list.add(new SimplifiedEntity(res.getString("secure_id"), res.getString("name"), res.getString("surname")));
		}catch(SQLException e) {
			return null;
		}
		return list;
	}
	
	public Tuple<String, String> getNameSurname(String adminSecureID) {
		try {
			ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("name","surname"), "secure_id = ?", Arrays.asList(new Tuple<>(adminSecureID, Types.VARCHAR)));
			if(res.next())
				return new Tuple<>(res.getString("name"), res.getString("surname"));
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return new Tuple<>();
	}
	
	public Ternary isAnAdminSecureID(String secureID) {
		try {
			return new DatabaseRequest(factory, credentials, true).getValuesCount(getTableName(), Arrays.asList("secure_id"), "secure_id=?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR))) != 0 ? Ternary.TRUE : Ternary.FALSE;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Ternary.UNDEFINED;
	}

	@Override
	public String getTableName() {
		return "Admin";
	}
	
}
