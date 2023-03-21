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
	
	/**
	 * Constructor for UserTable
	 * @param factory the DatabaseFactory used by the table
	 * @param credentials the credentials to the database
	 */
	public UserTable(DatabaseFactory factory, DatabaseCredentials credentials) {
		this.factory = factory;
		this.credentials = credentials;
	}
	
	/**
	 * Create a new User Account in the database
	 * @param name the name of the new user
	 * @param surname the surname of the new user
	 * @param password the password of the new user <strong>must be hashed</strong>
	 * @param address the address of the new user
	 * @param birth the birth date of the new user
	 * @param mail the mail of the new user <strong>must be unique</strong>
	 * @param phone the phone number of the new user <strong>must be unique</strong>
	 * @return the secure ID of the new user or null if cannot be created
	 * @throws DataCreationException
	 */
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
	
	/**
	 * Delete a user account from the database
	 * @param userSecureID the secure ID of the user
	 */
	public void deleteUserAccount(String userSecureID) {
		try {
			new DatabaseRequest(factory, credentials, true).deleteValues(getTableName(), "secure_id=?", Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR)));
		} catch (SQLException e) {
			//TODO Handle exception correctly
		}
	}
	
	/**
	 * Return if the user is locked by an administrator
	 * @param userSecureID the secure ID of the user
	 * @return a Ternary operator, if <strong>TRUE</strong> the account is locked else return <strong>FALSE</strong><br>
	 * If the Ternary returned <strong>UNDEFINED</strong> an error has been raised within the database
	 */
	public Ternary isUserLocked(String userSecureID) {
		try {
			final ResultSetWrappingSqlRowSet res = new DatabaseRequest(factory, credentials, true).getValues(getTableName(), Arrays.asList("secure_id","state"), "secure_id=?", Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR)));
			return res.next() ? (res.getInt("state") == 2 ? Ternary.TRUE : Ternary.FALSE) : Ternary.UNDEFINED; 
		} catch (SQLException e) {
			e.printStackTrace();
			return Ternary.UNDEFINED;
		}
	}
	
	/**
	 * Define the lock for the user
	 * @param userSecureID the secure ID of the user
	 * @param lockUser the new state of the column "lock"
	 */
	public void manageLockForUser(String userSecureID, boolean lockUser) {
		try {
			final int userState = lockUser ? 2 : 1; 
			
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("state"), Arrays.asList(userState), "secure_id=?", Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR)));
		} catch (SQLException e) {
			// TODO Handle error correctly
		}
	}
	
	/**
	 * Check the credentials when a user is trying to log in the website<br>
	 * Verify if the mail exist and if the passwords matches
	 * @param mail the mail provided by the user
	 * @param password the password provided by the user
	 * @return the secure ID of the user or null if the user cannot be authentified
	 */
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
	
	/**
	 * Get the entire list of user
	 * @return a list of user bean
	 */
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
	
	/**
	 * Get a specific user from the database
	 * @param userSecureID the secure ID of the user
	 * @return the user beans with the corresponding values or null
	 */
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
	
	/**
	 * Set a new profile picture of an user
	 * @param ftpConnection a representation of an sftp connection 
	 * @param userSecureID the secure ID of the user
	 * @param inputstream an InputStream representing the new profile picture
	 * @return if the profile picture has been correctly added to sftp
	 */
	public boolean setImage(SFTPConnection ftpConnection, String userSecureID, InputStream inputstream) {
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
			
			return true;
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
	
	/**
	 * Get the profile picture of an user
	 * @param secureID the secureID of the user
	 * @return the formatted URL to access to profile picture of the User
	 */
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
	
	/**
	 * Define a new name and new surname for an user
	 * @param secureID the secure ID of the user
	 * @param newName the new name of the user
	 * @param newSurname the new surname of the user
	 */
	public boolean setNameSurname(String secureID, String newName, String newSurname) {
		try {
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("name","surname"), Arrays.asList(newName, newSurname), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
			return true;
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}
	
	/**
	 * Get the name and surname of an user
	 * @param secureID the secureID of the user
	 * @return a tuple representing the pair of name and surname
	 */
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
	
	/**
	 * Define a new password for an user<br>
	 * <strong>The password must be already hashed</strong>
	 * @param secureID the secure ID of the user
	 * @param newPassword the new password of the user
	 * @return if the password has been correctly added
	 */
	public boolean setPassword(String secureID, String newPassword) {
		try {
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("password"), Arrays.asList(newPassword), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
			return true;
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}
	
	/**
	 * Get the password of an user
	 * @param secureID the secureID of the user
	 * @return a string representing the hashed password
	 */
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
	
	/**
	 * Define a new phone for the user<br>
	 * A verification is made to see if the phone isn't already used by someone else
	 * @param secureID the secureID of the user
	 * @param newNumber the new phone wanted by the user
	 * @return if the phone number has been correctly added
	 */
	public boolean setPhoneNumber(String secureID, String newNumber) {
		DatabaseRequest request = null; 
		newNumber = newNumber.replace(" ", "");
		try {
			request = new DatabaseRequest(factory, credentials);
			
			if(request.getValuesCount(getTableName(), Arrays.asList("phone"), "phone=?", Arrays.asList(new Tuple<>(newNumber, Types.VARCHAR))) == 0) {
				request.updateValues(getTableName(), Arrays.asList("phone"), Arrays.asList(newNumber), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
				return true;
			}else return false;
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
	
	/**
	 * Get the phone number of an user
	 * @param secureID the secureID of the user
	 * @return a string representing the phone number
	 */
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
	
	/**
	 * Define a new birth date for an user
	 * @param secureID the secure ID of the user
	 * @param newBirthDate the new birth date of the user
	 * @return if the birth date has been correctly added
	 */
	public boolean setBirthDate(String secureID, String newBirthDate) {
		try {
			 new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("birth"), Arrays.asList(newBirthDate), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
			 return true;
		} catch (SQLException e) {
			throw new DataCreationException(getClass(), CreationExceptionReason.FAILED_DB_CREATION);
		}
	}

	/**
	 * Get the birth date of an user
	 * @param secureID the secureID of the user
	 * @return a Date representing the birth date
	 * @see Date
	 */
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
	
	/**
	 * Define a new mail for the user<br>
	 * A verification is made to see if the mail isn't already used by someone else
	 * @param secureID the secureID of the user
	 * @param newMail the new mail wanted by the user
	 */
	public boolean setMail(String secureID, String newMail) {
		DatabaseRequest request = null; 
		try {
			request = new DatabaseRequest(factory, credentials);
			
			if(request.getValuesCount(getTableName(), Arrays.asList("mail"), "mail=?", Arrays.asList(new Tuple<>(newMail, Types.VARCHAR))) == 0) {
				request.updateValues(getTableName(), Arrays.asList("mail"), Arrays.asList(newMail), "secure_id = ?", Arrays.asList(new Tuple<>(secureID, Types.VARCHAR)));
				return true;
			}else return false;
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

	/**
	 * Get the mail of an user
	 * @param secureID the secureID of the user
	 * @return a string representing the mail
	 */
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
	
	/**
	 * Check if the mail or the phone are already used and thus deny the creation of the account
	 * @param mail the mail sent by the user
	 * @param phone the phone sent by the user
	 * @return a Ternary operator, if <strong>TRUE</strong> the account can be created else return <strong>FALSE</strong><br>
	 * If the Ternary returned <strong>UNDEFINED</strong> an error has been raised within the database
	 */
	public Ternary isAccoundCreationPossible(String mail, String phone) {
		try {
			phone = phone.replace(" ", "");
			return (new DatabaseRequest(factory, credentials, true).getValuesCount(getTableName(), Arrays.asList("mail","phone"), "(mail=? OR phone=?)", Arrays.asList(new Tuple<>(mail, Types.VARCHAR), new Tuple<>(phone, Types.VARCHAR))) == 0 ? Ternary.TRUE : Ternary.FALSE);
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
