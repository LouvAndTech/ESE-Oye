package fr.eseoye.eseoye.beans;

import java.sql.Date;

public class User {

	private String id;
	
	private String name, surname;
	private String password;
	private Date birth;
	private String phone;
	private String mail;
	private String state;
	
	public User(String id, String name, String surname, String password, Date birth, String phone, String mail, String state) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.birth = birth;
		this.phone = phone;
		this.mail = mail;
		this.state = state;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
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
