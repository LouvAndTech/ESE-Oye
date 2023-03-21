package fr.eseoye.eseoye.io.databases.tables;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.lang.Nullable;

import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.beans.Post;
import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.beans.PostState;
import fr.eseoye.eseoye.beans.SimplifiedEntity;
import fr.eseoye.eseoye.exceptions.DataCreationException;
import fr.eseoye.eseoye.exceptions.DataCreationException.CreationExceptionReason;
import fr.eseoye.eseoye.helpers.SFTPHelper;
import fr.eseoye.eseoye.helpers.SFTPHelper.ImageDirectory;
import fr.eseoye.eseoye.helpers.SecurityHelper;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.request.DatabaseRequest;
import fr.eseoye.eseoye.io.ftp.SFTPConnection;
import fr.eseoye.eseoye.io.objects.FetchPostFilter;
import fr.eseoye.eseoye.io.objects.FetchPostFilter.FetchOrderEnum;
import fr.eseoye.eseoye.utils.Tuple;

/**
 * Class handling all functions needed to interact with the Table <strong>Post</strong>
 * @author 278deco
 * @see fr.eseoye.eseoye.io.databases.tables.ITable
 */
public class PostTable implements ITable {
	
	//TODO Make the table name not hardcoded
	private static final String USER_TABLE_NAME = "User";
	private static final String CATEGORY_TABLE_NAME = "Post_Category";
	private static final String POST_STATE_TABLE_NAME = "Post_State";
	private static final String POST_IMG_TABLE_NAME = "Post_IMG";
	
	private DatabaseFactory factory;
	private DatabaseCredentials credentials;
	
	/**
	 * Constructor for PostTable
	 * @param factory the DatabaseFactory used by the table
	 * @param credentials the credentials to the database
	 */
	public PostTable(DatabaseFactory factory, DatabaseCredentials credentials) {
		this.factory = factory;
		this.credentials = credentials;
	}
	
	/**
	 * Create a new post in the database and put the images in the SFTP server
	 * @param sftpConnection a representation of an sftp connection 
	 * @param userSecureID the secure ID of the user wanting to creation the post
	 * @param title the title of the new post
	 * @param content the content of the new post
	 * @param price the price of the new post
	 * @param categoryID the category of the new post
	 * @param stateID the state of the new post
	 * @param images a list of image for the post
	 * @return the secure ID of the new post or null if it cannot be created
	 * @throws DataCreationException
	 * @see Category
	 * @see PostState
	 */
	public String createNewPost(SFTPConnection sftpConnection, String userSecureID, String title, String content, float price, int categoryID, int stateID, List<InputStream> images) throws DataCreationException {
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final ResultSetWrappingSqlRowSet requestUserDatabaseId = request.getValues(USER_TABLE_NAME, Arrays.asList("id"), "secure_id=?", Arrays.asList(new Tuple<>(userSecureID, Types.VARCHAR))); //Get the id of the user and store it for the creation of the post
			if(!requestUserDatabaseId.next()) throw new SQLException();
			final int userDatabaseID = requestUserDatabaseId.getInt("id");
			
			final ResultSetWrappingSqlRowSet requestLastPostId = request.getValues("SELECT id FROM "+getTableName()+" ORDER BY id DESC LIMIT 1;"); //Get the last id for post in the table
			int lastPostID = 0;
			if(requestLastPostId.next()) lastPostID = requestLastPostId.getInt("id");
			
			final ResultSetWrappingSqlRowSet requestLastPostImgId = request.getValues("SELECT id FROM "+POST_IMG_TABLE_NAME+" ORDER BY id DESC LIMIT 1;"); //Get the last id for a post img in the table
			int lastPostImgID = 0;
			if(requestLastPostImgId.next()) lastPostImgID = requestLastPostImgId.getInt("id");
			
			final String postSecureId = SecurityHelper.generateSecureID(System.currentTimeMillis(), lastPostID, SecurityHelper.SECURE_ID_LENGTH); //Generate the new secure id for the post
			
			request.insertValues(getTableName(), Arrays.asList("title", "content", "price", "category", "user", "state", "lock", "date", "secure_id"), Arrays.asList(title, content, price, categoryID, userDatabaseID, stateID, true, new Date(System.currentTimeMillis()), postSecureId));
			
			final ResultSetWrappingSqlRowSet requestPostDatabaseID = request.getValues("SELECT LAST_INSERT_ID() AS lid;"); //Get the id for the fresh created post
			if(!requestPostDatabaseID.next()) throw new SQLException();
			final int postDatabaseID = requestPostDatabaseID.getInt("lid");
			
			List<String> imagesId = sftpConnection.addNewPostImage(postSecureId, lastPostImgID, images);
			for(String imgId : imagesId) {
				request.insertValues(POST_IMG_TABLE_NAME,Arrays.asList("post","secure_id"), Arrays.asList(postDatabaseID, imgId));
			}
			
			return null;
		} catch (SQLException e) {
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
	 * Delete a new post from the database
	 * @param sftp sftpConnection a representation of an sftp connection 
	 * @param postSecureID the secure ID of the post 
	 * @return if the post has been correctly deleted
	 */
	public boolean deletePost(SFTPConnection sftp, String postSecureID) {
		try {
			sftp.removePostImageFolder(postSecureID);
			
			new DatabaseRequest(factory, credentials, true).deleteValues(getTableName(), "secure_id=?", Arrays.asList(new Tuple<>(postSecureID, Types.VARCHAR)));
			return true;
		} catch (SQLException | IOException e) {
			return false;
		}
	}
	
	/**
	 * Validate a post (remove the lock)
	 * @param postSecureID the secure ID of the post
	 * @return if the post has been correctly validated
	 * @see PostTable#setValidationStatus(String, boolean)
	 */
	public boolean validatePost(String postSecureID) {
		return this.setValidationStatus(postSecureID, false);
	}
	
	/**
	 * Set the validation status of a post (if the post locked or not)
	 * @param postSecureID the secure ID of the post
	 * @param newStatus the new status for the lock column of the post
	 * @return if the post has correctly changed status
	 */
	public boolean setValidationStatus(String postSecureID, boolean newStatus) {
		try {
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), Arrays.asList("lock"), Arrays.asList(newStatus), "secure_id=?", Arrays.asList(new Tuple<>(postSecureID, Types.VARCHAR)));
			return true;
		}catch(SQLException e) {
			return false;
		}
	}
	
	/**
	 * Modify the title and the content of the post<br>
	 * If one of the parameter if null the modification will not be applied to the corresponding field
	 * @param postSecureID the secure ID of the post
	 * @param newTitle the new title of the post to be applied
	 * @param newContent the new content of the post to be applied
	 * @return if the modification has been correctly made
	 */
	public boolean modifyPost(String postSecureID, @Nullable String newTitle, @Nullable String newContent) {
		try {
			List<String> fields = new ArrayList<>(); List<Object> values = new ArrayList<>();
			if(newTitle != null) { fields.add("title"); values.add(newTitle); }
			if(newContent != null) { fields.add("content"); values.add(newContent); }
			
			new DatabaseRequest(factory, credentials, true).updateValues(getTableName(), fields, values, "secure_id=?", Arrays.asList(new Tuple<>(postSecureID, Types.VARCHAR)));
		}catch(SQLException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Fetch the post to the database and show them in a list<br>
	 * Create an automatic paging
	 * @param postNumber the number of post to be shown
	 * @param pageNumber the index of the page viewed by the user
	 * @param parameters the parameters used by the user
	 * @see FetchPostFilter
	 * @return a tuple contains the list of the post and the number of all post respecting the conditions
	 */
	public Tuple<List<Post>, Integer> fetchShortPost(int postNumber, int pageNumber, FetchPostFilter parameters) {
		final List<Post> post = new ArrayList<>();
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final Tuple<String, List<Tuple<Object, Integer>>> whereClause = generateWhereClausePost(parameters);
						
			final String sqlRequestBody = "INNER JOIN "+USER_TABLE_NAME+" ON "+getTableName()+".user = "+USER_TABLE_NAME+".id "+
					"INNER JOIN "+CATEGORY_TABLE_NAME+" ON "+getTableName()+".category = "+CATEGORY_TABLE_NAME+".id "+
					"INNER JOIN "+POST_STATE_TABLE_NAME+" ON "+getTableName()+".state = "+POST_STATE_TABLE_NAME+".id "+
					(whereClause.getValueB().isEmpty() ? "" : "WHERE "+whereClause.getValueA()+" ");
					
			final ResultSetWrappingSqlRowSet res = request.getValuesWithCondition("SELECT "+getTableName()+".id AS post_id, "+getTableName()+".secure_id AS post_sid, "+getTableName()+".title AS post_title, "+getTableName()+".lock AS post_lock, "+USER_TABLE_NAME+".secure_id AS userpost_sid, "+USER_TABLE_NAME+".name AS userpost_name, "+USER_TABLE_NAME+".surname AS userpost_surname, "+getTableName()+".price AS post_price, "+CATEGORY_TABLE_NAME+".name AS category_name, "+POST_STATE_TABLE_NAME+".name AS poststate_name, "+getTableName()+".date AS post_date FROM "+getTableName()+" "+
							sqlRequestBody+
							generateOrderClausePost(parameters.getOrder())+" "+
							"LIMIT "+postNumber+" OFFSET "+(pageNumber*postNumber)+";", whereClause.getValueB());
			
			final ResultSetWrappingSqlRowSet requestTotalPostNumber = request.getValuesWithCondition("SELECT COUNT(Post.id) AS count FROM Post "
							+sqlRequestBody, whereClause.getValueB());
			if(!requestTotalPostNumber.next()) throw new SQLException();
			final int totalPostNumber = requestTotalPostNumber.getInt("count");
			
			while(res.next()) {				
				final SimplifiedEntity u = new SimplifiedEntity(res.getString("userpost_name"), res.getString("userpost_surname"));
				final Category c = new Category(res.getString("category_name"));
				final PostState ps = new PostState(res.getString("poststate_name"));
				
				final List<String> postImages = generatePostImagesURL(res.getString("post_sid"), fetchPostImages(request, res.getInt("post_id"), res.getString("post_sid"), 1));
				if(postImages.isEmpty()) postImages.add(SFTPHelper.getFormattedImageURL(ImageDirectory.ROOT, "", "404"));
				
				post.add(new Post(res.getString("post_sid"), res.getString("post_title"), u, res.getInt("post_price"), res.getDate("post_date"), c, ps, postImages.get(0)));
			}
			
			return new Tuple<>(post, (int)Math.ceil((double)totalPostNumber/postNumber));
		} catch (SQLException e) {
			e.printStackTrace();
			//TODO Handle exception
			return null;
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
	 * Generate the clause order used by the SQL request
	 * @param order the order wanted by the user
	 * @return the formatted string
	 * @see FetchOrderEnum
	 */
	private String generateOrderClausePost(FetchOrderEnum order) {
		switch (order) {
			case DATE_ASCENDING: 
				return "ORDER BY "+getTableName()+".id ASC";
			case PRICE_ASCENDING: 
				return "ORDER BY "+getTableName()+".price ASC";
			case PRICE_DESCENDING: 
				return "ORDER BY "+getTableName()+".price DESC";
			default:
				return "ORDER BY "+getTableName()+".id DESC";
		}
	}

	/**
	 * Generate the "where clause" used by the sql request
	 * @param parameters the parameters used by the user
	 * @return a tuple containing the formatted sql string and list of parameter for the PreparedStatement
	 */
	private Tuple<String, List<Tuple<Object, Integer>>> generateWhereClausePost(FetchPostFilter parameters) {
		final StringBuilder sb = new StringBuilder("");
		final List<Tuple<Object, Integer>> whereObj = new ArrayList<>();
		
		if(parameters.isUserIDPresent()) { sb.append(USER_TABLE_NAME+".secure_id=? AND "); whereObj.add(new Tuple<>(parameters.getUserSecureID(), Types.VARCHAR)); }
		if(parameters.isCategoryPresent()) { sb.append(CATEGORY_TABLE_NAME+".id=? AND "); whereObj.add(new Tuple<>(parameters.getCategoryID(), Types.INTEGER)); }
		if(parameters.isStatePresent()) { sb.append(POST_STATE_TABLE_NAME+".id=? AND "); whereObj.add(new Tuple<>(parameters.getStateID(), Types.INTEGER)); }
		if(parameters.isMaxPricePresent()) { sb.append(getTableName()+".price<=? AND "); whereObj.add(new Tuple<>(parameters.getMaxPrice(), Types.DECIMAL)); }
		if(parameters.mustBeValidated()) { sb.append(getTableName()+".lock=? AND "); whereObj.add(new Tuple<>(false, Types.BOOLEAN)); }
		if(parameters.anyKeyWordsPresent()) { 
			final Tuple<String, List<Tuple<Object, Integer>>> res = generateSearchEngine(parameters.getKeyWords());
			sb.append(res.getValueA());
			whereObj.addAll(res.getValueB());
		}
		sb.setLength(sb.length()-5);
		
		return new Tuple<>(sb.toString(), whereObj);
	}

	/**
	 * Generate the "search clause" (LIKE argument) used by the sql request
	 * @param keyWords the list of key words provided by the user
	 * @return a tuple containing the formatted sql string and list of parameter for the PreparedStatement
	 */
	private Tuple<String, List<Tuple<Object, Integer>>> generateSearchEngine(Set<String> keyWords) {
		final StringBuilder sb = new StringBuilder("");
		final List<Tuple<Object, Integer>> searchObj = new ArrayList<>();
		
		for(String word : keyWords) {
			sb.append(getTableName()+".title LIKE ? AND ");
			searchObj.add(new Tuple<>("%"+word+"%", Types.VARCHAR));
		}
		
		return new Tuple<>(sb.toString(), searchObj);
	}
	
	/**
	 * Get the last locked post ID for an administrator
	 * @return the secure ID of the post<br>
	 * if no post was found return an empty string
	 */
	public String fetchLastLockedPostID() {
		String id = null;
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final ResultSetWrappingSqlRowSet res = request.getValuesWithCondition("SELECT secure_id AS post_sid, `lock` FROM "+getTableName()+" "+
					"WHERE "+getTableName()+".lock=? ORDER BY "+getTableName()+".id DESC", Arrays.asList(new Tuple<>(true, Types.BOOLEAN)));
		
			if(res.next()) id = res.getString("post_sid");
			else id = "";
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			if(request != null) {
				try {
					request.closeConnection();
				} catch (SQLException e) {
					System.err.println("Couldn't close the database correctly "+e.getMessage());
				}
			}
		}
		
		return id;
	}
	
	/**
	 * Fetch an entire post and show all its values
	 * @param postID the secureID of the post
	 * @return the PostComplete beans representing the post
	 */
	public PostComplete fetchEntirePost(String postID) {
		PostComplete pc = null;
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final ResultSetWrappingSqlRowSet res = request.getValuesWithCondition("SELECT "+getTableName()+".id AS post_id, "+getTableName()+".secure_id AS post_sid, "+getTableName()+".title AS post_title, "+getTableName()+".content AS post_content, "+USER_TABLE_NAME+".secure_id AS userpost_sid, "+USER_TABLE_NAME+".name AS userpost_name, "+USER_TABLE_NAME+".surname AS userpost_surname, "+getTableName()+".price AS post_price, "+CATEGORY_TABLE_NAME+".name AS category_name, "+POST_STATE_TABLE_NAME+".name AS poststate_name, "+getTableName()+".date AS post_date FROM "+getTableName()+" "+
					"INNER JOIN "+USER_TABLE_NAME+" ON "+getTableName()+".user = "+USER_TABLE_NAME+".id "+
					"INNER JOIN "+CATEGORY_TABLE_NAME+" ON "+getTableName()+".category = "+CATEGORY_TABLE_NAME+".id "+
					"INNER JOIN "+POST_STATE_TABLE_NAME+" ON "+getTableName()+".state = "+POST_STATE_TABLE_NAME+".id "+
					"WHERE "+getTableName()+".secure_id=?", Arrays.asList(new Tuple<>(postID, Types.VARCHAR)));
			
			if(res.next()) {
				final SimplifiedEntity u = new SimplifiedEntity(res.getString("userpost_sid"), res.getString("userpost_name"), res.getString("userpost_surname"));
				final Category c = new Category(res.getString("category_name"));
				final PostState ps = new PostState(res.getString("poststate_name"));
				
				final List<String> postImages = generatePostImagesURL(res.getString("post_sid"), fetchPostImages(request, res.getInt("post_id"), res.getString("post_sid"), 4));
				if(postImages.isEmpty()) postImages.add(SFTPHelper.getFormattedImageURL(ImageDirectory.ROOT, "", "404"));
				
				pc = new PostComplete(res.getString("post_sid"), res.getString("post_title"), u, res.getFloat("post_price"), res.getDate("post_date"), res.getString("post_content"), c, ps, postImages.get(0), postImages);
			}
		} catch (SQLException e) {
			//TODO Handle exception
			return null;
		}finally {
			if(request != null) {
				try {
					request.closeConnection();
				} catch (SQLException e) {
					System.err.println("Couldn't close the database correctly "+e.getMessage());
				}
			}
		}
		
		return pc;
	}
	
	/**
	 * Fetch all images for a post
	 * @param req the request to the database
	 * @param postDatabaseID the id of the post stored in the database
	 * @param postSecureID the secure ID of the post
	 * @param limit the limit of image to be retrieved
	 * @return a list of secure ID representing the images
	 */
	private List<String> fetchPostImages(DatabaseRequest req, int postDatabaseID, String postSecureID, int limit) {
		final List<String> postImages = new ArrayList<>();
		try {
			final ResultSetWrappingSqlRowSet res = req.getValues(POST_IMG_TABLE_NAME, Arrays.asList("secure_id"), "post=?", Arrays.asList(new Tuple<>(postDatabaseID, Types.INTEGER)));
			
			int index = 0;
			while(res.next() && index < limit) {
				postImages.add(res.getString("secure_id"));
				index+=1;
			}
			
		} catch (SQLException e) {
			//TODO Handle exception
			return new ArrayList<>();
		}
		return postImages;
	}
	
	/**
	 * Generate the URL for the post images
	 * @param postSecureID the post secure ID
	 * @param imageSecureID the image secure ID
	 * @return the list of images URL
	 */
	private List<String> generatePostImagesURL(String postSecureID, List<String> imageSecureID){
		final List<String> res = new ArrayList<>();
		
		for(String id : imageSecureID) res.add(SFTPHelper.getFormattedImageURL(ImageDirectory.POST, postSecureID, id));
		
		return res;
	}
	
	@Override
	public String getTableName() {
		return "Post";
	}

}
