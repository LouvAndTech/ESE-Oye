package fr.eseoye.eseoye.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import fr.eseoye.eseoye.io.json.JSONConfiguration;

public class IOHandler {
	
	private static IOHandler instance;
	
	private final Path pathToSaveable;
	
	private final JSONConfiguration configuration;

	private IOHandler() {
		this.pathToSaveable = Path.of("datas");
		if(!Files.exists(pathToSaveable)) {
			try {
				Files.createDirectories(pathToSaveable);
			} catch (IOException e) {
				System.out.println("An error occured while creaating directory "+this.pathToSaveable.toAbsolutePath().toString()+" "+e.getMessage());
			}
			System.out.println("Directory "+this.pathToSaveable.toAbsolutePath().toString()+" has been successfully created !");
		}
		else System.out.println("Directory "+this.pathToSaveable.toAbsolutePath().toString()+" has been successfully loaded !");
		
		this.configuration = new JSONConfiguration(this.pathToSaveable.toAbsolutePath().toString());
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
