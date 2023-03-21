package fr.eseoye.eseoye.io.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FetchPostFilterTest {
	
	@Test
	void test() {
		FetchPostFilter fetch = FetchPostFilter.builder().keyWords("un","test").maxPrice(150).state(5).build();

		assertEquals(fetch.isKeyWordsListEmpty(), false);
		assertEquals(fetch.getMaxPrice(), (float)150);
		assertEquals(fetch.getStateID(), 5);
	}
	
}
