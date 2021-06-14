
// categoryGroup.java, Gabriel Seaver, 2021

package safekeeper.groupings;

import java.io.Serializable;
import java.util.HashSet;

public class CategoryGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String name;
	public HashSet<AccountGroup> accountGroups;
	
	public CategoryGroup (String name) {
		this.name = name;
		accountGroups = new HashSet<>();
	}
	
	public Object[] getAccountsAlphabetical () {
		return Alphabetical.orderSetAlphabetically(accountGroups, account -> ((AccountGroup)account).getDisplayName());
	}
	
	// Used for the dropdown fields in MainWindow
	public String toString () {
		return name;
	}
	
}