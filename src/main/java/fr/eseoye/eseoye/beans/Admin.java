package fr.eseoye.eseoye.beans;

public class Admin {

	private String id;
	private String name, surname;
	private String password;	
	private int rank;
	
	public Admin(String id, String name, String surname, String password, int rank) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.rank = rank;
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
	
	public int getRank() {
		return rank;
	}
	
}
