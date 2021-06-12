package safekeeper.groupings;

import java.util.Arrays;
import java.util.Set;

public class Alphabetical {
	public static <T> Object[] orderSetAlphabetically(Set<T> paramSet, StringFromObject paramStringFromObject) {
		Object[] arrayOfObject = paramSet.toArray();
		Arrays.sort(arrayOfObject, (paramObject1, paramObject2) -> paramStringFromObject.getString(paramObject1).toLowerCase().compareTo(paramStringFromObject.getString(paramObject2).toLowerCase()));
		return arrayOfObject;
	}
	
	public static interface StringFromObject {
		String getString(Object param1Object);
	}
}
