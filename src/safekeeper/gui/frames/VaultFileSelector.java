
// VaultFileSelector.java, Gabriel Seaver, 2021

package safekeeper.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.concurrent.CountDownLatch;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import safekeeper.gui.components.PasswordField;
import safekeeper.gui.components.PasswordGeneratorPanel;
import safekeeper.gui.layout.MatchedPairsLayout;
import safekeeper.gui.util.GUIUtils;

public class VaultFileSelector {
	
	public static final String EXTENSION = "skvault";
	
	public static enum LoadOption {
		CREATE_NEW, LOAD_EXISTING, NONE;
	}
	
	public static LoadOption chooseCreateNewOrLoadExisting (JFrame parentFrame) {
		// Show a vault loading method option chooser
		int index = GUIUtils.showOptionChooser(
			parentFrame,
			"Load an existing Safekeeper vault file,\n" +
			"or create a new one?",
			"Vault Loading Options",
			new String[] { "Load Existing", "Create New" },
			0);
		
		// Return the LoadOption selected
		if (index == 0) return LoadOption.LOAD_EXISTING;
		if (index == 1) return LoadOption.CREATE_NEW;
		return LoadOption.NONE; // GUIUtils.CLOSED_OPTION, meaning the chooser was closed
	}
	
	public static File selectVaultFile (JFrame parentFrame) {
		JFileChooser fileChooser = makeVaultFileChooser();
		
		// Show an "Open" dialog and return the file if the user chose a file
		if (fileChooser.showOpenDialog(parentFrame) == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		return null;
	}
	
	public static File getNewVaultFile(JFrame paramJFrame) {
		JFileChooser fileChooser = makeVaultFileChooser();
		
		// Show a "Save" dialog and return the file if the user chose a file
		if (fileChooser.showSaveDialog(paramJFrame) == JFileChooser.APPROVE_OPTION) {
			// Add the extension to the end of the file name if it
			// doesn't already have it
			File vaultFile = fileChooser.getSelectedFile();
			if (!vaultFile.getName().endsWith("."+EXTENSION))
				return new File(vaultFile.getAbsolutePath() + "."+EXTENSION);
			return vaultFile;
		} return null;
	}
	
	private static JFileChooser makeVaultFileChooser () {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(
			new FileNameExtensionFilter(
				"Safekeeper Vault File (."+EXTENSION+")",
				new String[] { EXTENSION }));
		return fileChooser;
	}
	
	private static final int MIN_MASTER_PASSWORD_LENGTH = 12;
	
	public static String makeNewMasterPassword (JFrame parentFrame) {
		// Latch used to ensure the method only returns after a new master password has been set
		CountDownLatch latch = new CountDownLatch(1);
		
		// Make the panel and dialog, which counts down the latch if closed (the method will return null in this case)
		JDialog dialog = GUIUtils.makeNewDialog("Set Master Password", parentFrame, () -> latch.countDown());
		JPanel panel = new JPanel(new BorderLayout());
		
		// Explanation label
		JLabel explanationLabel = GUIUtils.makeLabel(
			"Your master password will be used to log into your password vault,\n" +
			"so it is vital that it is very secure. Please use at least 15\n" +
			"characters, including symbols, numbers, and letters. Your master\n" +
			"password should not include any words or names. It is recommended\n" +
			"that you write it down in a safe location, because if it is lost,\n" +
			"it will be impossible to open your password vault.");
		
		explanationLabel.setBorder(GUIUtils.createMarginBorder(GUIUtils.MARGIN));
		panel.add(explanationLabel, BorderLayout.NORTH);
		
		// Password panel
		MasterPasswordPanel passwordPanel = new MasterPasswordPanel(parentFrame, latch);
		panel.add(passwordPanel, BorderLayout.CENTER);
		
		// Password generator panel
		PasswordGeneratorHolder generator = new PasswordGeneratorHolder();
		generator.panel = new PasswordGeneratorPanel(MIN_MASTER_PASSWORD_LENGTH, 15, (e) -> {
			// If the apply button is clicked, set the master password field's value
			passwordPanel.passwordField.setPassword(generator.panel.getPassword());
			
			// Show the password as well
			passwordPanel.passwordField.setPasswordIsVisible(true);
		});
		
		panel.add(GUIUtils.addMargin(generator.panel, GUIUtils.MARGIN), BorderLayout.SOUTH);
		
		// Finalize dialog
		dialog.add(panel);
		dialog.pack();
		GUIUtils.finalizeWindow(dialog, parentFrame);
		
		// Wait for latch to count down
		try { latch.await(); }
		catch (Exception exception) { }
		
		// Dispose the dialog and return the password
		dialog.dispose();
		return passwordPanel.password;
	}
	
	private static class PasswordGeneratorHolder {
		private PasswordGeneratorPanel panel;
	}
	
	private static class MasterPasswordPanel extends JPanel {
		
		private final CountDownLatch submitLatch;
		
		private final JFrame parentFrame;
		private final PasswordField passwordField, passwordReentryField;
		
		private String password;
		
		public MasterPasswordPanel (JFrame parentFrame, CountDownLatch submitLatch) {
			this.parentFrame = parentFrame;
			this.submitLatch = submitLatch;
			
			// Set the layout and border
			MatchedPairsLayout layout = new MatchedPairsLayout(this);
			setLayout(layout);
			setBorder(GUIUtils.createMarginBorder(GUIUtils.MARGIN));
			
			// Master password field and label
			passwordField = new PasswordField(1);
			layout.addMatch(
				GUIUtils.makeLabel("New Master Password"),
				passwordField);
			
			// Password re-entry field and label
			passwordReentryField = new PasswordField(1);
			layout.addMatch(
				GUIUtils.makeLabel("Password Re-Entry"),
				passwordReentryField);
			
			// Whenever the master password field is edited, update the field backgrounds
			passwordField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate (DocumentEvent e) { updatePasswordFieldBackground(); }
				
				@Override
				public void removeUpdate (DocumentEvent e) { updatePasswordFieldBackground(); }
				
				@Override
				public void changedUpdate (DocumentEvent e) {}
			});
			
			updatePasswordFieldBackground();
			
			// On password submit listeners
			passwordField.addPasswordSubmittedListener(e -> onPasswordSubmit());
			passwordReentryField.addPasswordSubmittedListener(e -> onPasswordSubmit());
			
			// Submit button
			layout.addMatch(
				GUIUtils.makeLabel(),
				GUIUtils.makeStretchPanel(GUIUtils.makeButton("Submit", e -> onPasswordSubmit())));
			
			// Finalize layout
			layout.setAutoCreateGaps(true);
			layout.finalizeLayout();
		}
		
		private static final Color INVALID_MASTER_PASSWORD_COLOR = new Color(255, 200, 200);
		
		private void updatePasswordFieldBackground () {
			// Check if the master password is valid
			boolean isValid = checkMasterPasswordValid(passwordField.getPassword(), false);
			
			// Get the new background color
			Color color = isValid ? Color.WHITE : INVALID_MASTER_PASSWORD_COLOR;
			
			// Set background colors
			passwordField.setFieldBackground(color);
			passwordReentryField.setFieldBackground(color);
		}
		
		private void onPasswordSubmit () {
			// If the master password field is empty, or the value is invalid, do nothing
			if (passwordField.getPassword().isEmpty() || !checkMasterPasswordValid(passwordField.getPassword(), true)) return;
			
			// If the password re-entry field is empty, display a warning
			if (passwordReentryField.getPassword().isEmpty()) {
				GUIUtils.showWarning("You must re-enter the master password into the re-entry field.");
				return;
			}
			
			// If the fields do not match, display a warning
			if (!passwordField.getPassword().equals(passwordReentryField.getPassword())) {
				GUIUtils.showWarning("The master password and the re-entered password do not match.");
				return;
			}
			
			// Otherwise, display a confirm master password chooser
			int index = GUIUtils.showOptionChooser(
				parentFrame,
				"Are you sure you want this to be your master password,\n" +
				"which will be used to access this Safekeeper password\n" +
				"vault in the future?",
				"Confirm Master Password",
				new String[] { "Confirm", "Cancel" },
				0);
			
			// If "Confirm" wasn't chosen, do nothing
			if (index != 0) return;
			
			// Otherwise, count down the submit latch
			password = passwordField.getPassword();
			submitLatch.countDown();
		}
		
	}
	
	private static boolean checkMasterPasswordValid(String password, boolean showWarning) {
		if (password.length() < MIN_MASTER_PASSWORD_LENGTH) {
			// Invalid, show a warning if necessary and return false
			if (showWarning)
				GUIUtils.showWarning("The master password must be at least 12\ncharacters, 15 characters is recommended.");
			return false;
		} return true; // Valid
	}
	
}