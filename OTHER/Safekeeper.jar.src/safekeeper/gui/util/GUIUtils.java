/*     */ package safekeeper.gui.util;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
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
/*     */ public class GUIUtils
/*     */ {
/*  53 */   public static final Font font = new Font("Dialog", 0, 13);
/*  54 */   public static final Font fontSmall = new Font("Dialog", 0, 11);
/*  55 */   public static final Font fontPassword = new Font("Monospaced", 0, 11);
/*  56 */   public static final Font fontButton = fontPassword;
/*  57 */   public static final Font fontPrinter = new Font("Monospaced", 0, 14);
/*     */   
/*  59 */   public static final ImageIcon lockIcon = new ImageIcon("safekeeper/resources/lock-icon.png");
/*     */   
/*     */   public static final int MARGIN = 10;
/*     */   
/*     */   public static final int CLOSED_OPTION = -1;
/*     */   
/*     */   public static void playErrorSound() {
/*  66 */     Runnable runnable = (Runnable)Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
/*  67 */     if (runnable != null) runnable.run();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setWindowsStyle() {
/*     */     try {
/*  75 */       UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
/*  76 */     } catch (Exception exception) {}
/*     */   }
/*     */   
/*     */   public static void stylizeWindow(Window paramWindow1, Window paramWindow2) {
/*  80 */     if (paramWindow1 instanceof Frame) ((Frame)paramWindow1).setResizable(false); 
/*  81 */     if (paramWindow1 instanceof Dialog) ((Dialog)paramWindow1).setResizable(false); 
/*  82 */     paramWindow1.setIconImage(lockIcon.getImage());
/*  83 */     paramWindow1.setLocationRelativeTo(paramWindow2);
/*  84 */     paramWindow1.setVisible(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void stylizeWindow(Window paramWindow1, Window paramWindow2, final WindowClosingListener closingListener) {
/*  93 */     stylizeWindow(paramWindow1, paramWindow2);
/*  94 */     paramWindow1.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent param1WindowEvent) {
/*  97 */             closingListener.onWindowClosing();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static JDialog makeNewDialog(String paramString, Window paramWindow, WindowClosingListener paramWindowClosingListener) {
/* 103 */     JDialog jDialog = new JDialog(paramWindow, paramString);
/* 104 */     jDialog.setDefaultCloseOperation(0);
/* 105 */     stylizeWindow(jDialog, paramWindow, paramWindowClosingListener);
/* 106 */     return jDialog;
/*     */   } @FunctionalInterface
/*     */   public static interface WindowClosingListener {
/*     */     void onWindowClosing(); } public static void setSize(JComponent paramJComponent, int paramInt1, int paramInt2) {
/* 110 */     Dimension dimension = new Dimension(paramInt1, paramInt2);
/* 111 */     paramJComponent.setPreferredSize(dimension);
/* 112 */     paramJComponent.setMinimumSize(dimension);
/* 113 */     paramJComponent.setMaximumSize(dimension);
/*     */   }
/*     */   
/*     */   public static Border createMarginBorder(int paramInt) {
/* 117 */     return BorderFactory.createEmptyBorder(paramInt, paramInt, paramInt, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JLabel makeLabel(String paramString, boolean paramBoolean) {
/* 124 */     if (paramString.contains("\n")) paramString = "<html>" + paramString.replaceAll("\n", "<br />") + "</html>"; 
/* 125 */     JLabel jLabel = new JLabel(paramString);
/* 126 */     jLabel.setFont(paramBoolean ? fontSmall : font);
/* 127 */     return jLabel;
/*     */   }
/*     */   
/*     */   public static JButton makeButton(String paramString, ActionListener paramActionListener) {
/* 131 */     JButton jButton = new JButton(paramString);
/* 132 */     jButton.setFont(fontButton);
/* 133 */     jButton.addActionListener(paramActionListener);
/* 134 */     jButton.setFocusPainted(false);
/* 135 */     return jButton;
/*     */   }
/*     */   
/*     */   public static JTextField makeTextField(boolean paramBoolean, String paramString) {
/* 139 */     JTextField jTextField = new JTextField(paramString, 1);
/* 140 */     jTextField.setFont(paramBoolean ? fontSmall : font);
/* 141 */     return jTextField;
/*     */   }
/*     */   
/*     */   public static JTextField makeTextField(boolean paramBoolean) {
/* 145 */     return makeTextField(paramBoolean, "");
/*     */   }
/*     */   
/*     */   public static Component makeVerticalStrut(int paramInt) {
/* 149 */     return Box.createVerticalStrut(paramInt);
/*     */   }
/*     */   
/*     */   public static Component makeHorizontalStrut(int paramInt) {
/* 153 */     return Box.createHorizontalStrut(paramInt);
/*     */   }
/*     */   
/*     */   public static JPanel makeStretchPanel(Component paramComponent) {
/* 157 */     JPanel jPanel = new JPanel(new BorderLayout());
/* 158 */     jPanel.add(paramComponent, "Center");
/* 159 */     return jPanel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showOptionChooser(Window paramWindow, String paramString1, String paramString2, String[] paramArrayOfString, int paramInt) {
/* 165 */     return JOptionPane.showOptionDialog(paramWindow, paramString1, paramString2, -1, -1, null, (Object[])paramArrayOfString, paramArrayOfString[paramInt]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showMessage(Window paramWindow, String paramString1, String paramString2) {
/* 177 */     JOptionPane.showMessageDialog(paramWindow, paramString1, paramString2, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showFatalError(String paramString) {
/* 185 */     playErrorSound();
/* 186 */     showMessage(null, "A fatal error has occurred:\n" + paramString, "Error");
/*     */ 
/*     */ 
/*     */     
/* 190 */     System.exit(1);
/*     */   }
/*     */   
/*     */   public static void showFatalError(Exception paramException) {
/* 194 */     showFatalError(paramException.toString());
/*     */   }
/*     */   
/*     */   public static void showWarning(String paramString) {
/* 198 */     playErrorSound();
/* 199 */     showMessage(null, paramString, "Warning");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showWarning(Exception paramException) {
/* 206 */     showWarning(paramException.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Component addMargin(Component paramComponent, int paramInt) {
/* 213 */     JPanel jPanel = new JPanel();
/* 214 */     jPanel.add(paramComponent);
/* 215 */     jPanel.setBorder(createMarginBorder(paramInt));
/* 216 */     return jPanel;
/*     */   }
/*     */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gu\\util\GUIUtils.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */