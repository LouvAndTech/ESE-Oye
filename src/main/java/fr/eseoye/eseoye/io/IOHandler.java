package fr.eseoye.eseoye.io;

import java.io.IOException;

import fr.eseoye.eseoye.io.json.JSONConfiguration;

public class IOHandler {
	
	private static IOHandler instance;
	
	private final JSONConfiguration configuration;

	private IOHandler() {
		this.configuration = new JSONConfiguration("data");
	}
	
	public boolean saveAllFiles() {
		System.out.println("Saving 3 files...");
		
		try {
			this.configuration.saveFile();
			
		} catch (IOException e) {
			System.out.println("Couldn't save the file properly "+e.getMessage());
			return false;
		}
		
		System.out.println("Successfully saved 3 files");
		return true;
	}
	
	public JSONConfiguration getConfiguration() {
		return this.configuration;
	}
	
	public static IOHandler getInstance() {
		if(instance == null) {
			synchronized (IOHandler.class) {
				if(instance == null) instance = new IOHandler();
			}
		}
		return instance;
	}
	
}
