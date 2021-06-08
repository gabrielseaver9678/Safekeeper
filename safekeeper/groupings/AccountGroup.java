package safekeeper.groupings;

import java.io.Serializable;
import java.util.Date;

public class AccountGroup implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public String username;
  
  public String email;
  
  public String notes;
  
  private String password;
  
  private String lastPassword;
  
  private Date passwordLastChangedDate;
  
  public final ServiceGroup service;
  
  public AccountGroup(ServiceGroup paramServiceGroup) {
    this("", null, "", "", paramServiceGroup);
  }
  
  public AccountGroup(String paramString1, String paramString2, String paramString3, String paramString4, ServiceGroup paramServiceGroup) {
    this.username = paramString1;
    this.email = paramString3;
    this.notes = paramString4;
    this.password = paramString2;
    this.service = paramServiceGroup;
    this.lastPassword = null;
    this.passwordLastChangedDate = null;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public String getLastPassword() {
    return this.lastPassword;
  }
  
  public Date getPasswordLastChangedDate() {
    return this.passwordLastChangedDate;
  }
  
  public void setPassword(String paramString) {
    this.lastPassword = this.password;
    this.password = paramString;
    this.passwordLastChangedDate = new Date();
  }
}
