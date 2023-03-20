package fr.eseoye.eseoye.io.databases.tables;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.rowset.CachedRowSet;

import com.hierynomus.sshj.userauth.keyprovider.bcrypt.BCrypt;

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

	private static final String userImageTableName = "User_IMG";
	
	private DatabaseFactory factory;
	private DatabaseCredentials credentials;
	
	public UserTable(DatabaseFactory factory, DatabaseCredentials credentials) {
		this.factory = factory;
		this.credentials = credentials;
	}
	
	public String createUserAccount(String name, String surname, String password, Date birth, String mail, String phone) throws DataCreationException {
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final int lastId = request.getValues("SELECT id FROM "+getTableName()+" ORDER BY id DESC LIMIT 1;").getInt("id");
			final String secureId = SecurityHelper.generateSecureID(System.currentTimeMillis(), lastId, SecurityHelper.SECURE_ID_LENGTH);
			
			request.insertValues(getTableName(), 
					Arrays.asList("name","surname", "birth", "phone", "mail", "password", "state", "secure_id"), 
					Arrays.asList(name, surname, birth, phone, mail, password, 1, secureId));
			
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
	
	public String checkUserConnection(String name, String password) {
		try {
			CachedRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("password","secure_id"), "password = ?", Arrays.asList(password));
			boolean isFound = false;
			while(!isFound && res.next()) 
				isFound = BCrypt.checkpw(password, res.getString("password"));
				
			return isFound ? res.getString("secure_id") : null;
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public void setImage(SFTPConnection ftpConnection, String userSecureID, InputStream inputstream) {
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final int userDatabaseId = request.getValues(getTableName(), Arrays.asList("id"), "secure_id = ?", Arrays.asList(userSecureID)).getInt("id"); //Get the id of the user and store it for the creation of the image
			
			final int lastId = request.getValues("SELECT id FROM "+userImageTableName+" ORDER BY id DESC LIMIT 1;").getInt("id"); //Get the last id for user image in the table
						
			String imageID = ftpConnection.addNewUserImage(userSecureID, lastId, inputstream);
			request.insertValues("User_IMG",Arrays.asList("user, secure_id"), Arrays.asList(userDatabaseId, imageID));
			
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
			
			final int userDatabaseId = request.getValues(getTableName(), Arrays.asList("id"), "secure_id = ", Arrays.asList(userSecureID)).getInt("id"); //Get the id of the user and store it to get the image

			final CachedRowSet res = request.getValues(userImageTableName, Arrays.asList("secure_id"), "user = ?", Arrays.asList(userDatabaseId));
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
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("name","surname"), Arrays.asList(newName, newSurname), "secure_id = ?", Arrays.asList(secureID));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}
	
	public Tuple<String, String> getNameSurname(String secureID) {
		try {
			CachedRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("name","surname"), "secure_id = ?", Arrays.asList(secureID));
			if(res.next())
				return new Tuple<>(res.getString("name"), res.getString("surname"));
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return new Tuple<>();
	}
	
	public void setPassword(String secureID, String newPassword) {
		try {
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("password"), Arrays.asList(newPassword), "secure_id = ?", Arrays.asList(secureID));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}
	
	public String getPassword(String secureID) {
		try {
			CachedRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("password"), "secure_id = ?", Arrays.asList(secureID));
			if(res.next())
				return res.getString("password");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public void setPhoneNumber(String secureID, String newNumber) {
		try {
			 new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("phone"), Arrays.asList(newNumber), "secure_id = ?", Arrays.asList(secureID));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}
	
	public String getPhoneNumber(String secureID) {
		try {
			CachedRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("phone"), "secure_id = ?", Arrays.asList(secureID));
			if(res.next())
				return res.getString("phone");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public void setBirthDate(String secureID, String newBirthDate) {
		try {
			 new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("birth"), Arrays.asList(newBirthDate), "secure_id = ?", Arrays.asList(secureID));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}

	public Date getBirthDate(String secureID) {
		try {
			CachedRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("birth"), "secure_id = ?", Arrays.asList(secureID));
			if(res.next())
				return res.getDate("birth");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public void setMail(String secureID, String newMail) {
		try {
			 new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("mail"), Arrays.asList(newMail), "secure_id = ?", Arrays.asList(secureID));
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}

	public String getMail(String secureID) {
		try {
			CachedRowSet res =  new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("mail"), "secure_id = ?", Arrays.asList(secureID));
			if(res.next()) 
				return res.getString("mail");
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
		return null;
	}
	
	public Ternary isAccoundCreationPossible(String mail, String phone) {
		try {
			return (new DatabaseRequest(factory, credentials, true).getValuesCount(getTableName(), Arrays.asList("mail","phone"), "mail=? AND phone=?", Arrays.asList(mail, phone)) != 0 ? Ternary.TRUE : Ternary.FALSE);
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
