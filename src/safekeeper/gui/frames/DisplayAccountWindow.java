
// DisplayAccountWindow.java, Gabriel Seaver, 2021

package safekeeper.gui.frames;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import safekeeper.groupings.AccountGroup;
import safekeeper.gui.util.GUIUtils;

public class DisplayAccountWindow extends AccountWindow {
	
	public DisplayAccountWindow (MainWindow mainWindow, AccountGroup account) {
		super("", mainWindow, account, true, true);
		updateTitle();
	}
	
	private void updateTitle () {
		setTitle("Account: " + accountGroup.username + " (" + accountGroup.service.name + ")");
	}
	
	@Override
	protected JPanel createButtonPanel () {
		// Create a new panel with only the "save" and "delete" buttons
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(GUIUtils.makeButton("Save Changes", e -> saveAccountChanges()), BorderLayout.NORTH);
		panel.add(GUIUtils.makeButton("Delete Account", e -> deleteAccount()), BorderLayout.SOUTH);
		return panel;
	}
	
	private void saveAccountChanges () {
		// If no fields were edited, display a warning and do nothing else
		if (!anyFieldEdited()) {
			GUIUtils.showWarning("There are no changes to save.");
			return;
		}
		
		// If the username and/or password are not valid, display a warning and do nothing else
		if (!validateUsernameAndPassword(usernameField.getText(), passwordField.getPassword())) return;
		
		// Make a string describing which fields have been edited
		String editedString = 
			(usernameEdited	? "\n- Username" : "") +
			(emailEdited	? "\n- Email" : "") +
			(passwordEdited	? "\n- Password" : "") +
			(notesEdited	? "\n- Notes" : "");
		
		// Show the "save changes" chooser
		int index = GUIUtils.showOptionChooser(
			this,
			"Are you sure you want to save the changes made to your:" + editedString,
			"Save Changes?",
			new String[] { "Save", "Do Not Save" },
			0);
		
		// If "Save" was not chosen, finish
		if (index != 0) return;
		
		// Otherwise, update values
		accountGroup.username = usernameField.getText();
		if (passwordEdited) // .setPassword has side effects, only use when necessary
			accountGroup.setPassword(passwordField.getPassword());
		accountGroup.email = emailField.getText();
		accountGroup.notes = notesField.getText();
		
		// Update the title and update the vault edited status
		updateTitle();
		vaultEdited();
	}
	
	@Override
	protected void onClosing () {
		if (anyFieldEdited()) {
			// Show "close window" chooser
			int index = GUIUtils.showOptionChooser(
				this,
				"If you close this window now, changes you have\n" +
				"made to the account will not be saved.",
				"Close Without Saving Changes?",
				new String[] { "Close Anyways", "Do Not Close" },
				0);
			
			// If "Close Anyways" was not selected, do nothing
			if (index != 0) return;
		} closeWindow(); // Either "Close Anyways" was selected, or no fields were edited
	}
	
	private void deleteAccount () {
		// Show "delete account" chooser
		int index = GUIUtils.showOptionChooser(
			this,
			"Are you sure you want to delete this account from your vault?",
			"Delete Account?",
			new String[] { "Delete Permanently", "Do Not Delete" },
			1);
		
		// If "Delete Permanently" was not selected, do nothing
		if (index != 0) return;
		
		// Otherwise, remove the account group from the service group
		accountGroup.service.accountGroups.remove(accountGroup);
		
		// Close the window and update the vault edited status
		vaultEdited();
		closeWindow();
	}
	
}