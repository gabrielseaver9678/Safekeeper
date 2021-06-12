/*     */ package safekeeper.gui.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SpinnerNumberModel;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import safekeeper.crypto.Crypto;
/*     */ import safekeeper.gui.layout.RowsLayout;
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
/*     */ public class PasswordGeneratorPanel
/*     */   extends JPanel
/*     */ {
/*     */   private final JCheckBox lowercase;
/*     */   private final JCheckBox uppercase;
/*     */   private final JCheckBox numbers;
/*     */   private final JCheckBox symbols;
/*     */   private final JTextField passwordField;
/*     */   private final JSpinner lengthField;
/*     */   private final JButton generateButton;
/*     */   private final JLabel securityLabel;
/*     */   
/*     */   public PasswordGeneratorPanel(int paramInt1, int paramInt2, ActionListener paramActionListener) {
/*  50 */     paramInt1 = Math.max(paramInt1, 4);
/*  51 */     paramInt2 = Math.min(paramInt2, 24);
/*     */     
/*  53 */     setBorder(BorderFactory.createTitledBorder("Password Generator"));
/*  54 */     RowsLayout rowsLayout = new RowsLayout(this, 10, 10);
/*  55 */     setLayout((LayoutManager)rowsLayout);
/*     */     
/*  57 */     this.lowercase = makeCheckBox("Lowercase");
/*  58 */     this.uppercase = makeCheckBox("Uppercase");
/*  59 */     this.numbers = makeCheckBox("Numbers");
/*  60 */     this.symbols = makeCheckBox("Symbols");
/*     */     
/*  62 */     this.passwordField = new JTextField(30);
/*  63 */     this.passwordField.setFont(GUIUtils.fontPassword);
/*  64 */     this.passwordField.setEditable(false);
/*     */     
/*  66 */     this.generateButton = GUIUtils.makeButton("Generate Password", paramActionEvent -> generatePassword());
/*     */     
/*  68 */     this.lengthField = new JSpinner(new SpinnerNumberModel(paramInt2, paramInt1, paramInt2, 1));
/*  69 */     this.lengthField.addChangeListener(paramChangeEvent -> generatePassword());
/*     */     
/*  71 */     this.securityLabel = GUIUtils.makeLabel("", false);
/*     */     
/*  73 */     rowsLayout.addRow(new JComponent[] { this.lowercase, this.uppercase, this.numbers, this.symbols });
/*  74 */     rowsLayout.addRow(new JComponent[] { this.lengthField, this.generateButton });
/*  75 */     rowsLayout.addRow(new JComponent[] { this.passwordField, GUIUtils.makeButton("Apply", paramActionListener) });
/*  76 */     rowsLayout.addRow(new JComponent[] { this.securityLabel });
/*     */     
/*  78 */     generatePassword();
/*     */   }
/*     */ 
/*     */   
/*     */   private JCheckBox makeCheckBox(String paramString) {
/*  83 */     JCheckBox jCheckBox = new JCheckBox(paramString, true);
/*  84 */     jCheckBox.setFont(GUIUtils.font);
/*  85 */     jCheckBox.setFocusPainted(false);
/*  86 */     jCheckBox.addActionListener(paramActionEvent -> generatePassword());
/*  87 */     return jCheckBox;
/*     */   }
/*     */   
/*     */   private Crypto.SecurityLevel getPasswordSecurityLevel() {
/*  91 */     return Crypto.getSecurityLevel(this.lowercase
/*  92 */         .isSelected(), this.uppercase
/*  93 */         .isSelected(), this.numbers
/*  94 */         .isSelected(), this.symbols
/*  95 */         .isSelected(), ((Integer)this.lengthField
/*  96 */         .getValue()).intValue());
/*     */   }
/*     */   
/*     */   private String getNewPassword() {
/* 100 */     return Crypto.generatePasswordUseAll(this.lowercase
/* 101 */         .isSelected(), this.uppercase
/* 102 */         .isSelected(), this.numbers
/* 103 */         .isSelected(), this.symbols
/* 104 */         .isSelected(), ((Integer)this.lengthField
/* 105 */         .getValue()).intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public String generatePassword() {
/* 110 */     String str = getNewPassword();
/* 111 */     if (str.equals("")) {
/* 112 */       GUIUtils.showWarning("Invalid password generation settings.");
/* 113 */       return null;
/*     */     } 
/*     */     
/* 116 */     this.passwordField.setText(str);
/*     */ 
/*     */     
/* 119 */     switch (getPasswordSecurityLevel()) {
/*     */       case EXTREMELY_UNSECURE:
/* 121 */         this.securityLabel.setForeground(new Color(180, 0, 0));
/* 122 */         this.securityLabel.setText("Warning: Extremely unsecure password");
/*     */         break;
/*     */       case UNSECURE:
/* 125 */         this.securityLabel.setForeground(new Color(180, 0, 0));
/* 126 */         this.securityLabel.setText("Warning: Unsecure password");
/*     */         break;
/*     */       case SECURE:
/* 129 */         this.securityLabel.setForeground(Color.BLACK);
/* 130 */         this.securityLabel.setText("Generated password is secure");
/*     */         break;
/*     */       case VERY_SECURE:
/* 133 */         this.securityLabel.setForeground(Color.BLACK);
/* 134 */         this.securityLabel.setText("Generated password is very secure"); break;
/*     */     } 
/* 136 */     return str;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 140 */     return this.passwordField.getText();
/*     */   }
/*     */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\components\PasswordGeneratorPanel.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */