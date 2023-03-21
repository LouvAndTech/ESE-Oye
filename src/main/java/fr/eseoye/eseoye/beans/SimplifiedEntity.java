package fr.eseoye.eseoye.beans;

public class SimplifiedEntity {

	private String secureID;
	
	private String name, surname;
	private Boolean isAdmin, isLocked;
	
	public SimplifiedEntity(String secureID, String name, String surname) {
		this.secureID = secureID;
		this.name = name;
		this.surname = surname;
	}
	
	public SimplifiedEntity(String name, String surname) {
		this(null, name, surname);
	}

	public SimplifiedEntity(String name, String surname, String secureID, Boolean isAdmin, Boolean isLocked) {
		this(secureID, name, surname);
		this.isAdmin = isAdmin;
		this.isLocked = isLocked;
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

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}
	
}
