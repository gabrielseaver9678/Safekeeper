
// CreateAccountWindow.java, Gabriel Seaver, 2021

package safekeeper.gui.frames;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import safekeeper.groupings.AccountGroup;
import safekeeper.groupings.CategoryGroup;
import safekeeper.gui.util.GUIUtils;

public class CreateAccountWindow extends AccountWindow {
	
	public CreateAccountWindow (MainWindow mainWindow, CategoryGroup category) {
		super("Create New Account (" + category.name + ")", mainWindow, new AccountGroup(category), false, false);
	}
	
	@Override
	protected JPanel createButtonPanel () {
		// Button panel only contains "Save New Account" button
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(GUIUtils.makeButton("Save New Account", e -> saveNewAccount()));
		return panel;
	}
	
	private void saveNewAccount () {
		// Make sure username and password are valid, showing a warning dialog if they are not
		if (validateFields()) {
			// If they are valid, set the account group values
			saveAllFieldDataToAccount();
			
			// Add the account to the category
			accountGroup.category.accountGroups.add(accountGroup);
			
			// Close the window and indicate that the vault has been edited
			vaultEdited();
			closeWindow();
		}
	}
	
	@Override
	protected void onClosing () {
		// Show "close without saving" chooser
		int index = GUIUtils.showOptionChooser(
			this,
			"If you close this window now, the new\n" +
			"account will not be saved.",
			"Close Without Saving Account?",
			new String[] { "Close Anyways", "Do Not Close" },
			0);
		
		// Do nothing if "Close Anyways" wasn't chosen
		if (index != 0) return;
		
		// Otherwise, close the window
		closeWindow();
	}
	
}