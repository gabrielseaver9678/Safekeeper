package safekeeper.gui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.Document;
import safekeeper.gui.util.GUIUtils;

public class PasswordField extends JPanel {
  private static final String SHOW = "Show";
  
  private static final String HIDE = "Hide";
  
  private JPasswordField passwordField;
  
  private JButton showButton;
  
  private boolean isPasswordVisible;
  
  public PasswordField(String paramString, int paramInt) {
    BoxLayout boxLayout = new BoxLayout(this, 0);
    setLayout(boxLayout);
    setAlignmentY(0.5F);
    this.showButton = GUIUtils.makeButton("Show", paramActionEvent -> setPasswordIsVisible(!this.isPasswordVisible));
    this.passwordField = new JPasswordField(paramString, paramInt);
    setPasswordIsVisible(false);
    this.passwordField.setFont(GUIUtils.fontPassword);
    add(this.passwordField);
    add(this.showButton);
  }
  
  public PasswordField(int paramInt) {
    this("", paramInt);
  }
  
  public void setPasswordIsVisible(boolean paramBoolean) {
    this.isPasswordVisible = paramBoolean;
    this.showButton.setText(this.isPasswordVisible ? "Hide" : "Show");
    this.passwordField.setEchoChar(this.isPasswordVisible ? Character.MIN_VALUE : 42);
  }
  
  public void addPasswordSubmittedListener(ActionListener paramActionListener) {
    this.passwordField.addActionListener(paramActionListener);
  }
  
  public String getPassword() {
    return new String(this.passwordField.getPassword());
  }
  
  public void setPassword(String paramString) {
    this.passwordField.setText(paramString);
  }
  
  public void clearPassword() {
    this.passwordField.setText("");
  }
  
  public void setEditable(boolean paramBoolean) {
    this.passwordField.setEditable(paramBoolean);
  }
  
  public Document getDocument() {
    return this.passwordField.getDocument();
  }
  
  public void setFieldBackground(Color paramColor) {
    this.passwordField.setBackground(paramColor);
  }
}
