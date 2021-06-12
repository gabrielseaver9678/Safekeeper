/*    */ package safekeeper.groupings;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AccountGroup
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String username;
/*    */   public String email;
/*    */   public String notes;
/*    */   private String password;
/*    */   private String lastPassword;
/*    */   private Date passwordLastChangedDate;
/*    */   public final ServiceGroup service;
/*    */   
/*    */   public AccountGroup(ServiceGroup paramServiceGroup) {
/* 34 */     this("", null, "", "", paramServiceGroup);
/*    */   }
/*    */   
/*    */   public AccountGroup(String paramString1, String paramString2, String paramString3, String paramString4, ServiceGroup paramServiceGroup) {
/* 38 */     this.username = paramString1;
/* 39 */     this.email = paramString3;
/* 40 */     this.notes = paramString4;
/* 41 */     this.password = paramString2;
/* 42 */     this.service = paramServiceGroup;
/*    */     
/* 44 */     this.lastPassword = null;
/* 45 */     this.passwordLastChangedDate = null;
/*    */   }
/*    */   
/*    */   public String getPassword() {
/* 49 */     return this.password;
/*    */   }
/*    */   
/*    */   public String getLastPassword() {
/* 53 */     return this.lastPassword;
/*    */   }
/*    */   
/*    */   public Date getPasswordLastChangedDate() {
/* 57 */     return this.passwordLastChangedDate;
/*    */   }
/*    */   
/*    */   public void setPassword(String paramString) {
/* 61 */     this.lastPassword = this.password;
/* 62 */     this.password = paramString;
/* 63 */     this.passwordLastChangedDate = new Date();
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\groupings\AccountGroup.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */