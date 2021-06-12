
// Alphabetical.java, Gabriel Seaver, 2021

package safekeeper.groupings;

import java.util.Arrays;
import java.util.Set;

public class Alphabetical {
	
	public static interface StringFromObject {
		abstract String getString (Object obj);
	}
	
	public static <T> Object[] orderSetAlphabetically (Set<T> set, StringFromObject objToString) {
		Object[] arrayOfObject = set.toArray();
		Arrays.sort(arrayOfObject, (a, b) -> objToString.getString(a).toLowerCase().compareTo(objToString.getString(b).toLowerCase()));
		return arrayOfObject;
	}
	
}