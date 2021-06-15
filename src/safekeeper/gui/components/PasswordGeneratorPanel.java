
// PasswordGeneratorPanel.java, Gabriel Seaver, 2021

package safekeeper.gui.components;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import safekeeper.crypto.Crypto;
import safekeeper.crypto.Crypto.SecurityLevel;
import safekeeper.gui.layout.RowsLayout;
import safekeeper.gui.util.GUIUtils;

public class PasswordGeneratorPanel extends JPanel {
	
	private final JCheckBox lowercase, uppercase, numbers, symbols;
	private final JTextField passwordField;
	private final JSpinner lengthField;
	private final JButton generateButton;
	private final JLabel securityLabel;
	
	private static final int MAX_LENGTH = 20;
	
	public PasswordGeneratorPanel (int minLength, int defaultLength, ActionListener applyButtonListener) {
		// Keeps min at 4 or above
		minLength = Math.max(minLength, 4);
		
		// Border and layout
		setBorder(BorderFactory.createTitledBorder("Password Generator"));
		RowsLayout rowsLayout = new RowsLayout(this, GUIUtils.MARGIN, GUIUtils.MARGIN);
		setLayout(rowsLayout);
		
		// Check boxes
		lowercase = makeCheckBox("Lowercase");
		uppercase = makeCheckBox("Uppercase");
		numbers = makeCheckBox("Numbers");
		symbols = makeCheckBox("Symbols");
		
		// Password field
		passwordField = new JTextField(30);
		passwordField.setFont(GUIUtils.fontPassword);
		passwordField.setEditable(false);
		
		// Generate button
		generateButton = GUIUtils.makeButton("Generate Password", e -> generatePassword());
		
		// Length field
		lengthField = new JSpinner(new SpinnerNumberModel(defaultLength, minLength, MAX_LENGTH, 1));
		lengthField.addChangeListener(e -> generatePassword());
		
		// Security label
		securityLabel = GUIUtils.makeLabel();
		
		// Initialize rows
		rowsLayout.addRow(new JComponent[] { lowercase, uppercase, numbers, symbols });
		rowsLayout.addRow(new JComponent[] { lengthField, generateButton });
		rowsLayout.addRow(new JComponent[] { passwordField, GUIUtils.makeButton("Apply", applyButtonListener) });
		rowsLayout.addRow(new JComponent[] { securityLabel });
		
		// Generates a new password
		generatePassword();
	}
	
	private JCheckBox makeCheckBox (String checkBoxText) {
		JCheckBox checkBox = new JCheckBox(checkBoxText, true);
		checkBox.setFont(GUIUtils.font);
		checkBox.setFocusPainted(false);
		checkBox.addActionListener(e -> generatePassword());
		return checkBox;
	}
	
	private SecurityLevel getPasswordSecurityLevel () {
		return Crypto.getPasswordSecurityLevel(
			lowercase.isSelected(), 
			uppercase.isSelected(), 
			numbers.isSelected(), 
			symbols.isSelected(),
			(Integer)lengthField.getValue());
	}
	
	private String getNewPassword () {
		return Crypto.generatePasswordUseAll(
			lowercase.isSelected(), 
			uppercase.isSelected(), 
			numbers.isSelected(), 
			symbols.isSelected(),
			(Integer)lengthField.getValue());
	}
	
	private static final Color UNSECURE_COLOR = new Color(180, 0, 0);
	
	public String generatePassword () {
		// Gets a new password
		String password = getNewPassword();
		if (password.equals("")) {
			GUIUtils.showWarning("Invalid password generation settings.");
			return null;
		}
		
		// Sets the password and the security label
		passwordField.setText(password);
		switch (getPasswordSecurityLevel()) {
			case EXTREMELY_UNSECURE:
				securityLabel.setForeground(UNSECURE_COLOR);
				securityLabel.setText("Warning: Extremely unsecure password");
				break;
			case UNSECURE:
				securityLabel.setForeground(UNSECURE_COLOR);
				securityLabel.setText("Warning: Unsecure password");
				break;
			case SECURE:
				securityLabel.setForeground(Color.BLACK);
				securityLabel.setText("Generated password is secure");
				break;
			case VERY_SECURE:
				securityLabel.setForeground(Color.BLACK);
				securityLabel.setText("Generated password is very secure");
				break;
		} return password;
	}
	
	public String getPassword () {
		return passwordField.getText();
	}
	
}