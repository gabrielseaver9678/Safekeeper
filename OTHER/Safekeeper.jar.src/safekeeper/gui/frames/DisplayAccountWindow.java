/*     */ package safekeeper.gui.frames;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.JPanel;
/*     */ import safekeeper.groupings.AccountGroup;
/*     */ import safekeeper.gui.util.GUIUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DisplayAccountWindow
/*     */   extends AccountWindow
/*     */ {
/*     */   public DisplayAccountWindow(MainWindow paramMainWindow, AccountGroup paramAccountGroup) {
/*  31 */     super("", paramMainWindow, paramAccountGroup, true, true);
/*  32 */     updateTitle();
/*     */   }
/*     */   
/*     */   private void updateTitle() {
/*  36 */     setTitle("Account: " + this.accountGroup.username + " (" + this.accountGroup.service.name + ")");
/*     */   }
/*     */ 
/*     */   
/*     */   protected JPanel createButtonPanel() {
/*  41 */     JPanel jPanel = new JPanel(new BorderLayout());
/*  42 */     jPanel.add(GUIUtils.makeButton("Save Changes", paramActionEvent -> saveAccountChanges()), "North");
/*  43 */     jPanel.add(GUIUtils.makeButton("Delete Account", paramActionEvent -> deleteAccount()), "South");
/*  44 */     return jPanel;
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveAccountChanges() {
/*  49 */     if (!anyFieldEdited()) {
/*     */       
/*  51 */       GUIUtils.showWarning("There are no changes to save.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  56 */     if (!validateUsernameAndPassword(this.usernameField.getText(), this.passwordField.getPassword())) {
/*     */       return;
/*     */     }
/*     */     
/*  60 */     String str = "";
/*  61 */     str = str + str;
/*  62 */     str = str + str;
/*  63 */     str = str + str;
/*  64 */     str = str + str;
/*     */ 
/*     */     
/*  67 */     int i = GUIUtils.showOptionChooser(this, "Are you sure you want to save the changes made to your:" + str, "Save Changes?", new String[] { "Save", "Do Not Save" }, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     if (i != 0) {
/*     */       return;
/*     */     }
/*  77 */     this.accountGroup.username = this.usernameField.getText();
/*  78 */     if (this.passwordEdited) this.accountGroup.setPassword(this.passwordField.getPassword()); 
/*  79 */     this.accountGroup.email = this.emailField.getText();
/*  80 */     this.accountGroup.notes = this.notesField.getText();
/*     */ 
/*     */     
/*  83 */     checkUsernameEdited();
/*  84 */     checkEmailEdited();
/*  85 */     checkNotesEdited();
/*  86 */     checkPasswordEdited();
/*     */     
/*  88 */     updateTitle();
/*     */ 
/*     */     
/*  91 */     vaultEdited();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onClosing() {
/*  98 */     if (anyFieldEdited()) {
/*  99 */       int i = GUIUtils.showOptionChooser(this, "If you close this window now, changes you have\nmade to the account will not be saved.", "Close Without Saving Changes?", new String[] { "Close Anyways", "Do Not Close" }, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 108 */       if (i != 0) {
/*     */         return;
/*     */       }
/*     */     } 
/* 112 */     closeWindow();
/*     */   }
/*     */   
/*     */   private void deleteAccount() {
/* 116 */     int i = GUIUtils.showOptionChooser(this, "Are you sure you want to delete this account from your vault?", "Delete Account?", new String[] { "Delete Permanently", "Do Not Delete" }, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     if (i != 0)
/*     */       return; 
/* 125 */     this.accountGroup.service.accountGroups.remove(this.accountGroup);
/*     */     
/* 127 */     closeWindow();
/* 128 */     vaultEdited();
/*     */   }
/*     */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\frames\DisplayAccountWindow.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */