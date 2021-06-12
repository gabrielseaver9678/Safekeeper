
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
	
	protected JTextField usernameField, emailField;
	protected JTextArea notesField;
	protected PasswordField passwordField;
	private DragHiddenTextField dragAndDrop;
	private PasswordGeneratorPanel passwordGeneratorPanel;
	
	protected boolean usernameEdited, emailEdited, notesEdited, passwordEdited;
	
	private final Color editedColor; // The background color a field turns when edited
	
	public AccountWindow (
			String title,
			MainWindow mainWindow,
			AccountGroup account,
			boolean colorEditedFields,
			boolean showDragAndDrop) {
		super(mainWindow, title);
		
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
			passwordGeneratorPanel = new PasswordGeneratorPanel(8, 20,
				e -> applyGeneratedPassword()),
			BorderLayout.CENTER);
		
		// Group of buttons at the bottom
		panel.add(createButtonPanel(), BorderLayout.SOUTH);
		
		addEditingListeners();
		
		// Finalize window
		add(panel);
		pack();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		GUIUtils.stylizeWindow(this, mainWindow, this::onClosing);
	}
	
	private JPanel createAccountPanel (boolean showDragAndDrop) {
		// Account panel and layout
		JPanel panel = new JPanel();
		MatchedPairsLayout layout = new MatchedPairsLayout(panel);
		panel.setLayout(layout);
		
		// Username field and label
		usernameField = GUIUtils.makeTextField(true, accountGroup.username);
		layout.addMatch(
				GUIUtils.makeLabel("Username", true),
				usernameField);
		
		// Email field and label
		emailField = GUIUtils.makeTextField(true, accountGroup.email);
		layout.addMatch(
				GUIUtils.makeLabel("Email Address", true),
				emailField);
		
		// Password field and label
		layout.addMatch(
				GUIUtils.makeLabel("Password", true),
				passwordField = new PasswordField(accountGroup.getPassword(), 1));
		
		// Show last password button
		if (accountGroup.getLastPassword() != null)
			layout.addMatch(
				GUIUtils.makeLabel("", true),
				GUIUtils.makeStretchPanel(
					GUIUtils.makeButton("Show Last Password", e -> makeLastPasswordDialog())));
		
		// Notes field and label
		notesField = new JTextArea(4, 15);
		notesField.setText(accountGroup.notes);
		notesField.setFont(GUIUtils.fontSmall);
		notesField.setLineWrap(true);
		
		JScrollPane notesScrollPane = new JScrollPane(notesField);
		notesScrollPane.setBorder(this.usernameField.getBorder());
		layout.addMatch(
			GUIUtils.makeLabel("Notes", true),
			notesScrollPane);
		
		// Drag-and-drop (if showDragAndDrop is true)
		dragAndDrop = new DragHiddenTextField(accountGroup.getPassword());
		if (showDragAndDrop)
			layout.addMatch(
				GUIUtils.makeLabel("Password Drag-and-Drop", true),
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
				GUIUtils.makeLabel("Last Password", true),
				passwordField);
		
		// Last change date viewer and label
		layout.addMatch(
				GUIUtils.makeLabel("Last Change", true),
				GUIUtils.makeLabel(
					new SimpleDateFormat("MMM d, yyyy (h:mm a)").format(accountGroup.getPasswordLastChangedDate()),
					true));
		
		// Finalize layout and dialog
		layout.setAutoCreateGaps(true);
		layout.finalizeLayout();
		
		lastPasswordWindow.add(panel);
		lastPasswordWindow.pack();
	}
	
	private void addEditingListeners () {
		setDocListener(usernameField.getDocument(), this::checkUsernameEdited);
		setDocListener(emailField.getDocument(), this::checkEmailEdited);
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
	
	protected final void checkUsernameEdited () {
		// Update edited boolean
		usernameEdited = !usernameField.getText().equals(accountGroup.username);
		
		// Update background color
		if (usernameEdited) usernameField.setBackground(editedColor);
		else usernameField.setBackground(Color.WHITE);
	}
	
	protected final void checkEmailEdited () {
		// Update edited boolean
		emailEdited = !emailField.getText().equals(accountGroup.email);
		
		// Update background color
		if (emailEdited) emailField.setBackground(editedColor);
		else emailField.setBackground(Color.WHITE);
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
		return usernameEdited || emailEdited || notesEdited || passwordEdited;
	}
	
	protected final void closeWindow () {
		// Dispose and update the account window variable
		dispose();
		mainWindow.accountWindow = null;
	}
	
	protected final void vaultEdited () {
		// Update fields
		checkUsernameEdited();
		checkEmailEdited();
		checkNotesEdited();
		checkPasswordEdited();
		dragAndDrop.setText(accountGroup.getPassword());
		
		// Update the vault edited status in the main window
		mainWindow.vaultEdited();
	}
	
	protected static final boolean validateUsernameAndPassword (String username, String password) {
		boolean unameInvalid = username.isBlank(), pwordInvalid = password.isEmpty();
		if (unameInvalid && pwordInvalid) {
			GUIUtils.showWarning("Username and password fields cannot be blank.");
			return false;
		} else if (unameInvalid) {
			GUIUtils.showWarning("Username field cannot be blank.");
			return false;
		} else if (pwordInvalid) {
			GUIUtils.showWarning("Password field cannot be blank.");
			return false;
		} return true;
	}
	
	protected abstract JPanel createButtonPanel();
	protected abstract void onClosing();
	
}