package fr.eseoye.eseoye.io.databases.tables;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.beans.Post;
import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.beans.PostState;
import fr.eseoye.eseoye.beans.SimplifiedUser;
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

public class PostTable implements ITable {
	
	
	//TODO Make the table name not hardcoded
	private static final String USER_TABLE_NAME = "User";
	private static final String CATEGORY_TABLE_NAME = "Post_Category";
	private static final String POST_STATE_TABLE_NAME = "Post_State";
	private static final String POST_IMG_TABLE_NAME = "Post_IMG";
	
	private DatabaseFactory factory;
	private DatabaseCredentials credentials;
	
	public PostTable(DatabaseFactory factory, DatabaseCredentials credentials) {
		this.factory = factory;
		this.credentials = credentials;
	}
	
	public String createNewPost(SFTPConnection sftpConnection, String userSecureID, String title, String content, float price, int categoryID, int stateID, List<InputStream> images) throws DataCreationException {
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final int userDatabaseId = request.getValues(USER_TABLE_NAME, Arrays.asList("id"), "secure_id = ?", Arrays.asList(userSecureID)).getInt("id"); //Get the id of the user and store it for the creation of the post

			final int lastPostId = request.getValues("SELECT id FROM "+getTableName()+" ORDER BY id DESC LIMIT 1;").getInt("id"); //Get the last id for post in the table
			final int lastPostImgId = request.getValues("SELECT id FROM "+getTableName()+" ORDER BY id DESC LIMIT 1;").getInt("id"); //Get the last id for a post img in the table
			
			final String postSecureId = SecurityHelper.generateSecureID(System.currentTimeMillis(), lastPostId, SecurityHelper.SECURE_ID_LENGTH); //Generate the new secure id for the post
			
			request.insertValues(postSecureId, Arrays.asList("title","content","price","category","user","state","date", "secure_id"), Arrays.asList(title, content, price, categoryID, userDatabaseId, stateID, new Date(System.currentTimeMillis()), postSecureId));
			
			final int postDatabaseID = request.getValues("SELECT SCOPE_IDENTITY();").getInt(0); //Get the id for the fresh created post
						
			List<String> imagesId = sftpConnection.addNewPostImage(postSecureId, lastPostImgId, images);
			for(String imgId : imagesId) {
				request.insertValues("Post_IMG",Arrays.asList("post, secure_id"), Arrays.asList(postDatabaseID, imgId));
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
	
//	public String modifyPost(String )

	public Tuple<List<Post>, Integer> fetchShortPost(int postNumber, int pageNumber, FetchPostFilter parameters) {
		return fetchShortPost(postNumber, pageNumber, null, parameters);
	}
	
	public Tuple<List<Post>, Integer> fetchShortPost(int postNumber, int pageNumber, String userSecureID, FetchPostFilter parameters) {
		final List<Post> post = new ArrayList<>();
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final Tuple<String, List<Object>> whereClause = generateWhereClausePost(userSecureID, parameters);
			final String orderClause = generateOrderClausePost(parameters.getOrder());
			
			final CachedRowSet res = request.getValuesWithCondition("SELECT "+getTableName()+".id, "+getTableName()+".secure_id, "+getTableName()+".title, "+USER_TABLE_NAME+".name, "+USER_TABLE_NAME+".surname "+getTableName()+".price, "+CATEGORY_TABLE_NAME+".name, "+POST_STATE_TABLE_NAME+".name, "+getTableName()+".date FROM "+getTableName()+" "+
							"INNER JOIN "+USER_TABLE_NAME+" ON "+getTableName()+".user = "+USER_TABLE_NAME+".id "+
							"INNER JOIN "+CATEGORY_TABLE_NAME+" ON "+getTableName()+".category = "+CATEGORY_TABLE_NAME+".id "+
							"INNER JOIN "+POST_STATE_TABLE_NAME+" ON "+getTableName()+".state = "+POST_STATE_TABLE_NAME+".id" +
							" "+whereClause.getValueA()+" "+
							"LIMIT "+postNumber+" OFFSET "+(pageNumber*postNumber)+
							" "+orderClause+";", whereClause.getValueB());
			
			while(res.next()) {
				final SimplifiedUser u = new SimplifiedUser(res.getString(USER_TABLE_NAME+".name"), res.getString(USER_TABLE_NAME+".surname"));
				final Category c = new Category(CATEGORY_TABLE_NAME+".name");
				final PostState ps = new PostState(POST_STATE_TABLE_NAME+".name");
				
				final List<String> postImages = fetchPostImages(request, res.getString(getTableName()+".id"), res.getString(getTableName()+".secure_id"), 1);
				if(postImages.isEmpty()) postImages.add(SFTPHelper.getFormattedImageURL(ImageDirectory.ROOT, "", "1.jpg"));
				
				post.add(new Post(res.getString("secure_id"), res.getString("title"), u, res.getInt("price"), res.getDate("date"), c, ps, postImages.get(0)));
			}
			
			int totalPostNumber = request.getValuesCount(getTableName(), Arrays.asList("id"));
			
			return new Tuple<>(post, (int)Math.floor(totalPostNumber/postNumber));
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
	}
	
	private String generateOrderClausePost(FetchOrderEnum order) {
		switch (order) {
			case DATE_ASCENDING: 
				return "ORDER BY "+getTableName()+".date ASC";
			case PRICE_ASCENDING: 
				return "ORDER BY "+getTableName()+".price ASC";
			case PRICE_DESCENDING: 
				return "ORDER BY "+getTableName()+".price DESC";
			default:
				return "ORDER BY "+getTableName()+".date DESC";
		}
	}

	private Tuple<String, List<Object>> generateWhereClausePost(String userSecureID, FetchPostFilter parameters) {
		final StringBuilder sb = new StringBuilder("WHERE ");
		final List<Object> whereObj = new ArrayList<>();
		
		if(userSecureID != null) { sb.append(USER_TABLE_NAME+".secure_id = ?, "); whereObj.add(userSecureID); }
		if(parameters.isCategoryPresent()) { sb.append(CATEGORY_TABLE_NAME+".id = ?, "); whereObj.add(parameters.getCategoryID()); }
		if(parameters.isStatePresent()) { sb.append(POST_STATE_TABLE_NAME+".id = ?, "); whereObj.add(parameters.getStateID()); }
		if(parameters.isMaxPricePresent()) { sb.append(getTableName()+".price <= ?, "); whereObj.add(parameters.getMaxPrice()); }
		if(parameters.mustBeValidated()) { sb.append(getTableName()+".lock = ?, "); whereObj.add(0); }
		sb.setLength(sb.length()-2);
		
		return whereObj.size() != 0 ? new Tuple<>(sb.toString(), whereObj) : new Tuple<>("", whereObj);
	}

	public PostComplete fetchEntirePost(String postID) {
		PostComplete pc = null;
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final CachedRowSet res = request.getValuesWithCondition("SELECT "+getTableName()+".id, "+getTableName()+".secure_id, "+getTableName()+".title, "+USER_TABLE_NAME+".name, "+USER_TABLE_NAME+".surname "+getTableName()+".price, "+CATEGORY_TABLE_NAME+".name, "+POST_STATE_TABLE_NAME+".name, "+getTableName()+".date FROM "+getTableName()+" "+
					"INNER JOIN "+USER_TABLE_NAME+" ON "+getTableName()+".user = "+USER_TABLE_NAME+".id "+
					"INNER JOIN "+CATEGORY_TABLE_NAME+" ON "+getTableName()+".category = "+CATEGORY_TABLE_NAME+".id "+
					"INNER JOIN "+POST_STATE_TABLE_NAME+" ON "+getTableName()+".state = "+POST_STATE_TABLE_NAME+".id"+
					"WHERE "+getTableName()+"=?", Arrays.asList(postID));
			
			if(res.next()) {
				final SimplifiedUser u = new SimplifiedUser(res.getString(USER_TABLE_NAME+".name"), res.getString(USER_TABLE_NAME+".surname"));
				final Category c = new Category(CATEGORY_TABLE_NAME+".name");
				final PostState ps = new PostState(POST_STATE_TABLE_NAME+".name");
				
				final List<String> postImages = fetchPostImages(request, res.getString(getTableName()+".id"), res.getString(getTableName()+".secure_id"), 4);
				if(postImages.isEmpty()) postImages.add(SFTPHelper.getFormattedImageURL(ImageDirectory.ROOT, "", "1.jpg"));
				
				pc = new PostComplete(res.getString("secure_id"), res.getString("title"), u, res.getFloat("price"), res.getDate("date"), res.getString("content"), c, ps, postImages.get(0), postImages.subList(1, postImages.size()));
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
	
	private List<String> fetchPostImages(DatabaseRequest req, String postDatabaseID, String postSecureID, int limit) {
		final List<String> postImages = new ArrayList<>();
		try {
			final CachedRowSet res = req.getValues(POST_IMG_TABLE_NAME, Arrays.asList("secure_id"), "post=?", Arrays.asList(postDatabaseID));
			
			int index = 0;
			while(res.next() && index < limit) {
				postImages.add(SFTPHelper.getFormattedImageURL(ImageDirectory.POST, postSecureID, res.getString("secure_id")));
				index+=1;
			}
			
		} catch (SQLException e) {
			//TODO Handle exception
			return new ArrayList<>();
		}
		return postImages;
	}
	
	@Override
	public String getTableName() {
		return "Post";
	}

}
