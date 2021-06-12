/*    */ package safekeeper.gui.components;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.BoxLayout;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JPasswordField;
/*    */ import javax.swing.text.Document;
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
/*    */ public class PasswordField
/*    */   extends JPanel
/*    */ {
/*    */   private static final String SHOW = "Show";
/*    */   private static final String HIDE = "Hide";
/*    */   private JPasswordField passwordField;
/*    */   private JButton showButton;
/*    */   private boolean isPasswordVisible;
/*    */   
/*    */   public PasswordField(String paramString, int paramInt) {
/* 42 */     BoxLayout boxLayout = new BoxLayout(this, 0);
/* 43 */     setLayout(boxLayout);
/*    */     
/* 45 */     setAlignmentY(0.5F);
/*    */     
/* 47 */     this.showButton = GUIUtils.makeButton("Show", paramActionEvent -> setPasswordIsVisible(!this.isPasswordVisible));
/*    */     
/* 49 */     this.passwordField = new JPasswordField(paramString, paramInt);
/* 50 */     setPasswordIsVisible(false);
/* 51 */     this.passwordField.setFont(GUIUtils.fontPassword);
/*    */     
/* 53 */     add(this.passwordField);
/* 54 */     add(this.showButton);
/*    */   }
/*    */   
/*    */   public PasswordField(int paramInt) {
/* 58 */     this("", paramInt);
/*    */   }
/*    */   
/*    */   public void setPasswordIsVisible(boolean paramBoolean) {
/* 62 */     this.isPasswordVisible = paramBoolean;
/* 63 */     this.showButton.setText(this.isPasswordVisible ? "Hide" : "Show");
/*    */     
/* 65 */     this.passwordField.setEchoChar(this.isPasswordVisible ? Character.MIN_VALUE : 42);
/*    */   }
/*    */   
/*    */   public void addPasswordSubmittedListener(ActionListener paramActionListener) {
/* 69 */     this.passwordField.addActionListener(paramActionListener);
/*    */   }
/*    */   
/*    */   public String getPassword() {
/* 73 */     return new String(this.passwordField.getPassword());
/*    */   }
/*    */   
/*    */   public void setPassword(String paramString) {
/* 77 */     this.passwordField.setText(paramString);
/*    */   }
/*    */   
/*    */   public void clearPassword() {
/* 81 */     this.passwordField.setText("");
/*    */   }
/*    */   
/*    */   public void setEditable(boolean paramBoolean) {
/* 85 */     this.passwordField.setEditable(paramBoolean);
/*    */   }
/*    */   
/*    */   public Document getDocument() {
/* 89 */     return this.passwordField.getDocument();
/*    */   }
/*    */   
/*    */   public void setFieldBackground(Color paramColor) {
/* 93 */     this.passwordField.setBackground(paramColor);
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\components\PasswordField.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */