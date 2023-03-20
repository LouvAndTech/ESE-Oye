package fr.eseoye.eseoye.beans;

public class SimplifiedEntity {

	private String secureID;
	
	private String name, surname;
	
	public SimplifiedEntity(String secureID, String name, String surname) {
		this.secureID = secureID;
		this.name = name;
		this.surname = surname;
	}
	
	public SimplifiedEntity(String name, String surname) {
		this(null, name, surname);
	}
	
	public boolean isSecureIDPresent() {
		return secureID != null;
	}
	
	public String getSecureID() {
		return secureID;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}
	
}
