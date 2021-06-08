package safekeeper.gui.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import safekeeper.groupings.AccountGroup;
import safekeeper.groupings.ServiceGroup;
import safekeeper.gui.util.GUIUtils;

public class CreateAccountWindow extends AccountWindow {
  public static CreateAccountWindow newWindow(MainWindow paramMainWindow, ServiceGroup paramServiceGroup) {
    AccountGroup accountGroup = new AccountGroup(paramServiceGroup);
    return new CreateAccountWindow("Create New Account (" + paramServiceGroup.name + ")", paramMainWindow, accountGroup);
  }
  
  private CreateAccountWindow(String paramString, MainWindow paramMainWindow, AccountGroup paramAccountGroup) {
    super(paramString, paramMainWindow, paramAccountGroup, false, false);
  }
  
  protected JPanel createButtonPanel() {
    JPanel jPanel = new JPanel(new BorderLayout());
    jPanel.add(GUIUtils.makeButton("Save New Account", paramActionEvent -> saveNewAccount()));
    return jPanel;
  }
  
  private void saveNewAccount() {
    if (validateUsernameAndPassword(this.usernameField.getText(), this.passwordField.getPassword())) {
      this.accountGroup.username = this.usernameField.getText();
      this.accountGroup.email = this.emailField.getText();
      this.accountGroup.setPassword(this.passwordField.getPassword());
      this.accountGroup.notes = this.notesField.getText();
      this.accountGroup.service.accountGroups.add(this.accountGroup);
      closeWindow();
      vaultEdited();
    } 
  }
  
  protected void onClosing() {
    int i = GUIUtils.showOptionChooser(this, "If you close this window now, the new\naccount will not be saved.", "Close Without Saving Account?", new String[] { "Close Anyways", "Do Not Close" }, 0);
    if (i != 0)
      return; 
    closeWindow();
  }
}
