package fr.eseoye.eseoye.utils;

public class Tuple<A,B> {
	
	private A valueA;
	private B valueB;
	
	public Tuple() {
		this.valueA = null;
		this.valueB = null;
	}
	
	public Tuple(A a, B b) {
		this.valueA = a;
		this.valueB = b;
	}
	
	public A getValueA() {
		return valueA;
	}
	
	public B getValueB() {
		return valueB;
	}
	
	@Override
	public String toString() {
		return "("+valueA+", "+valueB+")";
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Tuple && equal((Tuple<?,?>)obj);
	}
	
	private boolean equal(Tuple<?, ?> tuple) {
		return tuple.getValueA().equals(valueA) &&
			tuple.getValueB().equals(valueB);
	}
	
}
