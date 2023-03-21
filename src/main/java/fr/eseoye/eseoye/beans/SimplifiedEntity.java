package fr.eseoye.eseoye.beans;

public class SimplifiedEntity {

	private String secureID;
	
	private String name, surname;
	private String phone, mail;
	
	public SimplifiedEntity(String secureID, String name, String surname, String phone, String mail) {
		this.secureID = secureID;
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.mail = mail;
	}
	
	public SimplifiedEntity(String name, String surname, String phone, String mail) {
		this(null, name, surname, phone, mail);
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
	
	public String getPhone() {
		return phone;
	}
	
	public String getMail() {
		return mail;
	}
	
}
