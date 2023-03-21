package fr.eseoye.eseoye.helpers;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.eseoye.eseoye.helpers.SFTPHelper.ImageDirectory;

public class HelperTest {

	@Test
	void test() {
		String a = RequestHelper.convertArgumentsToUpdateFields(Arrays.asList("this","is","a","test"));
		String b = RequestHelper.convertListToDatabaseFields(Arrays.asList("this","is","a","test"));
		String c = RequestHelper.generateRequestEmptyValues(4);
		String d = RequestHelper.convertListToDatabaseFields(Arrays.asList("test"));
		
		assertEquals(a, "`this`=?, `is`=?, `a`=?, `test`=?");
		assertEquals(b, "`this`, `is`, `a`, `test`");
		assertEquals(c, "?, ?, ?, ?");
		assertEquals(d, "`test`");
		
		final String id1 = SecurityHelper.generateSecureID(System.currentTimeMillis(), 1, 8);
		final String id2 = SecurityHelper.generateSecureID(System.currentTimeMillis(), 3, 8);
		
		//Two secure ID shouldn't be able to have the same value
		assertNotEquals(id1, id2);
		
		final String url1 = SFTPHelper.getFormattedImageURL(ImageDirectory.ROOT, "", "test");
		final String url2 = SFTPHelper.getFormattedImageURL(ImageDirectory.USER, "foldertest", "test");
		
		assertEquals(url1, "http://eseoye.elouan-lerissel.fr/test.jpg");
		assertEquals(url2, "http://eseoye.elouan-lerissel.fr/user/foldertest/test.jpg");
	}
	
}

