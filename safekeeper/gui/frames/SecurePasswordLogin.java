package safekeeper.gui.frames;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import safekeeper.gui.components.PasswordField;
import safekeeper.gui.util.GUIUtils;

public class SecurePasswordLogin implements ActionListener {
	private String password;
	
	private final JDialog window;
	
	private final PasswordField passwordField;
	
	private final CheckPasswordFunction checkPassword;
	
	private final CountDownLatch waitForCorrectPassword;
	
	private boolean shutdownIfClosed;
	
	private SecurePasswordLogin(JFrame paramJFrame, CheckPasswordFunction paramCheckPasswordFunction) throws InterruptedException {
		this.shutdownIfClosed = true;
		this.checkPassword = paramCheckPasswordFunction;
		this.waitForCorrectPassword = new CountDownLatch(1);
		this.window = GUIUtils.makeNewDialog("Safekeeper Login", paramJFrame, () -> {
					if (this.shutdownIfClosed)
						System.exit(0);
				});
		this.passwordField = new PasswordField(25);
		this.passwordField.addPasswordSubmittedListener(this);
		JLabel jLabel = GUIUtils.makeLabel("Enter your master password:", true);
		jLabel.setLabelFor(this.passwordField);
		JPanel jPanel = new JPanel(new FlowLayout(1));
		jPanel.add(jLabel);
		jPanel.add(this.passwordField);
		this.window.add(jPanel);
		this.window.pack();
		this.waitForCorrectPassword.await();
	}
	
	public void actionPerformed(ActionEvent paramActionEvent) {
		this.password = this.passwordField.getPassword();
		this.passwordField.clearPassword();
		if (this.checkPassword.checkCorrectPassword(this.password)) {
			this.shutdownIfClosed = false;
			this.window.dispose();
			this.waitForCorrectPassword.countDown();
		} else {
			GUIUtils.playErrorSound();
			GUIUtils.showWarning("The entered master password was incorrect,\nor the vault file is corrupted.");
		}
	}
	
	public static String login(JFrame paramJFrame, CheckPasswordFunction paramCheckPasswordFunction) throws InterruptedException {
		SecurePasswordLogin securePasswordLogin = new SecurePasswordLogin(paramJFrame, paramCheckPasswordFunction);
		return securePasswordLogin.password;
	}
	
	@FunctionalInterface
	public static interface CheckPasswordFunction {
		boolean checkCorrectPassword(String param1String);
	}
}
