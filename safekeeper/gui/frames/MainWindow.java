
// MainWindow.java, Gabriel Seaver, 2021

package safekeeper.gui.frames;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;

import safekeeper.groupings.AccountGroup;
import safekeeper.groupings.ServiceGroup;
import safekeeper.groupings.ServiceGroupList;
import safekeeper.gui.util.GUIUtils;
import safekeeper.printing.SGLPrinter;

public class MainWindow extends JFrame {
	
	AccountWindow accountWindow = null;
	
	private ServiceGroupList sgl;
	private SaveFunction saveFunction;
	
	private boolean vaultHasBeenEdited = false;
	
	private JPanel buttonPanel;
	private JTree serviceTree;
	
	private JButton createAccountButton, createServiceButton, deleteServiceButton, printAllButton;
	private JComboBox<ServiceGroup> createAccountDropdown, deleteServiceDropdown;
	private JTextField createServiceField;
	
	public void vaultEdited () {
		vaultHasBeenEdited = true;
		recreateServiceTree();
		resetServiceDropdowns();
	}
	
	private class ServiceTreeRootNode extends DefaultMutableTreeNode {
		
		private ServiceTreeRootNode (ServiceGroupList sgl) {
			super("Services");
			
			// Gets services in alphabetical order
			Object[] services = sgl.getServicesAlphabetical();
			
			// Adds services to the tree
			for (Object service : services)
				add(new MainWindow.ServiceTreeServiceNode((ServiceGroup)service));
			
			// If there are no services, do not display the +/- button
			if (sgl.serviceGroups.size() == 0) setAllowsChildren(false);
		}
		
	}
	
	private class ServiceTreeServiceNode extends DefaultMutableTreeNode {
		
		private ServiceTreeServiceNode (ServiceGroup service) {
			super(service.name);
			
			// Gets accounts in alphabetical order
			Object[] accounts = service.getAccountsAlphabetical();
			
			// Adds accounts to the tree
			for (Object account : accounts)
				add(new MainWindow.ServiceTreeAccountNode((AccountGroup)account));
			
			// If there are no accounts, do not display the +/- button
			if (service.accountGroups.size() == 0) setAllowsChildren(false);
		}
		
	}
	
	private class ServiceTreeAccountNode extends DefaultMutableTreeNode {
		
		private final AccountGroup account;
		
		private ServiceTreeAccountNode (AccountGroup account) {
			super(account.username);
			this.account = account;
		}
		
		private void displayAccountWindow () {
			// If there is not already an account window, display a new one
			if (accountWindow == null)
				new DisplayAccountWindow(MainWindow.this, account);
		}
		
	}
	
	public MainWindow (SaveFunction saveFunction) {
		super("Safekeeper");
		
		// On closing
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		GUIUtils.stylizeWindow(this, null, this::onClosing);
		
		// Set save function
		this.saveFunction = saveFunction;
		
		// Create the scroll pane
		JScrollPane serviceScrollPane = new JScrollPane();
		serviceScrollPane.setBorder(GUIUtils.createMarginBorder(GUIUtils.MARGIN));
		serviceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		serviceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		GUIUtils.setSize(serviceScrollPane, 300, 300);
		
		// Initialize the service tree
		initializeServiceTree(serviceScrollPane);
		
		// Create the menu bar and button panel
		createMenuBar();
		createButtonPanel();
		
		// Add the panels and finalize
		add(serviceScrollPane, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);
		pack();
		GUIUtils.finalizeWindow(this, null);
	}
	
	public void setSGL (ServiceGroupList sgl) {
		// Set the SGL
		this.sgl = sgl;
		
		// Enable the buttons, dropdowns, etc.
		setUIEnabled(true);
		
		// Draw a new service tree
		recreateServiceTree();
		
		// Reset dropdowns to contain the most up-to-date services
		resetServiceDropdowns();
	}
	
	private void recreateServiceTree () {
		// Get the tree model
		DefaultTreeModel model = (DefaultTreeModel)serviceTree.getModel();
		
		// Remove all children of the root node
		((DefaultMutableTreeNode)model.getRoot()).removeAllChildren();
		
		// Recreate the tree recursively
		model.setRoot(new ServiceTreeRootNode(sgl));
		model.reload();
	}
	
	private void resetServiceDropdowns () {
		// Remove all dropdown items
		createAccountDropdown.removeAllItems();
		deleteServiceDropdown.removeAllItems();
		
		// Get the list of services in alphabetical order
		Object[] services = sgl.getServicesAlphabetical();
		
		// Loop through services, adding them to the dropdowns
		for (Object service : services) {
			createAccountDropdown.addItem((ServiceGroup)service);
			deleteServiceDropdown.addItem((ServiceGroup)service);
		}
	}
	
	private void setUIEnabled (boolean enabled) {
		// Enable all UI elements
		createAccountButton.setEnabled(enabled);
		createAccountDropdown.setEnabled(enabled);
		createServiceButton.setEnabled(enabled);
		createServiceField.setEnabled(enabled);
		deleteServiceButton.setEnabled(enabled);
		deleteServiceDropdown.setEnabled(enabled);
		printAllButton.setEnabled(enabled);
	}
	
	private void initializeServiceTree (JScrollPane serviceTreeParentPane) {
		// Create the tree model
		DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode());
		
		// Allows us to capture treeWillExpand for the account nodes to open account windows
		model.setAsksAllowsChildren(true);
		
		// Initialize the tree
		serviceTree = new JTree(model);
		serviceTree.setFont(GUIUtils.font);
		serviceTree.setOpaque(false);
		serviceTree.setToggleClickCount(1);
		serviceTree.addTreeWillExpandListener(new TreeWillExpandListener() {
			
			@Override
			public void treeWillExpand (TreeExpansionEvent e) throws ExpandVetoException {
				// Gets the node which is trying to expand
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
				
				// Check if the node is an account node
				if (node instanceof ServiceTreeAccountNode) {
					// Attempt to display a new account window, the user has clicked on the node
					((ServiceTreeAccountNode)node).displayAccountWindow();
					
					// Do not allow the node to try to expand, it will realize it does
					// not have children
					throw new ExpandVetoException(e, "Account node expansion veto");
				}
			}
			
			@Override
			public void treeWillCollapse (TreeExpansionEvent e) throws ExpandVetoException {
				// Check if the node is the root node
				if (((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).isRoot())
					// If so, do not allow the node to collapse
					throw new ExpandVetoException(e, "Root node cannot collapse");
			}
			
		});
		
		// Modify the tree cell renderer
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)serviceTree.getCellRenderer();
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);
		renderer.setLeafIcon(null);
		renderer.setBackgroundNonSelectionColor(getBackground());
		serviceTree.setCellRenderer(renderer);
		
		// Add the service tree to the scroll pane
		serviceTreeParentPane.setViewportView(serviceTree);
	}
	
	private void onClosing () {
		if (vaultHasBeenEdited) { // If the vault has been edited, save it and show a dialog
			boolean savedProperly = saveFunction.save();
			if (savedProperly) {
				// If it has saved properly, show a dialog to indicate this fact and then exit
				GUIUtils.showMessage(this, "Changes to your Safekeeper vault have been saved.", "Changes Saved");
				System.exit(0);
			} // If it hasn't saved properly, saveFunction.save() will have already shown a warning dialog
		} else System.exit(0); // Otherwise, exit immediately
	}
	
	private void createMenuBar() {
		// Create the menu bar
		JMenuBar menuBar = new JMenuBar();
		
		// Help menu
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		helpMenu.add(new JMenuItem("Show User Guide"));
		
		// Edit menu
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		// Finalize menu bar
		setJMenuBar(menuBar);
	}
	
	private void createButtonPanel() {
		// Initialize button panel and layout
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBorder(GUIUtils.createMarginBorder(GUIUtils.MARGIN));
		
		// Create account
		createAccountButton = GUIUtils.makeButton("Create New Account", e -> createAccount());
		createAccountDropdown = new JComboBox<>();
		createAccountDropdown.setFont(GUIUtils.fontButton);
		
		buttonPanel.add(makeButtonPanelRow(createAccountButton, createAccountDropdown));
		addButtonPanelStrut();
		
		// Create service
		createServiceButton = GUIUtils.makeButton("Create New Service", e -> createService());
		createServiceField = GUIUtils.makeTextField(true);
		createServiceField.addActionListener(e -> createService());
		
		buttonPanel.add(makeButtonPanelRow(createServiceButton, createServiceField));
		addButtonPanelStrut();
		
		// Delete service
		deleteServiceButton = GUIUtils.makeButton("Delete Service", e -> deleteService());
		deleteServiceDropdown = new JComboBox<>();
		deleteServiceDropdown.setFont(GUIUtils.fontButton);
		
		buttonPanel.add(makeButtonPanelRow(deleteServiceButton, deleteServiceDropdown));
		addButtonPanelStrut();
		
		// Print all
		printAllButton = GUIUtils.makeButton("Print All Account Information", e -> printAllAccountInfo());
		buttonPanel.add(GUIUtils.makeStretchPanel(printAllButton));
		
		// Disable UI elements
		setUIEnabled(false);
	}
	
	/**
	 * Used by createButtonPanel()
	 */
	private JPanel makeButtonPanelRow (JComponent leftComponent, JComponent rightComponent) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(leftComponent, BorderLayout.WEST);
		panel.add(rightComponent, BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * Used by createButtonPanel()
	 */
	private void addButtonPanelStrut () {
		buttonPanel.add(GUIUtils.makeVerticalStrut(GUIUtils.MARGIN));
	}
	
	/**
	 * Attempt to make a create new account window
	 */
	private void createAccount () {
		// If there is already an account window open, show a warning and do nothing
		if (accountWindow != null) {
			GUIUtils.showWarning("Cannot create a new account while\nediting another.");
			return;
		}
		
		// Otherwise, get the service
		ServiceGroup service = (ServiceGroup)createAccountDropdown.getSelectedItem();
		
		// If the dropdown had no services, service will be null
		if (service == null) return;
		
		// Create an account window
		new CreateAccountWindow(this, service);
	}
	
	/**
	 * Attempt to create a new service
	 */
	private void createService () {
		// Finish if either services cannot be edited or if the service name isn't valid
		if (!checkCanEditServices() || !checkCreateServiceNameIsValid()) return;
		
		// Show a "create new service" chooser
		int index = GUIUtils.showOptionChooser(
			this,
			"Are you sure you want to create a new service?\n" +
			"Services cannot be renamed once created.\n" +
			createServiceField.getText(),
			"Create New Service?",
			new String[] { "Create Service", "Cancel" },
			0);
		
		// If "Create Service" wasn't chosen, finish
		if (index != 0) return;
		
		// Create a new service and add it to the SGL
		sgl.serviceGroups.add(new ServiceGroup(createServiceField.getText()));
		
		// Update the vault edited status
		vaultEdited();
	}
	
	private void deleteService () {
		// If services cannot be edited right now, finish
		if (!checkCanEditServices()) return;
		
		// Get the service
		ServiceGroup service = (ServiceGroup)deleteServiceDropdown.getSelectedItem();
		
		// If there are no services in the SGL, service will be null
		if (service == null) return;
		
		// Get the message to display in the chooser and the default index, based on whether or not
		// the service has one or more connected accounts
		String message;
		int defaultIndex;
		if (service.accountGroups.size() == 0) {
			// If there are no connected accounts, the default
			// option is "Delete Permanently" because nothing
			// too important is connected
			message =
				"Are you sure you want to delete this service?\n" +
				service.name;
			defaultIndex = 0;
		} else {
			// If there are 1 or more connected accounts, the
			// default option is "Cancel" to be extra safe
			message =
				"Are you sure you want to delete this service, which\n" +
				"has " + service.accountGroups.size() + " connected account(s)?\n" +
				service.name;
			defaultIndex = 1;
		}
		
		// Show the chooser
		int index = GUIUtils.showOptionChooser(
			this,
			message,
			"Delete Service",
			new String[] { "Delete Permanently", "Cancel" },
			defaultIndex);
		
		// If "Delete Permanently" was not chosen, finish
		if (index != 0) return;
		
		// Otherwise, remove the service from the SGL
		sgl.serviceGroups.remove(service);
		
		// Update the vault edited status
		vaultEdited();
	}
	
	private void printAllAccountInfo () {
		// Show the print chooser
		int index = GUIUtils.showOptionChooser(
			this,
			"Are you sure you want to print all account information,\n" +
			"including passwords, usernames, and email addresses, for\n" +
			"for all accounts in every service?",
			"Print All Account Info?",
			new String[] { "Print All Info", "Cancel" },
			1);
		
		// If "Print All Info" was not selected, finish
		if (index != 0) return;
		
		// Attempt to print the SGL
		try {
			SGLPrinter.printSGL(sgl);
		} catch (Exception e) {
			GUIUtils.showWarning(e);
		}
	}
	
	private boolean checkCanEditServices () {
		// If there is no account window, services may be edited
		if (accountWindow == null) return true;
		
		// Otherwise, show a warning
		GUIUtils.showWarning("You cannot edit services while editing an account.");
		return false;
	}
	
	private boolean checkCreateServiceNameIsValid () {
		// If the create service field's value isn't blank, it is valid
		if (!createServiceField.getText().isBlank()) return true;
		
		// Otherwise, show a warning
		GUIUtils.showWarning("The name of the new service cannot be blank.");
		return false;
	}
	
	public static interface SaveFunction {
		abstract boolean save();
	}
	
}