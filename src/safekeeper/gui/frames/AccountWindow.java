
// AccountWindow.java, Gabriel Seaver, 2021

package safekeeper.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import safekeeper.groupings.AccountGroup;
import safekeeper.gui.components.DragHiddenTextField;
import safekeeper.gui.components.PasswordField;
import safekeeper.gui.components.PasswordGeneratorPanel;
import safekeeper.gui.layout.MatchedPairsLayout;
import safekeeper.gui.util.GUIUtils;

public abstract class AccountWindow extends JDialog {
	
	protected final AccountGroup accountGroup;
	
	private final MainWindow mainWindow;
	protected JDialog lastPasswordWindow;
	
	private JTextField companyField, usernameField;
	private JTextArea notesField;
	private PasswordField passwordField;
	private DragHiddenTextField dragAndDrop;
	private PasswordGeneratorPanel passwordGeneratorPanel;
	
	protected boolean companyEdited, usernameEdited, notesEdited, passwordEdited;
	
	private final Color editedColor; // The background color a field turns when edited
	
	public AccountWindow (
			String title,
			MainWindow mainWindow,
			AccountGroup account,
			boolean colorEditedFields,
			boolean showDragAndDrop) {
		super(mainWindow, title);
		
		// Window closing and stylization
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		GUIUtils.stylizeWindow(this, mainWindow, this::onClosing);
		
		this.mainWindow = mainWindow;
		accountGroup = account;
		editedColor = colorEditedFields ? Color.YELLOW : Color.WHITE;
		mainWindow.accountWindow = this;
		
		// Main panel layout and margins
		JPanel panel = new JPanel(new BorderLayout(GUIUtils.MARGIN, GUIUtils.MARGIN));
		panel.setBorder(GUIUtils.createMarginBorder(GUIUtils.MARGIN));
		
		// Account panel with username, password, etc. fields
		panel.add(createAccountPanel(showDragAndDrop), BorderLayout.NORTH);
		
		// Password generator panel
		panel.add(
			passwordGeneratorPanel = new PasswordGeneratorPanel(8, 14,
				e -> applyGeneratedPassword()),
			BorderLayout.CENTER);
		
		// Group of buttons at the bottom
		panel.add(createButtonPanel(), BorderLayout.SOUTH);
		
		addEditingListeners();
		
		// Finalize window
		add(panel);
		pack();
		GUIUtils.finalizeWindow(this, mainWindow);
	}
	
	private JPanel createAccountPanel (boolean showDragAndDrop) {
		// Account panel and layout
		JPanel panel = new JPanel();
		MatchedPairsLayout layout = new MatchedPairsLayout(panel);
		panel.setLayout(layout);
		
		// Company field and label
		companyField = GUIUtils.makeTextField(accountGroup.company);
		layout.addMatch(
			GUIUtils.makeLabel("Company"),
			companyField);
		
		// Username field and label
		usernameField = GUIUtils.makeTextField(accountGroup.username);
		layout.addMatch(
			GUIUtils.makeLabel("Username"),
			usernameField);
		
		// Password field and label
		layout.addMatch(
			GUIUtils.makeLabel("Password"),
			passwordField = new PasswordField(accountGroup.getPassword(), 1));
		
		// Show last password button
		if (accountGroup.getLastPassword() != null)
			layout.addMatch(
				GUIUtils.makeLabel(),
				GUIUtils.makeStretchPanel(
					GUIUtils.makeButton("Show Last Password", e -> makeLastPasswordDialog())));
		
		// Notes field and label
		notesField = new JTextArea(4, 15);
		notesField.setText(accountGroup.notes);
		notesField.setFont(GUIUtils.font);
		notesField.setLineWrap(true);
		
		JScrollPane notesScrollPane = new JScrollPane(notesField);
		notesScrollPane.setBorder(this.usernameField.getBorder());
		layout.addMatch(
			GUIUtils.makeLabel("Notes"),
			notesScrollPane);
		
		// Drag-and-drop (if showDragAndDrop is true)
		dragAndDrop = new DragHiddenTextField(accountGroup.getPassword());
		if (showDragAndDrop)
			layout.addMatch(
				GUIUtils.makeLabel("Copy Password"),
				dragAndDrop);
		
		// Finalize layout
		layout.setAutoCreateGaps(true);
		layout.finalizeLayout();
		return panel;
	}
	
	private void applyGeneratedPassword () {
		// Set the password field's password, and show the new password to
		// the user for extra clarity
		passwordField.setPassword(passwordGeneratorPanel.getPassword());
		passwordField.setPasswordIsVisible(true);
	}
	
	/**
	 * Attempt to make a "last password" dialog, if one is not already there
	 */
	private void makeLastPasswordDialog () {
		// Do nothing if there is already a "last password" dialog
		if (lastPasswordWindow != null) return;
		
		// Otherwise, make a new dialog
		lastPasswordWindow = GUIUtils.makeNewDialog("Last Password", this, () -> {
			// When the user attempts to exit the dialog, dispose it and
			// update lastPasswordWindow because it no longer exists
			lastPasswordWindow.dispose();
			lastPasswordWindow = null;
		});
		
		// Make the panel and layout
		JPanel panel = new JPanel();
		MatchedPairsLayout layout = new MatchedPairsLayout(panel);
		panel.setLayout(layout);
		panel.setBorder(GUIUtils.createMarginBorder(GUIUtils.MARGIN));
		
		// Last password viewer and label
		PasswordField passwordField = new PasswordField(accountGroup.getLastPassword(), 30);
		passwordField.setEditable(false);
		layout.addMatch(
			GUIUtils.makeLabel("Last Password"),
			passwordField);
		
		// Last change date viewer and label
		layout.addMatch(
			GUIUtils.makeLabel("Last Change"),
			GUIUtils.makeLabel(
				new SimpleDateFormat("MMM d, yyyy (h:mm a)").format(accountGroup.getPasswordLastChangedDate())));
		
		// Finalize layout and dialog
		layout.setAutoCreateGaps(true);
		layout.finalizeLayout();
		
		lastPasswordWindow.add(panel);
		lastPasswordWindow.pack();
		GUIUtils.finalizeWindow(lastPasswordWindow, this);
	}
	
	private void addEditingListeners () {
		setDocListener(companyField.getDocument(), this::checkCompanyEdited);
		setDocListener(usernameField.getDocument(), this::checkUsernameEdited);
		setDocListener(notesField.getDocument(), this::checkNotesEdited);
		setDocListener(passwordField.getDocument(), this::checkPasswordEdited);
	}
	
	/**
	 * Convenience method, used by addEditingListeners()
	 */
	private static void setDocListener (Document doc, EditListener listener) {
		doc.addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate (DocumentEvent e) { listener.onFieldEdit(); }
			
			@Override
			public void removeUpdate (DocumentEvent e) { listener.onFieldEdit(); }
			
			@Override
			public void changedUpdate (DocumentEvent e) {}
		});
	}
	
	@FunctionalInterface
	private static interface EditListener {
		abstract void onFieldEdit ();
	}
	
	protected final void checkCompanyEdited () {
		// Update edited boolean
		companyEdited = !companyField.getText().equals(accountGroup.company);
		
		// Update background color
		if (companyEdited) companyField.setBackground(editedColor);
		else companyField.setBackground(Color.WHITE);
	}
	
	protected final void checkUsernameEdited () {
		// Update edited boolean
		usernameEdited = !usernameField.getText().equals(accountGroup.username);
		
		// Update background color
		if (usernameEdited) usernameField.setBackground(editedColor);
		else usernameField.setBackground(Color.WHITE);
	}
	
	protected final void checkNotesEdited () {
		// Update edited boolean
		notesEdited = !notesField.getText().equals(accountGroup.notes);
		
		// Update background color
		if (notesEdited) notesField.setBackground(editedColor);
		else notesField.setBackground(Color.WHITE);
	}
	
	protected final void checkPasswordEdited () {
		// Update edited boolean
		passwordEdited = !passwordField.getPassword().equals(accountGroup.getPassword());
		
		// Update background color
		if (passwordEdited) passwordField.setFieldBackground(editedColor);
		else passwordField.setFieldBackground(Color.WHITE);
	}
	
	protected final boolean anyFieldEdited () {
		return companyEdited || usernameEdited || notesEdited || passwordEdited;
	}
	
	protected final void closeWindow () {
		// Dispose and update the account window variable
		dispose();
		mainWindow.accountWindow = null;
	}
	
	protected final void vaultEdited () {
		// Update fields
		checkCompanyEdited();
		checkUsernameEdited();
		checkNotesEdited();
		checkPasswordEdited();
		
		dragAndDrop.setText(accountGroup.getPassword());
		
		// Update the vault edited status in the main window
		mainWindow.vaultEdited();
	}
	
	protected final boolean validateFields () {
		// Determine which fields are invalid
		final boolean
			comInvalid = companyField.getText().isBlank(),
			unameInvalid = usernameField.getText().isBlank(),
			pwordInvalid = passwordField.getPassword().isEmpty();
		
		// Show a warning for one of the invalid fields
		if (comInvalid)
			GUIUtils.showWarning("Company field cannot be blank.");
		else if (unameInvalid)
			GUIUtils.showWarning("Username field cannot be blank.");
		else if (pwordInvalid)
			GUIUtils.showWarning("Password field cannot be blank.");
		
		// Return false if any field is invalid, true if all are valid
		return !(comInvalid || unameInvalid || pwordInvalid);
	}
	
	protected final void saveAllFieldDataToAccount () {
		accountGroup.company = companyField.getText();
		accountGroup.username = usernameField.getText();
		if (passwordEdited) // .setPassword has side effects, only use when necessary
			accountGroup.setPassword(passwordField.getPassword());
		accountGroup.notes = notesField.getText().strip();
	}
	
	protected abstract JPanel createButtonPanel();
	protected abstract void onClosing();
	
}