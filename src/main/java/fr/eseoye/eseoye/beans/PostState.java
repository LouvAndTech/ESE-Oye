package fr.eseoye.eseoye.beans;

public class PostState {

	private int id;
	private String name;
	
	public PostState(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public PostState(String name) {
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
