/*     */ package safekeeper.gui.frames;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.filechooser.FileNameExtensionFilter;
/*     */ import safekeeper.gui.components.PasswordField;
/*     */ import safekeeper.gui.components.PasswordGeneratorPanel;
/*     */ import safekeeper.gui.layout.MatchedPairsLayout;
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
/*     */ public class VaultFileSelector
/*     */ {
/*     */   public static final String EXTENSION = "skvault";
/*     */   
/*     */   public enum LoadOption
/*     */   {
/*  47 */     CREATE_NEW,
/*  48 */     LOAD_EXISTING,
/*  49 */     NONE;
/*     */   }
/*     */   
/*     */   public static LoadOption chooseCreateNewOrLoadExisting(JFrame paramJFrame) {
/*  53 */     int i = GUIUtils.showOptionChooser(paramJFrame, "Load an existing Safekeeper vault file,\nor create a new one?", "Vault Loading Options", new String[] { "Load Existing", "Create New" }, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     if (i == 1) return LoadOption.CREATE_NEW;
/*     */ 
/*     */     
/*  64 */     if (i == 0) return LoadOption.LOAD_EXISTING;
/*     */ 
/*     */     
/*  67 */     return LoadOption.NONE;
/*     */   }
/*     */   
/*     */   public static File selectVaultFile(JFrame paramJFrame) {
/*  71 */     JFileChooser jFileChooser = new JFileChooser();
/*  72 */     jFileChooser.setFileFilter(new FileNameExtensionFilter("Safekeeper Vault File (.skvault)", new String[] { "skvault" }));
/*     */     
/*  74 */     if (jFileChooser.showOpenDialog(paramJFrame) == 0)
/*  75 */       return jFileChooser.getSelectedFile(); 
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   public static File getNewVaultFile(JFrame paramJFrame) {
/*  80 */     JFileChooser jFileChooser = new JFileChooser();
/*  81 */     jFileChooser.setFileFilter(new FileNameExtensionFilter("Safekeeper Vault File (.skvault)", new String[] { "skvault" }));
/*     */     
/*  83 */     if (jFileChooser.showSaveDialog(paramJFrame) == 0) {
/*  84 */       File file = jFileChooser.getSelectedFile();
/*     */ 
/*     */       
/*  87 */       if (!file.getName().endsWith(".skvault"))
/*  88 */         return new File(file.getAbsolutePath() + ".skvault"); 
/*  89 */       return file;
/*  90 */     }  return null;
/*     */   }
/*     */   
/*     */   private static class ObjectHolder<T> { private T object;
/*     */     
/*     */     private ObjectHolder(T param1T) {
/*  96 */       this.object = param1T;
/*     */     } }
/*     */ 
/*     */   
/* 100 */   private static final Color INVALID_MASTER_PASSWORD_COLOR = new Color(255, 200, 200);
/*     */ 
/*     */ 
/*     */   
/*     */   public static String makeNewMasterPassword(final JFrame parentFrame) {
/* 105 */     final CountDownLatch latch = new CountDownLatch(1);
/* 106 */     final ObjectHolder password = new ObjectHolder(null);
/*     */     
/* 108 */     JDialog jDialog = GUIUtils.makeNewDialog("Set Master Password", parentFrame, () -> paramCountDownLatch.countDown());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     JPanel jPanel1 = new JPanel(new BorderLayout());
/* 118 */     JLabel jLabel = GUIUtils.makeLabel("Your master password will be used to log into your password vault,\nso it is vital that it is very secure. Please use at least 15\ncharacters, including symbols, numbers, and letters. Your master\npassword should not include any words or names. It is recommended\nthat you write it down in a safe location, because if it is lost,\nit will be impossible to open your password vault.", false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     jLabel.setBorder(GUIUtils.createMarginBorder(10));
/* 126 */     jPanel1.add(jLabel, "North");
/*     */ 
/*     */     
/* 129 */     JPanel jPanel2 = new JPanel();
/* 130 */     MatchedPairsLayout matchedPairsLayout = new MatchedPairsLayout(jPanel2);
/* 131 */     jPanel2.setLayout((LayoutManager)matchedPairsLayout);
/* 132 */     jPanel2.setBorder(GUIUtils.createMarginBorder(10));
/*     */ 
/*     */     
/* 135 */     final PasswordField passwordField = new PasswordField(1);
/* 136 */     matchedPairsLayout.addMatch(
/* 137 */         GUIUtils.makeLabel("New Master Password", true), (Container)passwordField1);
/*     */ 
/*     */ 
/*     */     
/* 141 */     final PasswordField passwordReentryField = new PasswordField(1);
/* 142 */     matchedPairsLayout.addMatch(
/* 143 */         GUIUtils.makeLabel("Password Re-Entry", true), (Container)passwordField2);
/*     */ 
/*     */ 
/*     */     
/* 147 */     final Runnable updatePasswordFieldBackground = new Runnable()
/*     */       {
/*     */         public void run() {
/* 150 */           boolean bool = VaultFileSelector.checkMasterPasswordValid(passwordField.getPassword(), false);
/* 151 */           Color color = bool ? Color.WHITE : VaultFileSelector.INVALID_MASTER_PASSWORD_COLOR;
/* 152 */           passwordField.setFieldBackground(color);
/* 153 */           passwordReentryField.setFieldBackground(color);
/*     */         }
/*     */       };
/*     */     
/* 157 */     passwordField1.getDocument().addDocumentListener(new DocumentListener()
/*     */         {
/*     */           public void insertUpdate(DocumentEvent param1DocumentEvent) {
/* 160 */             updatePasswordFieldBackground.run();
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeUpdate(DocumentEvent param1DocumentEvent) {
/* 165 */             updatePasswordFieldBackground.run();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void changedUpdate(DocumentEvent param1DocumentEvent) {}
/*     */         });
/* 172 */     runnable.run();
/*     */ 
/*     */     
/* 175 */     ActionListener actionListener = new ActionListener()
/*     */       {
/*     */         
/*     */         public void actionPerformed(ActionEvent param1ActionEvent)
/*     */         {
/* 180 */           if (passwordField.getPassword().isEmpty() || 
/* 181 */             !VaultFileSelector.checkMasterPasswordValid(passwordField.getPassword(), true)) {
/*     */             return;
/*     */           }
/*     */           
/* 185 */           if (passwordReentryField.getPassword().isEmpty()) {
/* 186 */             GUIUtils.showWarning("You must re-enter the master password into the re-entry field.");
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 191 */           if (!passwordField.getPassword().equals(passwordReentryField.getPassword())) {
/* 192 */             GUIUtils.showWarning("The master password and the re-entered password do not match.");
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 197 */           int i = GUIUtils.showOptionChooser(parentFrame, "Are you sure you want this to be your master password,\nwhich will be used to access this Safekeeper password\nvault in the future?", "Confirm Master Password", new String[] { "Confirm", "Cancel" }, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 207 */           if (i != 0) {
/*     */             return;
/*     */           }
/* 210 */           password.object = (T)passwordField.getPassword();
/* 211 */           latch.countDown();
/*     */         }
/*     */       };
/*     */     
/* 215 */     passwordField1.addPasswordSubmittedListener(actionListener);
/* 216 */     passwordField2.addPasswordSubmittedListener(actionListener);
/*     */ 
/*     */     
/* 219 */     matchedPairsLayout.addMatch(new JLabel(), 
/*     */         
/* 221 */         GUIUtils.makeStretchPanel(GUIUtils.makeButton("Submit", actionListener)));
/*     */     
/* 223 */     matchedPairsLayout.setAutoCreateGaps(true);
/* 224 */     matchedPairsLayout.finalizeLayout();
/* 225 */     jPanel1.add(jPanel2, "Center");
/*     */ 
/*     */     
/* 228 */     ObjectHolder objectHolder2 = new ObjectHolder(null);
/* 229 */     objectHolder2.object = (T)new PasswordGeneratorPanel(12, 20, paramActionEvent -> {
/*     */           paramPasswordField.setPassword(((PasswordGeneratorPanel)paramObjectHolder.object).getPassword());
/*     */           
/*     */           paramPasswordField.setPasswordIsVisible(true);
/*     */         });
/* 234 */     jPanel1.add(GUIUtils.addMargin((Component)objectHolder2.object, 10), "South");
/*     */     
/* 236 */     jDialog.add(jPanel1);
/* 237 */     jDialog.pack();
/*     */ 
/*     */     
/* 240 */     try { countDownLatch.await(); } catch (Exception exception) {}
/*     */     
/* 242 */     jDialog.dispose();
/* 243 */     return (String)objectHolder1.object;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean checkMasterPasswordValid(String paramString, boolean paramBoolean) {
/* 248 */     if (paramString.length() < 12) {
/* 249 */       if (paramBoolean)
/* 250 */         GUIUtils.showWarning("The master password must be at least 12\ncharacters, 15 characters is recommended."); 
/* 251 */       return false;
/* 252 */     }  return true;
/*     */   }
/*     */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\frames\VaultFileSelector.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */