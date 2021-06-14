
// AccountGroup.java, Gabriel Seaver, 2021

package safekeeper.groupings;

import java.io.Serializable;
import java.util.Date;

public class AccountGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String username, email, notes, company;
	private String password, lastPassword;
	private Date passwordLastChangedDate;
	
	public CategoryGroup category;
	
	public AccountGroup (CategoryGroup category) {
		this("", "", "", "", null, category);
	}
	
	public AccountGroup (String company, String username, String email, String notes, String password, CategoryGroup category) {
		this.company = company;
		this.username = username;
		this.email = email;
		this.notes = notes;
		this.password = password;
		this.category = category;
		
		lastPassword = null;
		passwordLastChangedDate = null;
	}
	
	public String getDisplayName () {
		return company + " (" + username + ")";
	}
	
	public String getPassword () {
		return password;
	}
	
	public String getLastPassword () {
		return lastPassword;
	}
	
	public Date getPasswordLastChangedDate () {
		return passwordLastChangedDate;
	}
	
	public void setPassword (String newPassword) {
		lastPassword = password;
		password = newPassword;
		passwordLastChangedDate = new Date();
	}
	
}