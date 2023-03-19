package fr.eseoye.eseoye.beans;

public class Category {
	
	private int id;
	private String name;
	
	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Category(String name) {
		this.id = -1;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
