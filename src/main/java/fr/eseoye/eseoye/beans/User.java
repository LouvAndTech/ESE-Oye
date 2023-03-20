package fr.eseoye.eseoye.beans;

import java.sql.Date;

public class User extends SimplifiedEntity {

	private String secureID;
	
	private String password;
	private Date birth;
	private String phone;
	private String mail;
	private String state;
	
	public User(String secureID, String name, String surname, String password, Date birth, String phone, String mail, String state) {
		super(secureID, name, surname);
		this.secureID = secureID;
		this.password = password;
		this.birth = birth;
		this.phone = phone;
		this.mail = mail;
		this.state = state;
	}
	
	public String getId() {
		return secureID;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Date getBirth() {
		return birth;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getMail() {
		return mail;
	}
	
	public String getState() {
		return state;
	}
}
