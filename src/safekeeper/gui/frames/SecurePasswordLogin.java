
// SecurePasswordLogin.java, Gabriel Seaver, 2021

package safekeeper.gui.frames;

import java.util.concurrent.CountDownLatch;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import safekeeper.gui.components.PasswordField;
import safekeeper.gui.util.GUIUtils;

public class SecurePasswordLogin {
	
	private final CountDownLatch waitForCorrectPassword;
	private boolean exitIfClosed;
	private String password;
	
	private final JDialog window;
	private final PasswordField passwordField;
	
	private final CheckPasswordFunction checkPassword;
	
	private SecurePasswordLogin (JFrame parentWindow, CheckPasswordFunction checkPasswordFunction) throws InterruptedException {
		checkPassword = checkPasswordFunction;
		
		// Currently, if the window is closed then the program should quit
		exitIfClosed = true;
		
		// Allows for waiting for the correct password
		waitForCorrectPassword = new CountDownLatch(1);
		
		// Make the dialog window
		window = GUIUtils.makeNewDialog("Safekeeper Login", parentWindow, () -> {
			if (exitIfClosed) System.exit(0);
		});
		
		// Panel
		JPanel panel = new JPanel();
		
		// Field label
		panel.add(GUIUtils.makeLabel("Enter your master password:"));
		
		// Password field
		passwordField = new PasswordField(25);
		passwordField.addPasswordSubmittedListener(e -> passwordSubmitted());
		
		panel.add(passwordField);
		
		// Dialog finalization
		window.add(panel);
		window.pack();
		GUIUtils.finalizeWindow(window, parentWindow);
		
		// Wait for the correct password
		waitForCorrectPassword.await();
	}
	
	private void passwordSubmitted () {
		// Update password string to hold the submitted password
		password = passwordField.getPassword();
		
		// Clear the password field
		passwordField.clearPassword();
		
		// Check if the password is correct
		if (checkPassword.checkCorrectPassword(password)) {
			// Allows the dialog to close without exiting the program
			exitIfClosed = false;
			
			// Close the dialog window
			window.dispose();
			
			// Count down the latch so that the constructor function may now return
			waitForCorrectPassword.countDown();
		} else {
			// Show a warning
			GUIUtils.showWarning("The entered master password was incorrect,\nor the vault file is corrupted.");
		}
	}
	
	public static String login (JFrame parentFrame, CheckPasswordFunction checkPasswordFunction) throws InterruptedException {
		SecurePasswordLogin securePasswordLogin = new SecurePasswordLogin(parentFrame, checkPasswordFunction);
		return securePasswordLogin.password;
	}
	
	@FunctionalInterface
	public static interface CheckPasswordFunction {
		abstract boolean checkCorrectPassword (String param1String);
	}
	
}