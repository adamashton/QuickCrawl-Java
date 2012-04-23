package core.support;

import java.util.Random;

public class DBSupport {
	public static String generateID(int length) {
		StringBuilder result = new StringBuilder();
		
		Random rand = new Random();
		
		length = rand.nextInt(length-1) + 1;
		
		for (int i=0; i<length; i++) {
			result.append((char) (rand.nextInt(25) + 97));  
		}
		
		return result.toString();
	}
}
