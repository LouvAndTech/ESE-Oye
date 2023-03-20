package fr.eseoye.eseoye.io.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import fr.eseoye.eseoye.helpers.SecurityHelper;
import fr.eseoye.eseoye.io.SFTPFactory;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.OpenMode;
import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.SFTPClient;

public class SFTPConnection {
	
	private static final String ROOT_DIRECTORY = "upload";
	private static final String USER_DIRECTORY = "user";
	private static final String POST_DIRECTORY = "post";
	
	private static final String SEPARATOR = "/";
	
	private SFTPFactory factory;
	
	public SFTPConnection(SFTPFactory factory) {
		this.factory = factory;
	}
	
	public List<String> addNewPostImage(String postID, int lastDBid, List<InputStream> imagesList) throws IOException {
		SSHClient client = null;
		SFTPClient sftp = null; 
		final List<String> imagesId = new ArrayList<>();
		
		try {
			client= factory.createSSHClient();
			sftp = client.newSFTPClient();
				
			sftp.mkdirs(SEPARATOR+ROOT_DIRECTORY+SEPARATOR+POST_DIRECTORY+SEPARATOR+postID+SEPARATOR);
			
			for(int i = 0; i < imagesList.size(); i++) {
				final String fileName = SecurityHelper.generateSecureID(
						System.currentTimeMillis(), 
						lastDBid+i, 
						SecurityHelper.IMG_FILE_LENGTH);
				
				imagesId.add(fileName);
				
				RemoteFile remote = sftp.open(
						SEPARATOR+ROOT_DIRECTORY+SEPARATOR+POST_DIRECTORY+SEPARATOR+postID+SEPARATOR+fileName+".jpg", 
						EnumSet.of(OpenMode.CREAT, OpenMode.WRITE));
				
				OutputStream os = remote.new RemoteFileOutputStream() {
					
					@Override
					public void close() throws IOException {
						try { super.close(); }finally { remote.close(); };
					}
				};

				imagesList.get(i).transferTo(os);
				
				imagesList.get(i).close();
				os.close();
			}
			
			return imagesId;
		}catch(IOException e) {
			System.err.println(e);
			return null;
			
		}finally {
			if(sftp != null) sftp.close();
			if(client != null) {
				client.disconnect();
				client.close();
			}
		}
	}
	
	public void removePostImage(String postSecureID, String imageSecureID) throws IOException {
		SSHClient client = null;
		SFTPClient sftp = null;
		
		try {
			client= factory.createSSHClient();
			sftp = client.newSFTPClient();
			
			sftp.rm(SEPARATOR+ROOT_DIRECTORY+SEPARATOR+POST_DIRECTORY+SEPARATOR+postSecureID+SEPARATOR+imageSecureID+".jpg");
			
		}catch(IOException e) {
			System.err.println(e);
		}finally {
			if(sftp != null) sftp.close();
			if(client != null) {
				client.disconnect();
				client.close();
			}
		}
	}
	
	/**
	 * Add a new image for a user 
	 * @param userID
	 * @param imageObjectList
	 * @throws IOException 
	 */
	public String addNewUserImage(String userID, int lastDBid, InputStream inputStream) throws IOException {
		SSHClient client = null;
		SFTPClient sftp = null; 
		
		try {
			client= factory.createSSHClient();
			sftp = client.newSFTPClient();
		
			sftp.mkdirs(SEPARATOR+ROOT_DIRECTORY+SEPARATOR+USER_DIRECTORY+SEPARATOR+userID+SEPARATOR);
			
			final String fileName = SecurityHelper.generateSecureID(System.currentTimeMillis(), lastDBid, SecurityHelper.IMG_FILE_LENGTH);
			
			RemoteFile remote = sftp.open(
					SEPARATOR+ROOT_DIRECTORY+SEPARATOR+USER_DIRECTORY+SEPARATOR+userID+SEPARATOR+fileName+".jpg", 
					EnumSet.of(OpenMode.CREAT, OpenMode.WRITE));
			
			OutputStream os = remote.new RemoteFileOutputStream() {
				
				@Override
				public void close() throws IOException {
					try { super.close(); }finally { remote.close(); };
				}
			};

			inputStream.transferTo(os);
			
			inputStream.close();
			os.close();
			
			return fileName;
			
		}catch(IOException e) {
			System.err.println(e);
			
			return null;
		}finally {
			if(sftp != null) sftp.close();
			if(client != null) {
				client.disconnect();
				client.close();
			}
		}
	}
	
	public void removeUserImage(String userSecureID, String imageSecureID) throws IOException {
		SSHClient client = null;
		SFTPClient sftp = null;
		
		try {
			client= factory.createSSHClient();
			sftp = client.newSFTPClient();
			
			sftp.rm(SEPARATOR+ROOT_DIRECTORY+SEPARATOR+USER_DIRECTORY+SEPARATOR+userSecureID+SEPARATOR+imageSecureID+".jpg");
			
		}catch(IOException e) {
			System.err.println(e);
		}finally {
			if(sftp != null) sftp.close();
			if(client != null) {
				client.disconnect();
				client.close();
			}
		}
	}
	

}
