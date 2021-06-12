/*     */ package safekeeper.gui.frames;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.text.SimpleDateFormat;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.Document;
/*     */ import safekeeper.groupings.AccountGroup;
/*     */ import safekeeper.gui.components.DragHiddenTextField;
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
/*     */ 
/*     */ public abstract class AccountWindow
/*     */   extends JDialog
/*     */ {
/*     */   protected final AccountGroup accountGroup;
/*     */   private final MainWindow mainWindow;
/*     */   protected JDialog lastPasswordWindow;
/*     */   protected JTextField usernameField;
/*     */   protected JTextField emailField;
/*     */   protected JTextArea notesField;
/*     */   protected PasswordField passwordField;
/*     */   private PasswordGeneratorPanel passwordGeneratorPanel;
/*     */   private DragHiddenTextField dragAndDrop;
/*     */   protected boolean usernameEdited;
/*     */   protected boolean emailEdited;
/*     */   protected boolean notesEdited;
/*     */   protected boolean passwordEdited;
/*     */   private final Color editedColor;
/*     */   
/*     */   public AccountWindow(String paramString, MainWindow paramMainWindow, AccountGroup paramAccountGroup, boolean paramBoolean1, boolean paramBoolean2) {
/*  62 */     super(paramMainWindow, paramString);
/*     */     
/*  64 */     this.mainWindow = paramMainWindow;
/*  65 */     this.accountGroup = paramAccountGroup;
/*     */     
/*  67 */     this.editedColor = paramBoolean1 ? Color.YELLOW : Color.WHITE;
/*     */ 
/*     */     
/*  70 */     paramMainWindow.accountWindow = this;
/*     */ 
/*     */     
/*  73 */     JPanel jPanel = new JPanel(new BorderLayout(10, 10));
/*  74 */     jPanel.setBorder(GUIUtils.createMarginBorder(10));
/*  75 */     jPanel.add(createAccountPanel(paramBoolean2), "North");
/*  76 */     jPanel.add((Component)(this.passwordGeneratorPanel = new PasswordGeneratorPanel(8, 20, paramActionEvent -> applyGeneratedPassword())), "Center");
/*     */ 
/*     */     
/*  79 */     jPanel.add(createButtonPanel(), "South");
/*     */ 
/*     */     
/*  82 */     addEditingListeners();
/*     */ 
/*     */     
/*  85 */     add(jPanel);
/*  86 */     pack();
/*  87 */     setDefaultCloseOperation(0);
/*  88 */     GUIUtils.stylizeWindow(this, paramMainWindow, this::onClosing);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JPanel createAccountPanel(boolean paramBoolean) {
/*  97 */     JPanel jPanel = new JPanel();
/*  98 */     MatchedPairsLayout matchedPairsLayout = new MatchedPairsLayout(jPanel);
/*  99 */     jPanel.setLayout((LayoutManager)matchedPairsLayout);
/*     */ 
/*     */     
/* 102 */     this.usernameField = GUIUtils.makeTextField(true, this.accountGroup.username);
/*     */     
/* 104 */     matchedPairsLayout.addMatch(
/* 105 */         GUIUtils.makeLabel("Username", true), this.usernameField);
/*     */ 
/*     */ 
/*     */     
/* 109 */     this.emailField = GUIUtils.makeTextField(true, this.accountGroup.email);
/* 110 */     matchedPairsLayout.addMatch(
/* 111 */         GUIUtils.makeLabel("Email Address", true), this.emailField);
/*     */ 
/*     */ 
/*     */     
/* 115 */     matchedPairsLayout.addMatch(
/* 116 */         GUIUtils.makeLabel("Password", true), 
/* 117 */         (Container)(this.passwordField = new PasswordField(this.accountGroup.getPassword(), 1)));
/*     */ 
/*     */     
/* 120 */     if (this.accountGroup.getLastPassword() != null)
/*     */     {
/* 122 */       matchedPairsLayout.addMatch(
/* 123 */           GUIUtils.makeLabel("", true), 
/* 124 */           GUIUtils.makeStretchPanel(GUIUtils.makeButton("Show Last Password", paramActionEvent -> makeLastPasswordDialog())));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     JLabel jLabel = GUIUtils.makeLabel("Notes", true);
/*     */     
/* 132 */     this.notesField = new JTextArea(4, 15);
/* 133 */     this.notesField.setText(this.accountGroup.notes);
/* 134 */     this.notesField.setFont(GUIUtils.fontSmall);
/* 135 */     this.notesField.setLineWrap(true);
/*     */     
/* 137 */     JScrollPane jScrollPane = new JScrollPane(this.notesField);
/* 138 */     jScrollPane.setBorder(this.usernameField.getBorder());
/*     */     
/* 140 */     matchedPairsLayout.addMatch(jLabel, jScrollPane);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     this.dragAndDrop = new DragHiddenTextField(this.accountGroup.getPassword());
/* 146 */     if (paramBoolean) {
/* 147 */       matchedPairsLayout.addMatch(
/* 148 */           GUIUtils.makeLabel("Password Drag-and-Drop", true), (Container)this.dragAndDrop);
/*     */     }
/*     */ 
/*     */     
/* 152 */     matchedPairsLayout.setAutoCreateGaps(true);
/* 153 */     matchedPairsLayout.finalizeLayout();
/*     */     
/* 155 */     return jPanel;
/*     */   }
/*     */   
/*     */   private void applyGeneratedPassword() {
/* 159 */     this.passwordField.setPassword(this.passwordGeneratorPanel.getPassword());
/* 160 */     this.passwordField.setPasswordIsVisible(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void makeLastPasswordDialog() {
/* 168 */     if (this.lastPasswordWindow != null)
/* 169 */       return;  this.lastPasswordWindow = GUIUtils.makeNewDialog("Last Password", this, () -> {
/*     */           this.lastPasswordWindow.dispose();
/*     */           
/*     */           this.lastPasswordWindow = null;
/*     */         });
/* 174 */     JPanel jPanel = new JPanel();
/* 175 */     MatchedPairsLayout matchedPairsLayout = new MatchedPairsLayout(jPanel);
/* 176 */     jPanel.setLayout((LayoutManager)matchedPairsLayout);
/*     */ 
/*     */     
/* 179 */     PasswordField passwordField = new PasswordField(this.accountGroup.getLastPassword(), 30);
/* 180 */     passwordField.setEditable(false);
/* 181 */     matchedPairsLayout.addMatch(
/* 182 */         GUIUtils.makeLabel("Last Password", true), (Container)passwordField);
/*     */ 
/*     */ 
/*     */     
/* 186 */     matchedPairsLayout.addMatch(
/* 187 */         GUIUtils.makeLabel("Last Change", true), 
/* 188 */         GUIUtils.makeLabel((new SimpleDateFormat("MMM d, yyyy (h:mm a)")).format(this.accountGroup.getPasswordLastChangedDate()), true));
/*     */     
/* 190 */     matchedPairsLayout.setAutoCreateGaps(true);
/* 191 */     matchedPairsLayout.finalizeLayout();
/*     */     
/* 193 */     jPanel.setBorder(GUIUtils.createMarginBorder(10));
/*     */     
/* 195 */     this.lastPasswordWindow.add(jPanel);
/* 196 */     this.lastPasswordWindow.pack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEditingListeners() {
/* 205 */     setDocListener(this.usernameField.getDocument(), this::checkUsernameEdited);
/* 206 */     setDocListener(this.emailField.getDocument(), this::checkEmailEdited);
/* 207 */     setDocListener(this.notesField.getDocument(), this::checkNotesEdited);
/* 208 */     setDocListener(this.passwordField.getDocument(), this::checkPasswordEdited);
/*     */   }
/*     */   
/*     */   private void setDocListener(Document paramDocument, final EditListener listener) {
/* 212 */     paramDocument.addDocumentListener(new DocumentListener()
/*     */         {
/*     */           public void insertUpdate(DocumentEvent param1DocumentEvent) {
/* 215 */             listener.onFieldEdit();
/*     */           }
/*     */           
/*     */           public void removeUpdate(DocumentEvent param1DocumentEvent) {
/* 219 */             listener.onFieldEdit();
/*     */           }
/*     */ 
/*     */           
/*     */           public void changedUpdate(DocumentEvent param1DocumentEvent) {}
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface EditListener
/*     */   {
/*     */     void onFieldEdit();
/*     */   }
/*     */   
/*     */   protected final void checkUsernameEdited() {
/* 235 */     this.usernameEdited = !this.usernameField.getText().equals(this.accountGroup.username);
/* 236 */     if (this.usernameEdited) {
/* 237 */       this.usernameField.setBackground(this.editedColor);
/*     */     } else {
/* 239 */       this.usernameField.setBackground(Color.WHITE);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void checkEmailEdited() {
/* 244 */     this.emailEdited = !this.emailField.getText().equals(this.accountGroup.email);
/* 245 */     if (this.emailEdited) {
/* 246 */       this.emailField.setBackground(this.editedColor);
/*     */     } else {
/* 248 */       this.emailField.setBackground(Color.WHITE);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void checkNotesEdited() {
/* 253 */     this.notesEdited = !this.notesField.getText().equals(this.accountGroup.notes);
/* 254 */     if (this.notesEdited) {
/* 255 */       this.notesField.setBackground(this.editedColor);
/*     */     } else {
/* 257 */       this.notesField.setBackground(Color.WHITE);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void checkPasswordEdited() {
/* 262 */     this.passwordEdited = !this.passwordField.getPassword().equals(this.accountGroup.getPassword());
/* 263 */     if (this.passwordEdited) {
/* 264 */       this.passwordField.setFieldBackground(this.editedColor);
/*     */     } else {
/* 266 */       this.passwordField.setFieldBackground(Color.WHITE);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final boolean anyFieldEdited() {
/* 271 */     return (this.usernameEdited || this.emailEdited || this.notesEdited || this.passwordEdited);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void closeWindow() {
/* 277 */     dispose();
/* 278 */     this.mainWindow.accountWindow = null;
/*     */   }
/*     */   
/*     */   protected final void vaultEdited() {
/* 282 */     this.mainWindow.vaultEdited();
/* 283 */     this.dragAndDrop.setText(this.accountGroup.getPassword());
/*     */   }
/*     */   
/*     */   protected static final boolean validateUsernameAndPassword(String paramString1, String paramString2) {
/* 287 */     boolean bool1 = paramString1.isBlank(), bool2 = paramString2.isEmpty();
/* 288 */     if (bool1 && bool2) {
/* 289 */       GUIUtils.showWarning("Username and password fields cannot be blank.");
/* 290 */       return false;
/* 291 */     }  if (bool1) {
/* 292 */       GUIUtils.showWarning("Username field cannot be blank.");
/* 293 */       return false;
/* 294 */     }  if (bool2) {
/* 295 */       GUIUtils.showWarning("Password field cannot be blank.");
/* 296 */       return false;
/* 297 */     }  return true;
/*     */   }
/*     */   
/*     */   protected abstract JPanel createButtonPanel();
/*     */   
/*     */   protected abstract void onClosing();
/*     */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\frames\AccountWindow.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */