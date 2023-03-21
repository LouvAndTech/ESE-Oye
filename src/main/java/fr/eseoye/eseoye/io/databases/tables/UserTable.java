package fr.eseoye.eseoye.io.databases.tables;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import com.hierynomus.sshj.userauth.keyprovider.bcrypt.BCrypt;

import fr.eseoye.eseoye.beans.SimplifiedEntity;
import fr.eseoye.eseoye.beans.User;
import fr.eseoye.eseoye.exceptions.DataCreationException;
import fr.eseoye.eseoye.exceptions.DataCreationException.CreationExceptionReason;
import fr.eseoye.eseoye.helpers.SFTPHelper;
import fr.eseoye.eseoye.helpers.SFTPHelper.ImageDirectory;
import fr.eseoye.eseoye.helpers.SecurityHelper;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.request.DatabaseRequest;
import fr.eseoye.eseoye.io.ftp.SFTPConnection;
import fr.eseoye.eseoye.utils.Ternary;
import fr.eseoye.eseoye.utils.Tuple;

public class UserTable implements ITable {

	private static final String USER_IMAGE_TABLE_NAME = "User_IMG";
	
	private DatabaseFactory factory;
	private DatabaseCredentials credentials;
	
	public UserTable(DatabaseFactory factory, DatabaseCredentials credentials) {
		this.factory = factory;
		this.credentials = credentials;
	}
	
	public String createUserAccount(String name, String surname, String password, String address, Date birth, String mail, String phone) throws DataCreationException {
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final ResultSetWrappingSqlRowSet requestLastId = request.getValues("SELECT id FROM "+getTableName()+" ORDER BY id DESC LIMIT 1;");
			int lastIdUser = 0;
			if(requestLastId.next()) lastIdUser = requestLastId.getInt("id");
			
			final String secureId = SecurityHelper.generateSecureID(System.currentTimeMillis(), lastIdUser, SecurityHelper.SECURE_ID_LENGTH);
			
			request.insertValues(getTableName(), 
					Arrays.asList("name","surname", "birth", "address", "phone", "mail", "password", "state", "secure_id"), 
					Arrays.asList(name, surname, birth, address, phone.replaceAll(" ", ""), mail, password, 1, secureId));
			
			return secureId;
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
	
	public void deleteUserAccount(String userSecureID) {
		try {
			new DatabaseRequest(factory, credentials, true).deleteValues(getTableName(), "secure_id=?", Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR)));
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
	}
	
	public Ternary isUserLocked(String userSecureID) {
		try {
			final ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("secure_id","state"), "secure_id=?", Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR)));
			return res.next() ? (res.getInt("state") == 2 ? Ternary.TRUE : Ternary.FALSE) : Ternary.UNDEFINED; 
		} catch (SQLException e) {
			e.printStackTrace();
			return Ternary.UNDEFINED;
		}
	}
	
	public void manageLockForUser(String userSecureID, boolean lockUser) {
		try {
			final int userState = lockUser ? 2 : 1; 
			
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("state"), Arrays.asList(userState), "secure_id=?", Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR)));
		} catch (SQLException e) {
			// TODO Handle error correctly
		}
	}
	
	public String checkUserConnection(String mail, String password) {
		try {
			ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("mail","password","secure_id"), "mail = ?", Arrays.asList(new Tuple<>(mail, Types.VARCHAR)));
			boolean isFound = false;
			while(!isFound && res.next()) 
				isFound = BCrypt.checkpw(password, res.getString("password"));
				
			return isFound ? res.getString("secure_id") : null;
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public List<SimplifiedEntity> getUserList() {
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
	
	public User getUser(String userSecureID) {
		
		try {			
			ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(
					getTableName(), 
					Arrays.asList("secure_id","name","surname","birth","address","phone","mail"), 
					"secure_id=?", 
					Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR)));
			
			if(!res.next()) throw new SQLException();
			
			return new User(res.getString("secure_id"), res.getString("name"), res.getString("surname"), null, res.getDate("birth"), res.getString("address"), res.getString("phone"), res.getString("mail"), -1);
		}catch(SQLException e) {
			return null;
		}
		
	}
	
	public void setImage(SFTPConnection ftpConnection, String userSecureID, InputStream inputstream) {
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			final ResultSetWrappingSqlRowSet requestUserDatabaseId = request.getValues(getTableName(), Arrays.asList("id"), "secure_id = ?", Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR))); //Get the id of the user and store it for the creation of the image
			if(!requestUserDatabaseId.next()) throw new SQLException();
			final int userDatabaseID = requestUserDatabaseId.getInt("id");
			
			String existingPicSecureID = null;
			ResultSetWrappingSqlRowSet requestExistingPic = request.getValues(USER_IMAGE_TABLE_NAME, Arrays.asList("id", "secure_id", "user"), "user=?", Arrays.asList(new Tuple<>(userDatabaseID, Types.INTEGER)));
			if(requestExistingPic.next()) {
				existingPicSecureID = requestExistingPic.getString("secure_id");	
				request.deleteValues(USER_IMAGE_TABLE_NAME, "secure_id=?", Arrays.asList(new Tuple<>(existingPicSecureID, Types.VARCHAR)));
			}
			
			final int lastId = request.getValues("SELECT id FROM "+USER_IMAGE_TABLE_NAME+" ORDER BY id DESC LIMIT 1;").getInt("id"); //Get the last id for user image in the table
			
			if(existingPicSecureID != null) ftpConnection.removeUserImage(userSecureID, existingPicSecureID);
			String imageID = ftpConnection.addNewUserImage(userSecureID, lastId, inputstream);
			request.insertValues("User_IMG",Arrays.asList("user, secure_id"), Arrays.asList(userDatabaseID, imageID));
			
		}catch(SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		} catch (IOException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_IMAGE_UPLOAD);
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
	
	public String getImage(String userSecureID) {
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final int userDatabaseId = request.getValues(getTableName(), Arrays.asList("id"), "secure_id = ", Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR))).getInt("id"); //Get the id of the user and store it to get the image

			final ResultSetWrappingSqlRowSet res = request.getValues(USER_IMAGE_TABLE_NAME, Arrays.asList("secure_id"), "user = ?", Arrays.asList(new Tuple<>(userDatabaseId, Types.INTEGER)));
			if(res.next())
				return SFTPHelper.getFormattedImageURL(ImageDirectory.USER, userSecureID, res.getString("secure_id"));
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}finally {
			if(request != null) {
				try {
					request.closeConnection();
				} catch (SQLException e) {
					System.err.println("Couldn't close the database correctly "+e.getMessage());
				}
			}
		}
		return null;
	}
	
	public void setNameSurname(String secureID, String newName, String newSurname) {
		try {
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("name","surname"), Arrays.asList(newName, newSurname), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}
	
	public Tuple<String, String> getNameSurname(String secureID) {
		try {
			ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("name","surname"), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
			if(res.next())
				return new Tuple<>(res.getString("name"), res.getString("surname"));
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return new Tuple<>();
	}
	
	public void setPassword(String secureID, String newPassword) {
		try {
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("password"), Arrays.asList(newPassword), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}
	
	public String getPassword(String secureID) {
		try {
			ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("password"), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
			if(res.next())
				return res.getString("password");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public void setPhoneNumber(String secureID, String newNumber) {
		try {
			 new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("phone"), Arrays.asList(newNumber), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}
	
	public String getPhoneNumber(String secureID) {
		try {
			ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("phone"), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
			if(res.next())
				return res.getString("phone");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public void setBirthDate(String secureID, String newBirthDate) {
		try {
			 new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("birth"), Arrays.asList(newBirthDate), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}

	public Date getBirthDate(String secureID) {
		try {
			ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("birth"), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
			if(res.next())
				return res.getDate("birth");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public void setMail(String secureID, String newMail) {
		try {
			 new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("mail"), Arrays.asList(newMail), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}

	public String getMail(String secureID) {
		try {
			ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("mail"), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
			if(res.next()) 
				return res.getString("mail");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public Ternary isAccoundCreationPossible(String mail, String phone) {
		try {
			phone = phone.replace(" ", "");
			return (new DatabaseRequest(factory, credentials, true).getValuesCount(getTableName(), Arrays.asList("mail","phone"), "(mail=? OR phone=?)", Arrays.asList(new Tuple<>(mail, Types.VARCHAR), new Tuple<>(phone, Types.VARCHAR))) != 0 ? Ternary.TRUE : Ternary.FALSE);
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
