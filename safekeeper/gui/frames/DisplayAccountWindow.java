package safekeeper.gui.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import safekeeper.groupings.AccountGroup;
import safekeeper.gui.util.GUIUtils;

public class DisplayAccountWindow extends AccountWindow {
	public DisplayAccountWindow(MainWindow paramMainWindow, AccountGroup paramAccountGroup) {
		super("", paramMainWindow, paramAccountGroup, true, true);
		updateTitle();
	}
	
	private void updateTitle() {
		setTitle("Account: " + this.accountGroup.username + " (" + this.accountGroup.service.name + ")");
	}
	
	protected JPanel createButtonPanel() {
		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(GUIUtils.makeButton("Save Changes", paramActionEvent -> saveAccountChanges()), "North");
		jPanel.add(GUIUtils.makeButton("Delete Account", paramActionEvent -> deleteAccount()), "South");
		return jPanel;
	}
	
	private void saveAccountChanges() {
		if (!anyFieldEdited()) {
			GUIUtils.showWarning("There are no changes to save.");
			return;
		}
		if (!validateUsernameAndPassword(this.usernameField.getText(), this.passwordField.getPassword()))
			return;
		String str = "";
		str = str + str;
		str = str + str;
		str = str + str;
		str = str + str;
		int i = GUIUtils.showOptionChooser(this, "Are you sure you want to save the changes made to your:" + str, "Save Changes?", new String[] { "Save", "Do Not Save" }, 0);
		if (i != 0)
			return;
		this.accountGroup.username = this.usernameField.getText();
		if (this.passwordEdited)
			this.accountGroup.setPassword(this.passwordField.getPassword());
		this.accountGroup.email = this.emailField.getText();
		this.accountGroup.notes = this.notesField.getText();
		checkUsernameEdited();
		checkEmailEdited();
		checkNotesEdited();
		checkPasswordEdited();
		updateTitle();
		vaultEdited();
	}
	
	protected void onClosing() {
		if (anyFieldEdited()) {
			int i = GUIUtils.showOptionChooser(this, "If you close this window now, changes you have\nmade to the account will not be saved.", "Close Without Saving Changes?", new String[] { "Close Anyways", "Do Not Close" }, 0);
			if (i != 0)
				return;
		}
		closeWindow();
	}
	
	private void deleteAccount() {
		int i = GUIUtils.showOptionChooser(this, "Are you sure you want to delete this account from your vault?", "Delete Account?", new String[] { "Delete Permanently", "Do Not Delete" }, 1);
		if (i != 0)
			return;
		this.accountGroup.service.accountGroups.remove(this.accountGroup);
		closeWindow();
		vaultEdited();
	}
}
