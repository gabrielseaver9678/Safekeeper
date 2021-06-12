/*    */ package safekeeper;
/*    */ 
/*    */ import safekeeper.gui.util.GUIUtils;
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
/*    */ public class Entry
/*    */ {
/*    */   public static void main(String[] paramArrayOfString) {
/*    */     try {
/* 30 */       new Program(paramArrayOfString);
/* 31 */     } catch (Exception exception) {
/* 32 */       exception.printStackTrace();
/* 33 */       GUIUtils.showFatalError(exception);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\Entry.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */