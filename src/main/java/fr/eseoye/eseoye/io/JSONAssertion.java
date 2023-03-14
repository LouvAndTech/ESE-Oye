package fr.eseoye.eseoye.io;

import java.util.function.BooleanSupplier;

import fr.eseoye.eseoye.exceptions.JSONAssertionException;

public class JSONAssertion {

	public static void assertInstanceof(Object testedObj, Class<?> reference, String testedKey) {
		if(!reference.isInstance(testedObj)) throw new JSONAssertionException("Object instance of condition wasn't met", testedKey, AssertionType.INSTANCE_OF, reference);
	}

	public static void assertInstanceof(Object testedObj, Class<?> reference) {
		if(!reference.isInstance(testedObj)) throw new JSONAssertionException("Object instance of condition wasn't met", AssertionType.INSTANCE_OF, reference);
	}
	
	public static void assertTrue(boolean condition, String testedKey) {
		if(!condition) throw new JSONAssertionException("Boolean condition wasn't met", testedKey, AssertionType.NOT_EQUAL);
	}

	public static void assertTrue(boolean condition) {
		if(!condition) throw new JSONAssertionException("Boolean condition wasn't met", AssertionType.NOT_EQUAL);
	}
	
	public static void assertTrue(BooleanSupplier condition, String testedKey) {
		if(!condition.getAsBoolean()) throw new JSONAssertionException("Boolean condition wasn't met", testedKey, AssertionType.NOT_EQUAL);
	}

	public static void assertTrue(BooleanSupplier condition) {
		if(!condition.getAsBoolean()) throw new JSONAssertionException("Boolean condition wasn't met", AssertionType.NOT_EQUAL);
	}
	
	public static void assertNotNull(Object obj, String testedKey) {
		if(obj == null) throw new JSONAssertionException("Object not null condition wasn't met", testedKey, AssertionType.NOT_NULL);
	}
	
	public static void assertNotNull(Object obj) {
		if(obj == null) throw new JSONAssertionException("Object not null condition wasn't met", AssertionType.NOT_NULL);
	}
	
	
}