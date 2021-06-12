package safekeeper.gui.components;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
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
import javax.swing.event.ChangeEvent;

import safekeeper.crypto.Crypto;
import safekeeper.gui.layout.RowsLayout;
import safekeeper.gui.util.GUIUtils;

public class PasswordGeneratorPanel extends JPanel {
	private final JCheckBox lowercase;
	
	private final JCheckBox uppercase;
	
	private final JCheckBox numbers;
	
	private final JCheckBox symbols;
	
	private final JTextField passwordField;
	
	private final JSpinner lengthField;
	
	private final JButton generateButton;
	
	private final JLabel securityLabel;
	
	public PasswordGeneratorPanel(int minLength, int maxLength, ActionListener paramActionListener) {
		minLength = Math.max(minLength, 4);
		maxLength = Math.min(maxLength, 24);
	
		setBorder(BorderFactory.createTitledBorder("Password Generator"));
		RowsLayout rowsLayout = new RowsLayout(this, 10, 10);
		setLayout((LayoutManager)rowsLayout);
	
	
		this.lowercase = makeCheckBox("Lowercase");
		this.uppercase = makeCheckBox("Uppercase");
		this.numbers = makeCheckBox("Numbers");
		this.symbols = makeCheckBox("Symbols");
	
	
		this.passwordField = new JTextField(30);
		this.passwordField.setFont(GUIUtils.fontPassword);
		this.passwordField.setEditable(false);
	
	
		this.generateButton = GUIUtils.makeButton("Generate Password", paramActionEvent -> generatePassword());
	
	
		this.lengthField = new JSpinner(new SpinnerNumberModel(maxLength, minLength, maxLength, 1));
		this.lengthField.addChangeListener(paramChangeEvent -> generatePassword());
	
	
		this.securityLabel = GUIUtils.makeLabel("", false);
	
	
		rowsLayout.addRow(new JComponent[] { this.lowercase, this.uppercase, this.numbers, this.symbols });
		rowsLayout.addRow(new JComponent[] { this.lengthField, this.generateButton });
		rowsLayout.addRow(new JComponent[] { this.passwordField, GUIUtils.makeButton("Apply", paramActionListener) });
		rowsLayout.addRow(new JComponent[] { this.securityLabel });
	
	
		generatePassword();
	}
	
	private JCheckBox makeCheckBox(String paramString) {
		JCheckBox jCheckBox = new JCheckBox(paramString, true);
		jCheckBox.setFont(GUIUtils.font);
		jCheckBox.setFocusPainted(false);
		jCheckBox.addActionListener(paramActionEvent -> generatePassword());
		return jCheckBox;
	}
	
	private Crypto.SecurityLevel getPasswordSecurityLevel() {
		return Crypto.getSecurityLevel(this.lowercase
				.isSelected(), this.uppercase
				.isSelected(), this.numbers
				.isSelected(), this.symbols
				.isSelected(), ((Integer)this.lengthField
				.getValue()).intValue());
	}
	
	private String getNewPassword() {
		return Crypto.generatePasswordUseAll(this.lowercase
				.isSelected(), this.uppercase
				.isSelected(), this.numbers
				.isSelected(), this.symbols
				.isSelected(), ((Integer)this.lengthField
				.getValue()).intValue());
	}
	
	public String generatePassword() {
		String str = getNewPassword();
		if (str.equals("")) {
			GUIUtils.showWarning("Invalid password generation settings.");
			return null;
		}
		this.passwordField.setText(str);
		switch (getPasswordSecurityLevel()) {
			case EXTREMELY_UNSECURE:
				this.securityLabel.setForeground(new Color(180, 0, 0));
				this.securityLabel.setText("Warning: Extremely unsecure password");
				break;
			case UNSECURE:
				this.securityLabel.setForeground(new Color(180, 0, 0));
				this.securityLabel.setText("Warning: Unsecure password");
				break;
			case SECURE:
				this.securityLabel.setForeground(Color.BLACK);
				this.securityLabel.setText("Generated password is secure");
				break;
			case VERY_SECURE:
				this.securityLabel.setForeground(Color.BLACK);
				this.securityLabel.setText("Generated password is very secure");
				break;
		}
		return str;
	}
	
	public String getPassword() {
		return this.passwordField.getText();
	}
}
