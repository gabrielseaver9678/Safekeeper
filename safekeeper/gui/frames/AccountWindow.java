package safekeeper.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import safekeeper.groupings.AccountGroup;
import safekeeper.gui.components.DragHiddenTextField;
import safekeeper.gui.components.PasswordField;
import safekeeper.gui.components.PasswordGeneratorPanel;
import safekeeper.gui.layout.MatchedPairsLayout;
import safekeeper.gui.util.GUIUtils;

public abstract class AccountWindow extends JDialog {
  protected final AccountGroup accountGroup;
  
  private final MainWindow mainWindow;
  
  protected JDialog lastPasswordWindow;
  
  protected JTextField usernameField;
  
  protected JTextField emailField;
  
  protected JTextArea notesField;
  
  protected PasswordField passwordField;
  
  private PasswordGeneratorPanel passwordGeneratorPanel;
  
  private DragHiddenTextField dragAndDrop;
  
  protected boolean usernameEdited;
  
  protected boolean emailEdited;
  
  protected boolean notesEdited;
  
  protected boolean passwordEdited;
  
  private final Color editedColor;
  
  public AccountWindow(String paramString, MainWindow paramMainWindow, AccountGroup paramAccountGroup, boolean paramBoolean1, boolean paramBoolean2) {
    super(paramMainWindow, paramString);
	
    this.mainWindow = paramMainWindow;
    this.accountGroup = paramAccountGroup;
    this.editedColor = paramBoolean1 ? Color.YELLOW : Color.WHITE;
    paramMainWindow.accountWindow = this;
	
    JPanel jPanel = new JPanel(new BorderLayout(10, 10));
    jPanel.setBorder(GUIUtils.createMarginBorder(10));
	
    jPanel.add(createAccountPanel(paramBoolean2), BorderLayout.NORTH);
    jPanel.add(this.passwordGeneratorPanel = new PasswordGeneratorPanel(8, 20, paramActionEvent -> applyGeneratedPassword()), BorderLayout.CENTER);
    jPanel.add(createButtonPanel(), BorderLayout.SOUTH);
	
    addEditingListeners();
    add(jPanel);
    pack();
    setDefaultCloseOperation(0);
    GUIUtils.stylizeWindow(this, paramMainWindow, this::onClosing);
  }
  
  private JPanel createAccountPanel(boolean paramBoolean) {
    JPanel jPanel = new JPanel();
    MatchedPairsLayout matchedPairsLayout = new MatchedPairsLayout(jPanel);
    jPanel.setLayout((LayoutManager)matchedPairsLayout);
    this.usernameField = GUIUtils.makeTextField(true, this.accountGroup.username);
    matchedPairsLayout.addMatch(
        GUIUtils.makeLabel("Username", true), this.usernameField);
    this.emailField = GUIUtils.makeTextField(true, this.accountGroup.email);
    matchedPairsLayout.addMatch(
        GUIUtils.makeLabel("Email Address", true), this.emailField);
    matchedPairsLayout.addMatch(
        GUIUtils.makeLabel("Password", true), 
        (Container)(this.passwordField = new PasswordField(this.accountGroup.getPassword(), 1)));
    if (this.accountGroup.getLastPassword() != null)
      matchedPairsLayout.addMatch(
          GUIUtils.makeLabel("", true), 
          GUIUtils.makeStretchPanel(GUIUtils.makeButton("Show Last Password", paramActionEvent -> makeLastPasswordDialog()))); 
    JLabel jLabel = GUIUtils.makeLabel("Notes", true);
    this.notesField = new JTextArea(4, 15);
    this.notesField.setText(this.accountGroup.notes);
    this.notesField.setFont(GUIUtils.fontSmall);
    this.notesField.setLineWrap(true);
    JScrollPane jScrollPane = new JScrollPane(this.notesField);
    jScrollPane.setBorder(this.usernameField.getBorder());
    matchedPairsLayout.addMatch(jLabel, jScrollPane);
    this.dragAndDrop = new DragHiddenTextField(this.accountGroup.getPassword());
    if (paramBoolean)
      matchedPairsLayout.addMatch(
          GUIUtils.makeLabel("Password Drag-and-Drop", true), (Container)this.dragAndDrop); 
    matchedPairsLayout.setAutoCreateGaps(true);
    matchedPairsLayout.finalizeLayout();
    return jPanel;
  }
  
  private void applyGeneratedPassword() {
    this.passwordField.setPassword(this.passwordGeneratorPanel.getPassword());
    this.passwordField.setPasswordIsVisible(true);
  }
  
  private void makeLastPasswordDialog() {
    if (this.lastPasswordWindow != null)
      return; 
    this.lastPasswordWindow = GUIUtils.makeNewDialog("Last Password", this, () -> {
          this.lastPasswordWindow.dispose();
          this.lastPasswordWindow = null;
        });
    JPanel jPanel = new JPanel();
    MatchedPairsLayout matchedPairsLayout = new MatchedPairsLayout(jPanel);
    jPanel.setLayout((LayoutManager)matchedPairsLayout);
    PasswordField passwordField = new PasswordField(this.accountGroup.getLastPassword(), 30);
    passwordField.setEditable(false);
    matchedPairsLayout.addMatch(
        GUIUtils.makeLabel("Last Password", true), (Container)passwordField);
    matchedPairsLayout.addMatch(
        GUIUtils.makeLabel("Last Change", true), 
        GUIUtils.makeLabel((new SimpleDateFormat("MMM d, yyyy (h:mm a)")).format(this.accountGroup.getPasswordLastChangedDate()), true));
    matchedPairsLayout.setAutoCreateGaps(true);
    matchedPairsLayout.finalizeLayout();
    jPanel.setBorder(GUIUtils.createMarginBorder(10));
    this.lastPasswordWindow.add(jPanel);
    this.lastPasswordWindow.pack();
  }
  
  private void addEditingListeners() {
    setDocListener(this.usernameField.getDocument(), this::checkUsernameEdited);
    setDocListener(this.emailField.getDocument(), this::checkEmailEdited);
    setDocListener(this.notesField.getDocument(), this::checkNotesEdited);
    setDocListener(this.passwordField.getDocument(), this::checkPasswordEdited);
  }
  
  private void setDocListener(Document paramDocument, final EditListener listener) {
    paramDocument.addDocumentListener(new DocumentListener() {
          public void insertUpdate(DocumentEvent param1DocumentEvent) {
            listener.onFieldEdit();
          }
          
          public void removeUpdate(DocumentEvent param1DocumentEvent) {
            listener.onFieldEdit();
          }
          
          public void changedUpdate(DocumentEvent param1DocumentEvent) {}
        });
  }
  
  @FunctionalInterface
  private static interface EditListener {
    void onFieldEdit();
  }
  
  protected final void checkUsernameEdited() {
    this.usernameEdited = !this.usernameField.getText().equals(this.accountGroup.username);
    if (this.usernameEdited) {
      this.usernameField.setBackground(this.editedColor);
    } else {
      this.usernameField.setBackground(Color.WHITE);
    } 
  }
  
  protected final void checkEmailEdited() {
    this.emailEdited = !this.emailField.getText().equals(this.accountGroup.email);
    if (this.emailEdited) {
      this.emailField.setBackground(this.editedColor);
    } else {
      this.emailField.setBackground(Color.WHITE);
    } 
  }
  
  protected final void checkNotesEdited() {
    this.notesEdited = !this.notesField.getText().equals(this.accountGroup.notes);
    if (this.notesEdited) {
      this.notesField.setBackground(this.editedColor);
    } else {
      this.notesField.setBackground(Color.WHITE);
    } 
  }
  
  protected final void checkPasswordEdited() {
    this.passwordEdited = !this.passwordField.getPassword().equals(this.accountGroup.getPassword());
    if (this.passwordEdited) {
      this.passwordField.setFieldBackground(this.editedColor);
    } else {
      this.passwordField.setFieldBackground(Color.WHITE);
    } 
  }
  
  protected final boolean anyFieldEdited() {
    return (this.usernameEdited || this.emailEdited || this.notesEdited || this.passwordEdited);
  }
  
  protected final void closeWindow() {
    dispose();
    this.mainWindow.accountWindow = null;
  }
  
  protected final void vaultEdited() {
    this.mainWindow.vaultEdited();
    this.dragAndDrop.setText(this.accountGroup.getPassword());
  }
  
  protected static final boolean validateUsernameAndPassword(String paramString1, String paramString2) {
    boolean bool1 = paramString1.isBlank(), bool2 = paramString2.isEmpty();
    if (bool1 && bool2) {
      GUIUtils.showWarning("Username and password fields cannot be blank.");
      return false;
    } 
    if (bool1) {
      GUIUtils.showWarning("Username field cannot be blank.");
      return false;
    } 
    if (bool2) {
      GUIUtils.showWarning("Password field cannot be blank.");
      return false;
    } 
    return true;
  }
  
  protected abstract JPanel createButtonPanel();
  
  protected abstract void onClosing();
}
