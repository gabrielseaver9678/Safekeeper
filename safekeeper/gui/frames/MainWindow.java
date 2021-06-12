package safekeeper.gui.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	
	private JScrollPane serviceScrollPane;
	
	private JButton createAccountButton;
	
	private JButton createServiceButton;
	
	private JButton deleteServiceButton;
	
	private JButton printAllButton;
	
	private JTextField createServiceField;
	
	private JComboBox<ServiceGroup> createAccountDropdown;
	
	private JComboBox<ServiceGroup> deleteServiceDropdown;
	
	public void vaultEdited() {
		this.vaultHasBeenEdited = true;
		recreateServiceTree();
		resetServiceDropdowns();
	}
	
	private class ServiceTreeRootNode extends DefaultMutableTreeNode {
		private ServiceTreeRootNode(ServiceGroupList param1ServiceGroupList) {
			super("Services");
			Object[] arrayOfObject = param1ServiceGroupList.getServicesAlphabetical();
			for (Object object : arrayOfObject)
				add(new MainWindow.ServiceTreeServiceNode((ServiceGroup)object));
			if (param1ServiceGroupList.serviceGroups.size() == 0)
				setAllowsChildren(false);
		}
	}
	
	private class ServiceTreeServiceNode extends DefaultMutableTreeNode {
		private ServiceTreeServiceNode(ServiceGroup param1ServiceGroup) {
			super(param1ServiceGroup.name);
			Object[] arrayOfObject = param1ServiceGroup.getAccountsAlphabetical();
			for (Object object : arrayOfObject)
				add(new MainWindow.ServiceTreeAccountNode((AccountGroup)object));
			if (param1ServiceGroup.accountGroups.size() == 0)
				setAllowsChildren(false);
		}
	}
	
	private class ServiceTreeAccountNode extends DefaultMutableTreeNode {
		private AccountGroup account;
		
		private ServiceTreeAccountNode(AccountGroup param1AccountGroup) {
			super(param1AccountGroup.username);
			this.account = param1AccountGroup;
		}
		
		private void displayAccountWindow() {
			if (MainWindow.this.accountWindow == null)
				new DisplayAccountWindow(MainWindow.this, this.account);
		}
	}
	
	public MainWindow(SaveFunction paramSaveFunction) {
		super("Safekeeper");
		this.saveFunction = paramSaveFunction;
		this.serviceScrollPane = new JScrollPane();
		this.serviceScrollPane.setBorder(GUIUtils.createMarginBorder(10));
		this.serviceScrollPane.setVerticalScrollBarPolicy(22);
		this.serviceScrollPane.setHorizontalScrollBarPolicy(31);
		GUIUtils.setSize(this.serviceScrollPane, 300, 300);
		initializeServiceTree();
		createMenuBar();
		createButtonPanel();
		add(this.serviceScrollPane, "North");
		add(this.buttonPanel, "South");
		pack();
		setDefaultCloseOperation(0);
		GUIUtils.stylizeWindow(this, null, this::onClosing);
	}
	
	public void setSGL(ServiceGroupList paramServiceGroupList) {
		this.sgl = paramServiceGroupList;
		setUIEnabled(true);
		recreateServiceTree();
		resetServiceDropdowns();
	}
	
	private void recreateServiceTree() {
		DefaultTreeModel defaultTreeModel = (DefaultTreeModel)this.serviceTree.getModel();
		((DefaultMutableTreeNode)defaultTreeModel.getRoot()).removeAllChildren();
		defaultTreeModel.setRoot(new ServiceTreeRootNode(this.sgl));
		defaultTreeModel.reload();
	}
	
	private void resetServiceDropdowns() {
		this.createAccountDropdown.removeAllItems();
		this.deleteServiceDropdown.removeAllItems();
		Object[] arrayOfObject = this.sgl.getServicesAlphabetical();
		for (byte b = 0; b < arrayOfObject.length; b++) {
			ServiceGroup serviceGroup = (ServiceGroup)arrayOfObject[b];
			this.createAccountDropdown.addItem(serviceGroup);
			this.deleteServiceDropdown.addItem(serviceGroup);
		}
	}
	
	private void setUIEnabled(boolean paramBoolean) {
		this.createAccountButton.setEnabled(paramBoolean);
		this.createAccountDropdown.setEnabled(paramBoolean);
		this.createServiceButton.setEnabled(paramBoolean);
		this.createServiceField.setEnabled(paramBoolean);
		this.deleteServiceButton.setEnabled(paramBoolean);
		this.deleteServiceDropdown.setEnabled(paramBoolean);
		this.printAllButton.setEnabled(paramBoolean);
	}
	
	private void initializeServiceTree() {
		DefaultTreeModel defaultTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
		defaultTreeModel.setAsksAllowsChildren(true);
		this.serviceTree = new JTree(defaultTreeModel);
		this.serviceTree.setFont(GUIUtils.font);
		this.serviceTree.setOpaque(false);
		this.serviceTree.setToggleClickCount(1);
		this.serviceTree.addTreeWillExpandListener(new TreeWillExpandListener() {
			@Override
					public void treeWillExpand(TreeExpansionEvent param1TreeExpansionEvent) throws ExpandVetoException {
						DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)param1TreeExpansionEvent.getPath().getLastPathComponent();
						if (defaultMutableTreeNode instanceof MainWindow.ServiceTreeAccountNode) {
							((MainWindow.ServiceTreeAccountNode)defaultMutableTreeNode).displayAccountWindow();
							throw new ExpandVetoException(param1TreeExpansionEvent, "Account node expansion veto");
						}
					}
			
			@Override
					public void treeWillCollapse(TreeExpansionEvent param1TreeExpansionEvent) throws ExpandVetoException {
						if (((DefaultMutableTreeNode)param1TreeExpansionEvent.getPath().getLastPathComponent()).isRoot())
							throw new ExpandVetoException(param1TreeExpansionEvent, "Root node cannot collapse");
					}
				});
		DefaultTreeCellRenderer defaultTreeCellRenderer = (DefaultTreeCellRenderer)this.serviceTree.getCellRenderer();
		defaultTreeCellRenderer.setClosedIcon(null);
		defaultTreeCellRenderer.setOpenIcon(null);
		defaultTreeCellRenderer.setLeafIcon(null);
		defaultTreeCellRenderer.setBackgroundNonSelectionColor(getBackground());
		this.serviceTree.setCellRenderer(defaultTreeCellRenderer);
		this.serviceScrollPane.setViewportView(this.serviceTree);
	}
	
	private void onClosing() {
		if (this.vaultHasBeenEdited) {
			boolean bool = this.saveFunction.save();
			if (bool) {
				GUIUtils.showMessage(this, "Changes to your Safekeeper vault have been saved.", "Changes Saved");
				System.exit(0);
			}
		} else {
			System.exit(0);
		}
	}
	
	private void createMenuBar() {
		JMenuBar jMenuBar = new JMenuBar();
		JMenu jMenu1 = new JMenu("Help");
		jMenuBar.add(jMenu1);
		jMenu1.add(new JMenuItem("Show User Guide"));
		JMenu jMenu2 = new JMenu("Edit");
		jMenuBar.add(jMenu2);
		setJMenuBar(jMenuBar);
	}
	
	private void createButtonPanel() {
		this.buttonPanel = new JPanel();
		this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 1));
		this.buttonPanel.setBorder(GUIUtils.createMarginBorder(10));
		JPanel jPanel1 = new JPanel(new BorderLayout());
		this.createAccountButton = GUIUtils.makeButton("Create New Account", paramActionEvent -> createAccount());
		jPanel1.add(this.createAccountButton, "West");
		this.createAccountDropdown = new JComboBox<>();
		this.createAccountDropdown.setFont(GUIUtils.fontButton);
		jPanel1.add(this.createAccountDropdown, "Center");
		this.buttonPanel.add(jPanel1);
		this.buttonPanel.add(GUIUtils.makeVerticalStrut(10));
		JPanel jPanel2 = new JPanel(new BorderLayout());
		this.createServiceButton = GUIUtils.makeButton("Create New Service", paramActionEvent -> createService());
		jPanel2.add(this.createServiceButton, "West");
		this.createServiceField = GUIUtils.makeTextField(true);
		this.createServiceField.addActionListener(paramActionEvent -> createService());
		jPanel2.add(this.createServiceField, "Center");
		this.buttonPanel.add(jPanel2);
		this.buttonPanel.add(GUIUtils.makeVerticalStrut(10));
		JPanel jPanel3 = new JPanel(new BorderLayout());
		this.deleteServiceButton = GUIUtils.makeButton("Delete Service", paramActionEvent -> deleteService());
		this.deleteServiceButton.setEnabled(false);
		jPanel3.add(this.deleteServiceButton, "West");
		this.deleteServiceDropdown = new JComboBox<>();
		this.deleteServiceDropdown.setFont(GUIUtils.fontButton);
		this.deleteServiceDropdown.setEnabled(false);
		jPanel3.add(this.deleteServiceDropdown, "Center");
		this.buttonPanel.add(jPanel3);
		this.buttonPanel.add(GUIUtils.makeVerticalStrut(10));
		this.printAllButton = GUIUtils.makeButton("Print All Account Information", paramActionEvent -> printAllAccountInfo());
		this.buttonPanel.add(GUIUtils.makeStretchPanel(this.printAllButton));
		setUIEnabled(false);
	}
	
	private void createAccount() {
		if (this.accountWindow != null) {
			GUIUtils.showWarning("Cannot create a new account while\nediting another.");
			return;
		}
		ServiceGroup serviceGroup = (ServiceGroup)this.createAccountDropdown.getSelectedItem();
		if (serviceGroup == null)
			return;
		new CreateAccountWindow(this, serviceGroup);
	}
	
	private void createService() {
		if (!checkCanEditServices() || !checkCreateServiceNameIsValid())
			return;
		int i = GUIUtils.showOptionChooser(this, "Are you sure you want to create a new service?\nServices cannot be renamed once created.\n" + this.createServiceField
				
				.getText(), "Create New Service?", new String[] { "Create Service", "Cancel" }, 0);
		if (i != 0)
			return;
		this.sgl.serviceGroups.add(new ServiceGroup(this.createServiceField.getText()));
		vaultEdited();
	}
	
	private void deleteService() {
		if (!checkCanEditServices())
			return;
		ServiceGroup serviceGroup = (ServiceGroup)this.deleteServiceDropdown.getSelectedItem();
		if (serviceGroup == null)
			return;
		if (serviceGroup.accountGroups.size() == 0) {
			int i = GUIUtils.showOptionChooser(this, "Are you sure you want to delete this service?\n" + serviceGroup.name, "Delete Service", new String[] { "Delete Permanently", "Cancel" }, 0);
			if (i != 0)
				return;
		} else {
			int i = GUIUtils.showOptionChooser(this, "Are you sure you want to delete this service, which\nhas " + serviceGroup.accountGroups
					
					.size() + " connected account(s)?", "Delete Service", new String[] { "Delete Permanently", "Cancel" }, 1);
			if (i != 0)
				return;
		}
		this.sgl.serviceGroups.remove(serviceGroup);
		vaultEdited();
	}
	
	private void printAllAccountInfo() {
		int i = GUIUtils.showOptionChooser(this, "Are you sure you want to print all account information,\nincluding passwords, usernames, and email addresses, for\nfor all accounts in every service?", "Print All Account Info?", new String[] { "Print All Info", "Cancel" }, 1);
		if (i != 0)
			return;
		try {
			SGLPrinter.printSGL(this.sgl);
		} catch (Exception exception) {
			GUIUtils.showWarning(exception);
		}
	}
	
	private boolean checkCanEditServices() {
		if (this.accountWindow == null)
			return true;
		GUIUtils.showWarning("You cannot edit services while editing an account.");
		return false;
	}
	
	private boolean checkCreateServiceNameIsValid() {
		if (!this.createServiceField.getText().isBlank())
			return true;
		GUIUtils.showWarning("The name of the service cannot be blank.");
		return false;
	}
	
	public static interface SaveFunction {
		boolean save();
	}
}
