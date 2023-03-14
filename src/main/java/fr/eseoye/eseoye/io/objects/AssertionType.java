package fr.eseoye.eseoye.io.objects;

public enum AssertionType {

	NOT_EQUAL("Not equal"),
	NOT_NULL("Not null"),
	INSTANCE_OF("Instance Of");
	
	final String name;
	private AssertionType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}