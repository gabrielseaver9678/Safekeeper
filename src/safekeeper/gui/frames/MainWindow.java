
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
import safekeeper.groupings.CategoryGroup;
import safekeeper.groupings.CategoryGroupList;
import safekeeper.gui.util.GUIUtils;
import safekeeper.printing.CGLPrinter;

public class MainWindow extends JFrame {
	
	AccountWindow accountWindow = null;
	
	private CategoryGroupList cgl;
	private SaveFunction saveFunction;
	
	private boolean vaultHasBeenEdited = false;
	
	private JPanel buttonPanel;
	private JTree categoryTree;
	
	private JButton createAccountButton, createCategoryButton, deleteCategoryButton, printAllButton;
	private JComboBox<CategoryGroup> createAccountDropdown, deleteCategoryDropdown;
	private JTextField createCategoryField;
	
	public void vaultEdited () {
		vaultHasBeenEdited = true;
		recreateCategoryTree();
		resetCategoryDropdowns();
	}
	
	private class CategoryTreeRootNode extends DefaultMutableTreeNode {
		
		private CategoryTreeRootNode (CategoryGroupList cgl) {
			super("Categories");
			
			// Gets categories in alphabetical order
			Object[] categories = cgl.getCategoriesAlphabetical();
			
			// Adds categories to the tree
			for (Object category : categories)
				add(new MainWindow.CategoryTreeCategoryNode((CategoryGroup)category));
			
			// If there are no categories, do not display the +/- button
			if (cgl.categoryGroups.size() == 0) setAllowsChildren(false);
		}
		
	}
	
	private class CategoryTreeCategoryNode extends DefaultMutableTreeNode {
		
		private CategoryTreeCategoryNode (CategoryGroup category) {
			super(category.name);
			
			// Gets accounts in alphabetical order
			Object[] accounts = category.getAccountsAlphabetical();
			
			// Adds accounts to the tree
			for (Object account : accounts)
				add(new MainWindow.CategoryTreeAccountNode((AccountGroup)account));
			
			// If there are no accounts, do not display the +/- button
			if (category.accountGroups.size() == 0) setAllowsChildren(false);
		}
		
	}
	
	private class CategoryTreeAccountNode extends DefaultMutableTreeNode {
		
		private final AccountGroup account;
		
		private CategoryTreeAccountNode (AccountGroup account) {
			super(account.getDisplayName());
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
		JScrollPane categoryScrollPane = new JScrollPane();
		categoryScrollPane.setBorder(GUIUtils.createMarginBorder(GUIUtils.MARGIN));
		categoryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		categoryScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		GUIUtils.setSize(categoryScrollPane, 400, 400);
		
		// Initialize the category tree
		initializeCategoryTree(categoryScrollPane);
		
		// Create the menu bar and button panel
		createMenuBar();
		createButtonPanel();
		
		// Add the panels and finalize
		add(categoryScrollPane, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);
		pack();
		GUIUtils.finalizeWindow(this, null);
	}
	
	public void setCGL (CategoryGroupList cgl) {
		// Set the CGL
		this.cgl = cgl;
		
		// Enable the buttons, dropdowns, etc.
		setUIEnabled(true);
		
		// Draw a new category tree
		recreateCategoryTree();
		
		// Reset dropdowns to contain the most up-to-date categories
		resetCategoryDropdowns();
	}
	
	private void recreateCategoryTree () {
		// Get the tree model
		DefaultTreeModel model = (DefaultTreeModel)categoryTree.getModel();
		
		// Remove all children of the root node
		((DefaultMutableTreeNode)model.getRoot()).removeAllChildren();
		
		// Recreate the tree recursively
		model.setRoot(new CategoryTreeRootNode(cgl));
		model.reload();
	}
	
	private void resetCategoryDropdowns () {
		// Remove all dropdown items
		createAccountDropdown.removeAllItems();
		deleteCategoryDropdown.removeAllItems();
		
		// Get the list of categories in alphabetical order
		Object[] categories = cgl.getCategoriesAlphabetical();
		
		// Loop through categories, adding them to the dropdowns
		for (Object category : categories) {
			createAccountDropdown.addItem((CategoryGroup)category);
			deleteCategoryDropdown.addItem((CategoryGroup)category);
		}
	}
	
	private void setUIEnabled (boolean enabled) {
		// Enable all UI elements
		createAccountButton.setEnabled(enabled);
		createAccountDropdown.setEnabled(enabled);
		createCategoryButton.setEnabled(enabled);
		createCategoryField.setEnabled(enabled);
		deleteCategoryButton.setEnabled(enabled);
		deleteCategoryDropdown.setEnabled(enabled);
		printAllButton.setEnabled(enabled);
	}
	
	private void initializeCategoryTree (JScrollPane categoryTreeParentPane) {
		// Create the tree model
		DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode());
		
		// Allows us to capture treeWillExpand for the account nodes to open account windows
		model.setAsksAllowsChildren(true);
		
		// Initialize the tree
		categoryTree = new JTree(model);
		categoryTree.setFont(GUIUtils.font);
		categoryTree.setOpaque(false);
		categoryTree.setToggleClickCount(1);
		categoryTree.addTreeWillExpandListener(new TreeWillExpandListener() {
			
			@Override
			public void treeWillExpand (TreeExpansionEvent e) throws ExpandVetoException {
				// Gets the node which is trying to expand
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
				
				// Check if the node is an account node
				if (node instanceof CategoryTreeAccountNode) {
					// Attempt to display a new account window, the user has clicked on the node
					((CategoryTreeAccountNode)node).displayAccountWindow();
					
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
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)categoryTree.getCellRenderer();
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);
		renderer.setLeafIcon(null);
		renderer.setBackgroundNonSelectionColor(getBackground());
		categoryTree.setCellRenderer(renderer);
		
		// Add the category tree to the scroll pane
		categoryTreeParentPane.setViewportView(categoryTree);
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
		
		// Create category
		createCategoryButton = GUIUtils.makeButton("Create New Category", e -> createCategory());
		createCategoryField = GUIUtils.makeTextField(true);
		createCategoryField.addActionListener(e -> createCategory());
		
		buttonPanel.add(makeButtonPanelRow(createCategoryButton, createCategoryField));
		addButtonPanelStrut();
		
		// Delete category
		deleteCategoryButton = GUIUtils.makeButton("Delete Category", e -> deleteCategory());
		deleteCategoryDropdown = new JComboBox<>();
		deleteCategoryDropdown.setFont(GUIUtils.fontButton);
		
		buttonPanel.add(makeButtonPanelRow(deleteCategoryButton, deleteCategoryDropdown));
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
		
		// Otherwise, get the category
		CategoryGroup category = (CategoryGroup)createAccountDropdown.getSelectedItem();
		
		// If the dropdown had no categories, category will be null
		if (category == null) return;
		
		// Create an account window
		new CreateAccountWindow(this, category);
	}
	
	/**
	 * Attempt to create a new category
	 */
	private void createCategory () {
		// Finish if either categories cannot be edited or if the category name isn't valid
		if (!checkCanEditCategories() || !checkCreateCategoryNameIsValid()) return;
		
		// Show a "create new category" chooser
		int index = GUIUtils.showOptionChooser(
			this,
			"Are you sure you want to create a new category?\n" +
			"Categories cannot be renamed once created.\n" +
			createCategoryField.getText(),
			"Create New Category?",
			new String[] { "Create Category", "Cancel" },
			0);
		
		// If "Create Category" wasn't chosen, finish
		if (index != 0) return;
		
		// Create a new category and add it to the CGL
		cgl.categoryGroups.add(new CategoryGroup(createCategoryField.getText()));
		
		// Update the vault edited status
		vaultEdited();
	}
	
	private void deleteCategory () {
		// If categories cannot be edited right now, finish
		if (!checkCanEditCategories()) return;
		
		// Get the category
		CategoryGroup category = (CategoryGroup)deleteCategoryDropdown.getSelectedItem();
		
		// If there are no categories in the CGL, category will be null
		if (category == null) return;
		
		// Get the message to display in the chooser and the default index, based on whether or not
		// the category has one or more connected accounts
		String message;
		int defaultIndex;
		if (category.accountGroups.size() == 0) {
			// If there are no connected accounts, the default
			// option is "Delete Permanently" because nothing
			// too important is connected
			message =
				"Are you sure you want to delete this category?\n" +
				category.name;
			defaultIndex = 0;
		} else {
			// If there are 1 or more connected accounts, the
			// default option is "Cancel" to be extra safe
			message =
				"Are you sure you want to delete this category, which\n" +
				"has " + category.accountGroups.size() + " connected account(s)?\n" +
				category.name;
			defaultIndex = 1;
		}
		
		// Show the chooser
		int index = GUIUtils.showOptionChooser(
			this,
			message,
			"Delete Category",
			new String[] { "Delete Permanently", "Cancel" },
			defaultIndex);
		
		// If "Delete Permanently" was not chosen, finish
		if (index != 0) return;
		
		// Otherwise, remove the category from the CGL
		cgl.categoryGroups.remove(category);
		
		// Update the vault edited status
		vaultEdited();
	}
	
	private void printAllAccountInfo () {
		// Show the print chooser
		int index = GUIUtils.showOptionChooser(
			this,
			"Are you sure you want to print account information for\n" +
			"for all accounts in every category?",
			"Print All Account Info?",
			new String[] { "Print All Info", "Cancel" },
			1);
		
		// If "Print All Info" was not selected, finish
		if (index != 0) return;
		
		// Attempt to print the CGL
		try {
			CGLPrinter.printCGL(cgl);
		} catch (Exception e) {
			GUIUtils.showWarning(e);
		}
	}
	
	private boolean checkCanEditCategories () {
		// If there is no account window, categories may be edited
		if (accountWindow == null) return true;
		
		// Otherwise, show a warning
		GUIUtils.showWarning("You cannot edit categories while editing an account.");
		return false;
	}
	
	private boolean checkCreateCategoryNameIsValid () {
		// If the create category field's value isn't blank, it is valid
		if (!createCategoryField.getText().isBlank()) return true;
		
		// Otherwise, show a warning
		GUIUtils.showWarning("The name of the new category cannot be blank.");
		return false;
	}
	
	public static interface SaveFunction {
		abstract boolean save();
	}
	
}