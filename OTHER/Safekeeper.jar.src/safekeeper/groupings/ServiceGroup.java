/*    */ package safekeeper.groupings;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.HashSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServiceGroup
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String name;
/*    */   public HashSet<AccountGroup> accountGroups;
/*    */   
/*    */   public ServiceGroup(String paramString) {
/* 36 */     this.name = paramString;
/* 37 */     this.accountGroups = new HashSet<>();
/*    */   }
/*    */   
/*    */   public Object[] getAccountsAlphabetical() {
/* 41 */     return Alphabetical.orderSetAlphabetically(this.accountGroups, paramObject -> ((AccountGroup)paramObject).username);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\groupings\ServiceGroup.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */