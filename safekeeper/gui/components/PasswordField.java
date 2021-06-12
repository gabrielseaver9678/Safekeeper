
// PasswordField.java, Gabriel Seaver, 2021

package safekeeper.gui.components;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.Document;

import safekeeper.gui.util.GUIUtils;

public class PasswordField extends JPanel {
	
	private static final String SHOW = "Show", HIDE = "Hide";
	
	private JPasswordField passwordField;
	private JButton showButton;
	private boolean isPasswordVisible;
	
	public PasswordField (String initialPassword, int cols) {
		// Sets layout and alignment
		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(boxLayout);
		setAlignmentY(JComponent.CENTER_ALIGNMENT);
		
		// Creates show/hide button and password field
		showButton = GUIUtils.makeButton(
			SHOW,
			e -> setPasswordIsVisible(!isPasswordVisible));
		passwordField = new JPasswordField(initialPassword, cols);
		passwordField.setFont(GUIUtils.fontPassword);
		
		// Makes the 
		setPasswordIsVisible(false);
		add(passwordField);
		add(showButton);
	}
	
	public PasswordField (int cols) {
		this("", cols);
	}
	
	public void setPasswordIsVisible (boolean isVisible) {
		// Updates isPasswordVisible boolean
		isPasswordVisible = isVisible;
		
		// Updates button text and password field display text
		showButton.setText(isPasswordVisible ? HIDE : SHOW);
		passwordField.setEchoChar(isPasswordVisible ? '\0' : '*');
	}
	
	public void addPasswordSubmittedListener (ActionListener listener) {
		passwordField.addActionListener(listener);
	}
	
	public String getPassword () {
		return new String(passwordField.getPassword());
	}
	
	public void setPassword (String password) {
		passwordField.setText(password);
	}
	
	public void clearPassword () {
		setPassword("");
	}
	
	public void setEditable (boolean editable) {
		passwordField.setEditable(editable);
	}
	
	public Document getDocument () {
		return passwordField.getDocument();
	}
	
	public void setFieldBackground (Color background) {
		passwordField.setBackground(background);
	}
	
}