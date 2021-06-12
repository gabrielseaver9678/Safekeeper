/*    */ package safekeeper.printing;
/*    */ 
/*    */ import java.awt.print.PrinterException;
/*    */ import java.awt.print.PrinterJob;
/*    */ import java.text.MessageFormat;
/*    */ import javax.swing.JTextArea;
/*    */ import safekeeper.groupings.AccountGroup;
/*    */ import safekeeper.groupings.ServiceGroup;
/*    */ import safekeeper.groupings.ServiceGroupList;
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
/*    */ 
/*    */ 
/*    */ public class SGLPrinter
/*    */ {
/*    */   private static boolean print(String paramString) throws PrinterException {
/* 38 */     JTextArea jTextArea = new JTextArea(paramString);
/* 39 */     jTextArea.setLineWrap(true);
/* 40 */     jTextArea.setFont(GUIUtils.fontPrinter);
/*    */     
/* 42 */     PrinterJob printerJob = PrinterJob.getPrinterJob();
/* 43 */     MessageFormat messageFormat1 = null;
/* 44 */     MessageFormat messageFormat2 = new MessageFormat("Page {0}");
/*    */     
/* 46 */     printerJob.setPrintable(jTextArea.getPrintable(messageFormat1, messageFormat2));
/* 47 */     if (printerJob.printDialog()) {
/* 48 */       printerJob.print();
/* 49 */       return true;
/* 50 */     }  return false;
/*    */   }
/*    */   
/*    */   private static String getTextToPrint(ServiceGroupList paramServiceGroupList) {
/* 54 */     Object[] arrayOfObject = paramServiceGroupList.getServicesAlphabetical();
/* 55 */     if (arrayOfObject.length == 0) return "[No Services]";
/*    */     
/* 57 */     String str = "";
/* 58 */     for (Object object : arrayOfObject) {
/* 59 */       str = str + str;
/*    */     }
/* 61 */     return str;
/*    */   }
/*    */   
/*    */   private static String getTextToPrint(ServiceGroup paramServiceGroup) {
/* 65 */     Object[] arrayOfObject = paramServiceGroup.getAccountsAlphabetical();
/* 66 */     String str = paramServiceGroup.name + "\n";
/*    */     
/* 68 */     if (arrayOfObject.length == 0) return "  [No Accounts]\n";
/*    */     
/* 70 */     for (Object object : arrayOfObject) {
/* 71 */       str = str + str;
/*    */     }
/* 73 */     return str;
/*    */   }
/*    */   
/*    */   private static String getTextToPrint(AccountGroup paramAccountGroup) {
/* 77 */     String str = "  " + paramAccountGroup.username + "\n";
/*    */ 
/*    */     
/* 80 */     if (!paramAccountGroup.email.equals("")) {
/* 81 */       str = str + "    Email:    " + str + "\n";
/*    */     }
/* 83 */     str = str + "    Password: " + str + "\n";
/*    */     
/* 85 */     return str;
/*    */   }
/*    */   
/*    */   public static boolean printSGL(ServiceGroupList paramServiceGroupList) throws PrinterException {
/* 89 */     return print(getTextToPrint(paramServiceGroupList));
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\printing\SGLPrinter.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */