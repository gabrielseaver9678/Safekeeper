/*     */ package safekeeper.gui.frames;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import safekeeper.gui.components.PasswordField;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecurePasswordLogin
/*     */   implements ActionListener
/*     */ {
/*     */   private String password;
/*     */   private final JDialog window;
/*     */   private final PasswordField passwordField;
/*     */   private final CheckPasswordFunction checkPassword;
/*     */   private final CountDownLatch waitForCorrectPassword;
/*     */   private boolean shutdownIfClosed;
/*     */   
/*     */   private SecurePasswordLogin(JFrame paramJFrame, CheckPasswordFunction paramCheckPasswordFunction) throws InterruptedException {
/*  51 */     this.shutdownIfClosed = true;
/*  52 */     this.checkPassword = paramCheckPasswordFunction;
/*     */ 
/*     */     
/*  55 */     this.waitForCorrectPassword = new CountDownLatch(1);
/*     */ 
/*     */     
/*  58 */     this.window = GUIUtils.makeNewDialog("Safekeeper Login", paramJFrame, () -> {
/*     */           if (this.shutdownIfClosed) {
/*     */             System.exit(0);
/*     */           }
/*     */         });
/*     */     
/*  64 */     this.passwordField = new PasswordField(25);
/*  65 */     this.passwordField.addPasswordSubmittedListener(this);
/*     */ 
/*     */     
/*  68 */     JLabel jLabel = GUIUtils.makeLabel("Enter your master password:", true);
/*  69 */     jLabel.setLabelFor((Component)this.passwordField);
/*     */ 
/*     */     
/*  72 */     JPanel jPanel = new JPanel(new FlowLayout(1));
/*  73 */     jPanel.add(jLabel);
/*  74 */     jPanel.add((Component)this.passwordField);
/*     */ 
/*     */     
/*  77 */     this.window.add(jPanel);
/*  78 */     this.window.pack();
/*     */ 
/*     */     
/*  81 */     this.waitForCorrectPassword.await();
/*     */   }
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent paramActionEvent) {
/*  86 */     this.password = this.passwordField.getPassword();
/*  87 */     this.passwordField.clearPassword();
/*  88 */     if (this.checkPassword.checkCorrectPassword(this.password)) {
/*  89 */       this.shutdownIfClosed = false;
/*  90 */       this.window.dispose();
/*  91 */       this.waitForCorrectPassword.countDown();
/*     */     } else {
/*  93 */       GUIUtils.playErrorSound();
/*  94 */       GUIUtils.showWarning("The entered master password was incorrect,\nor the vault file is corrupted.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String login(JFrame paramJFrame, CheckPasswordFunction paramCheckPasswordFunction) throws InterruptedException {
/* 101 */     SecurePasswordLogin securePasswordLogin = new SecurePasswordLogin(paramJFrame, paramCheckPasswordFunction);
/* 102 */     return securePasswordLogin.password;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface CheckPasswordFunction {
/*     */     boolean checkCorrectPassword(String param1String);
/*     */   }
/*     */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\frames\SecurePasswordLogin.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */