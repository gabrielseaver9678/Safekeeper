/*    */ package safekeeper.gui.frames;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.JPanel;
/*    */ import safekeeper.groupings.AccountGroup;
/*    */ import safekeeper.groupings.ServiceGroup;
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
/*    */ public class CreateAccountWindow
/*    */   extends AccountWindow
/*    */ {
/*    */   public static CreateAccountWindow newWindow(MainWindow paramMainWindow, ServiceGroup paramServiceGroup) {
/* 32 */     AccountGroup accountGroup = new AccountGroup(paramServiceGroup);
/* 33 */     return new CreateAccountWindow("Create New Account (" + paramServiceGroup.name + ")", paramMainWindow, accountGroup);
/*    */   }
/*    */   
/*    */   private CreateAccountWindow(String paramString, MainWindow paramMainWindow, AccountGroup paramAccountGroup) {
/* 37 */     super(paramString, paramMainWindow, paramAccountGroup, false, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected JPanel createButtonPanel() {
/* 42 */     JPanel jPanel = new JPanel(new BorderLayout());
/* 43 */     jPanel.add(GUIUtils.makeButton("Save New Account", paramActionEvent -> saveNewAccount()));
/* 44 */     return jPanel;
/*    */   }
/*    */   
/*    */   private void saveNewAccount() {
/* 48 */     if (validateUsernameAndPassword(this.usernameField.getText(), this.passwordField.getPassword())) {
/*    */ 
/*    */       
/* 51 */       this.accountGroup.username = this.usernameField.getText();
/* 52 */       this.accountGroup.email = this.emailField.getText();
/* 53 */       this.accountGroup.setPassword(this.passwordField.getPassword());
/* 54 */       this.accountGroup.notes = this.notesField.getText();
/*    */ 
/*    */       
/* 57 */       this.accountGroup.service.accountGroups.add(this.accountGroup);
/*    */ 
/*    */       
/* 60 */       closeWindow();
/* 61 */       vaultEdited();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onClosing() {
/* 67 */     int i = GUIUtils.showOptionChooser(this, "If you close this window now, the new\naccount will not be saved.", "Close Without Saving Account?", new String[] { "Close Anyways", "Do Not Close" }, 0);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 76 */     if (i != 0) {
/*    */       return;
/*    */     }
/* 79 */     closeWindow();
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\frames\CreateAccountWindow.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */