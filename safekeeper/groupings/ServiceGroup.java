package safekeeper.groupings;

import java.io.Serializable;
import java.util.HashSet;

public class ServiceGroup implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String name;
	
	public HashSet<AccountGroup> accountGroups;
	
	public ServiceGroup(String paramString) {
		this.name = paramString;
		this.accountGroups = new HashSet<>();
	}
	
	public Object[] getAccountsAlphabetical() {
		return Alphabetical.orderSetAlphabetically(this.accountGroups, paramObject -> ((AccountGroup)paramObject).username);
	}
	
	public String toString() {
		return this.name;
	}
}
