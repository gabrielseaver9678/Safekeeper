
// AccountGroup.java, Gabriel Seaver, 2021

package safekeeper.groupings;

import java.io.Serializable;
import java.util.Date;

public class AccountGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String username, email, notes;
	private String password, lastPassword;
	private Date passwordLastChangedDate;
	
	public final ServiceGroup service;
	
	public AccountGroup (ServiceGroup service) {
		this(null, null, "", "", service);
	}
	
	public AccountGroup (String username, String email, String notes, String password, ServiceGroup service) {
		this.username = username;
		this.email = email;
		this.notes = notes;
		this.password = password;
		this.service = service;
		
		lastPassword = null;
		passwordLastChangedDate = null;
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