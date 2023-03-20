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
			
			final CachedRowSet requestUserDatabaseId = request.getValues(USER_TABLE_NAME, Arrays.asList("id"), "secure_id=?", Arrays.asList(userSecureID)); //Get the id of the user and store it for the creation of the post
			if(!requestUserDatabaseId.next()) throw new SQLException();
			final int userDatabaseID = requestUserDatabaseId.getInt("id");
			
			final CachedRowSet requestLastPostId = request.getValues("SELECT id FROM "+getTableName()+" ORDER BY id DESC LIMIT 1;"); //Get the last id for post in the table
			int lastPostID = 0;
			if(requestLastPostId.next()) lastPostID = requestLastPostId.getInt("id");
			
			final CachedRowSet requestLastPostImgId = request.getValues("SELECT id FROM "+POST_IMG_TABLE_NAME+" ORDER BY id DESC LIMIT 1;"); //Get the last id for a post img in the table
			int lastPostImgID = 0;
			if(requestLastPostImgId.next()) lastPostImgID = requestLastPostImgId.getInt("id");
			
			final String postSecureId = SecurityHelper.generateSecureID(System.currentTimeMillis(), lastPostID, SecurityHelper.SECURE_ID_LENGTH); //Generate the new secure id for the post
			
			request.insertValues(getTableName(), Arrays.asList("title","content","price","category","user","state","lock", "date", "secure_id"), Arrays.asList(title, content, price, categoryID, userDatabaseID, stateID, 1, new Date(System.currentTimeMillis()), postSecureId));
			
			final CachedRowSet requestPostDatabaseID = request.getValues("SELECT LAST_INSERT_ID() AS lid;"); //Get the id for the fresh created post
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
			
			final CachedRowSet res = request.getValuesWithCondition("SELECT "+getTableName()+".id AS p_id, "+getTableName()+".secure_id AS p_sid, "+getTableName()+".title AS p_title, "+USER_TABLE_NAME+".name AS u_name, "+USER_TABLE_NAME+".surname AS u_surname, "+getTableName()+".price AS p_price, "+CATEGORY_TABLE_NAME+".name AS c_name, "+POST_STATE_TABLE_NAME+".name AS ps_name, "+getTableName()+".date AS p_date FROM "+getTableName()+" "+
							"INNER JOIN "+USER_TABLE_NAME+" ON "+getTableName()+".user = "+USER_TABLE_NAME+".id "+
							"INNER JOIN "+CATEGORY_TABLE_NAME+" ON "+getTableName()+".category = "+CATEGORY_TABLE_NAME+".id "+
							"INNER JOIN "+POST_STATE_TABLE_NAME+" ON "+getTableName()+".state = "+POST_STATE_TABLE_NAME+".id " +
							whereClause.getValueA()+" "+
							orderClause+
							" LIMIT "+postNumber+" OFFSET "+(pageNumber*postNumber)+";", whereClause.getValueB());
			
			while(res.next()) {
				final SimplifiedEntity u = new SimplifiedEntity(res.getString("u_name"), res.getString("u_surname"));
				final Category c = new Category("c_name");
				final PostState ps = new PostState("ps_name");
				
				final List<String> postImages = fetchPostImages(request, res.getInt("p_id"), res.getString("p_sid"), 1);
				if(postImages.isEmpty()) postImages.add(SFTPHelper.getFormattedImageURL(ImageDirectory.ROOT, "", "1.jpg"));
				
				post.add(new Post(res.getString("p_sid"), res.getString("p_title"), u, res.getInt("p_price"), res.getDate("p_date"), c, ps, postImages.get(0)));
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
		
		if(userSecureID != null) { sb.append(USER_TABLE_NAME+".secure_id=? AND "); whereObj.add(userSecureID); }
		if(parameters.isCategoryPresent()) { sb.append(CATEGORY_TABLE_NAME+".id=? AND "); whereObj.add(parameters.getCategoryID()); }
		if(parameters.isStatePresent()) { sb.append(POST_STATE_TABLE_NAME+".id=? AND "); whereObj.add(parameters.getStateID()); }
		if(parameters.isMaxPricePresent()) { sb.append(getTableName()+".price<=? AND "); whereObj.add(parameters.getMaxPrice()); }
		if(parameters.mustBeValidated()) { sb.append(getTableName()+".lock=? AND "); whereObj.add(0); }
		sb.setLength(sb.length()-5);
		
		return whereObj.size() != 0 ? new Tuple<>(sb.toString(), whereObj) : new Tuple<>("", whereObj);
	}

	public PostComplete fetchEntirePost(String postID) {
		PostComplete pc = null;
		DatabaseRequest request = null;
		
		try {
			request = new DatabaseRequest(factory, credentials);
			
			final CachedRowSet res = request.getValuesWithCondition("SELECT "+getTableName()+".id AS p_id, "+getTableName()+".secure_id AS p_sid, "+getTableName()+".title AS p_title, "+getTableName()+".content AS p_content, "+USER_TABLE_NAME+".name AS u_name, "+USER_TABLE_NAME+".surname AS u_surname, "+getTableName()+".price AS p_price, "+CATEGORY_TABLE_NAME+".name AS c_name, "+POST_STATE_TABLE_NAME+".name AS ps_name, "+getTableName()+".date AS p_date FROM "+getTableName()+" "+
					"INNER JOIN "+USER_TABLE_NAME+" ON "+getTableName()+".user = "+USER_TABLE_NAME+".id "+
					"INNER JOIN "+CATEGORY_TABLE_NAME+" ON "+getTableName()+".category = "+CATEGORY_TABLE_NAME+".id "+
					"INNER JOIN "+POST_STATE_TABLE_NAME+" ON "+getTableName()+".state = "+POST_STATE_TABLE_NAME+".id"+
					"WHERE "+getTableName()+".secure_id=?", Arrays.asList(postID));
			
			if(res.next()) {
				final SimplifiedEntity u = new SimplifiedEntity(res.getString("u_name"), res.getString("u_surname"));
				final Category c = new Category("c_name");
				final PostState ps = new PostState("ps_name");
				
				final List<String> postImages = fetchPostImages(request, res.getInt("p_id"), res.getString("p_sid"), 4);
				if(postImages.isEmpty()) postImages.add(SFTPHelper.getFormattedImageURL(ImageDirectory.ROOT, "", "1.jpg"));
				
				pc = new PostComplete(res.getString("p_sid"), res.getString("p_title"), u, res.getFloat("p_price"), res.getDate("p_date"), res.getString("p_content"), c, ps, postImages.get(0), postImages.subList(1, postImages.size()));
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
	
	private List<String> fetchPostImages(DatabaseRequest req, int postDatabaseID, String postSecureID, int limit) {
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
