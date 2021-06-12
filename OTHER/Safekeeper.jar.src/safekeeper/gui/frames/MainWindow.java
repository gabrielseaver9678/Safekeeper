/*     */ package safekeeper.gui.frames;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.event.TreeExpansionEvent;
/*     */ import javax.swing.event.TreeWillExpandListener;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.ExpandVetoException;
/*     */ import safekeeper.groupings.AccountGroup;
/*     */ import safekeeper.groupings.ServiceGroup;
/*     */ import safekeeper.groupings.ServiceGroupList;
/*     */ import safekeeper.gui.util.GUIUtils;
/*     */ import safekeeper.printing.SGLPrinter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MainWindow
/*     */   extends JFrame
/*     */ {
/*  50 */   AccountWindow accountWindow = null;
/*     */   
/*     */   private ServiceGroupList sgl;
/*     */   
/*     */   private SaveFunction saveFunction;
/*     */   
/*     */   private boolean vaultHasBeenEdited = false;
/*     */   
/*     */   private JPanel buttonPanel;
/*     */   
/*     */   private JTree serviceTree;
/*     */   
/*     */   private JScrollPane serviceScrollPane;
/*     */   
/*     */   public void vaultEdited() {
/*  65 */     this.vaultHasBeenEdited = true;
/*  66 */     recreateServiceTree();
/*  67 */     resetServiceDropdowns();
/*     */   }
/*     */   private JButton createAccountButton; private JButton createServiceButton; private JButton deleteServiceButton; private JButton printAllButton; private JTextField createServiceField; private JComboBox<ServiceGroup> createAccountDropdown; private JComboBox<ServiceGroup> deleteServiceDropdown;
/*     */   
/*     */   private class ServiceTreeRootNode extends DefaultMutableTreeNode { private ServiceTreeRootNode(ServiceGroupList param1ServiceGroupList) {
/*  72 */       super("Services");
/*     */ 
/*     */       
/*  75 */       Object[] arrayOfObject = param1ServiceGroupList.getServicesAlphabetical();
/*     */ 
/*     */       
/*  78 */       for (Object object : arrayOfObject) {
/*  79 */         add(new MainWindow.ServiceTreeServiceNode((ServiceGroup)object));
/*     */       }
/*     */       
/*  82 */       if (param1ServiceGroupList.serviceGroups.size() == 0) setAllowsChildren(false); 
/*     */     } }
/*     */ 
/*     */   
/*     */   private class ServiceTreeServiceNode extends DefaultMutableTreeNode {
/*     */     private ServiceTreeServiceNode(ServiceGroup param1ServiceGroup) {
/*  88 */       super(param1ServiceGroup.name);
/*     */ 
/*     */       
/*  91 */       Object[] arrayOfObject = param1ServiceGroup.getAccountsAlphabetical();
/*     */ 
/*     */       
/*  94 */       for (Object object : arrayOfObject) {
/*  95 */         add(new MainWindow.ServiceTreeAccountNode((AccountGroup)object));
/*     */       }
/*     */       
/*  98 */       if (param1ServiceGroup.accountGroups.size() == 0) setAllowsChildren(false); 
/*     */     } }
/*     */   
/*     */   private class ServiceTreeAccountNode extends DefaultMutableTreeNode {
/*     */     private AccountGroup account;
/*     */     
/*     */     private ServiceTreeAccountNode(AccountGroup param1AccountGroup) {
/* 105 */       super(param1AccountGroup.username);
/* 106 */       this.account = param1AccountGroup;
/*     */     }
/*     */     private void displayAccountWindow() {
/* 109 */       if (MainWindow.this.accountWindow == null) new DisplayAccountWindow(MainWindow.this, this.account);
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MainWindow(SaveFunction paramSaveFunction) {
/* 122 */     super("Safekeeper");
/*     */     
/* 124 */     this.saveFunction = paramSaveFunction;
/*     */ 
/*     */     
/* 127 */     this.serviceScrollPane = new JScrollPane();
/*     */     
/* 129 */     this.serviceScrollPane.setBorder(GUIUtils.createMarginBorder(10));
/* 130 */     this.serviceScrollPane.setVerticalScrollBarPolicy(22);
/* 131 */     this.serviceScrollPane.setHorizontalScrollBarPolicy(31);
/*     */     
/* 133 */     GUIUtils.setSize(this.serviceScrollPane, 300, 300);
/*     */     
/* 135 */     initializeServiceTree();
/*     */     
/* 137 */     createMenuBar();
/* 138 */     createButtonPanel();
/*     */ 
/*     */     
/* 141 */     add(this.serviceScrollPane, "North");
/* 142 */     add(this.buttonPanel, "South");
/* 143 */     pack();
/*     */     
/* 145 */     setDefaultCloseOperation(0);
/* 146 */     GUIUtils.stylizeWindow(this, null, this::onClosing);
/*     */   }
/*     */   
/*     */   public void setSGL(ServiceGroupList paramServiceGroupList) {
/* 150 */     this.sgl = paramServiceGroupList;
/* 151 */     setUIEnabled(true);
/* 152 */     recreateServiceTree();
/* 153 */     resetServiceDropdowns();
/*     */   }
/*     */   
/*     */   private void recreateServiceTree() {
/* 157 */     DefaultTreeModel defaultTreeModel = (DefaultTreeModel)this.serviceTree.getModel();
/* 158 */     ((DefaultMutableTreeNode)defaultTreeModel.getRoot()).removeAllChildren();
/* 159 */     defaultTreeModel.setRoot(new ServiceTreeRootNode(this.sgl));
/* 160 */     defaultTreeModel.reload();
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetServiceDropdowns() {
/* 165 */     this.createAccountDropdown.removeAllItems();
/* 166 */     this.deleteServiceDropdown.removeAllItems();
/*     */ 
/*     */     
/* 169 */     Object[] arrayOfObject = this.sgl.getServicesAlphabetical();
/*     */ 
/*     */     
/* 172 */     for (byte b = 0; b < arrayOfObject.length; b++) {
/* 173 */       ServiceGroup serviceGroup = (ServiceGroup)arrayOfObject[b];
/* 174 */       this.createAccountDropdown.addItem(serviceGroup);
/* 175 */       this.deleteServiceDropdown.addItem(serviceGroup);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setUIEnabled(boolean paramBoolean) {
/* 180 */     this.createAccountButton.setEnabled(paramBoolean);
/* 181 */     this.createAccountDropdown.setEnabled(paramBoolean);
/* 182 */     this.createServiceButton.setEnabled(paramBoolean);
/* 183 */     this.createServiceField.setEnabled(paramBoolean);
/* 184 */     this.deleteServiceButton.setEnabled(paramBoolean);
/* 185 */     this.deleteServiceDropdown.setEnabled(paramBoolean);
/* 186 */     this.printAllButton.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeServiceTree() {
/* 192 */     DefaultTreeModel defaultTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
/* 193 */     defaultTreeModel.setAsksAllowsChildren(true);
/*     */ 
/*     */     
/* 196 */     this.serviceTree = new JTree(defaultTreeModel);
/* 197 */     this.serviceTree.setFont(GUIUtils.font);
/* 198 */     this.serviceTree.setOpaque(false);
/* 199 */     this.serviceTree.setToggleClickCount(1);
/*     */     
/* 201 */     this.serviceTree.addTreeWillExpandListener(new TreeWillExpandListener()
/*     */         {
/*     */           
/*     */           public void treeWillExpand(TreeExpansionEvent param1TreeExpansionEvent) throws ExpandVetoException
/*     */           {
/* 206 */             DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)param1TreeExpansionEvent.getPath().getLastPathComponent();
/* 207 */             if (defaultMutableTreeNode instanceof MainWindow.ServiceTreeAccountNode) {
/*     */               
/* 209 */               ((MainWindow.ServiceTreeAccountNode)defaultMutableTreeNode).displayAccountWindow();
/*     */               
/* 211 */               throw new ExpandVetoException(param1TreeExpansionEvent, "Account node expansion veto");
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void treeWillCollapse(TreeExpansionEvent param1TreeExpansionEvent) throws ExpandVetoException {
/* 218 */             if (((DefaultMutableTreeNode)param1TreeExpansionEvent.getPath().getLastPathComponent()).isRoot()) {
/* 219 */               throw new ExpandVetoException(param1TreeExpansionEvent, "Root node cannot collapse");
/*     */             }
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 225 */     DefaultTreeCellRenderer defaultTreeCellRenderer = (DefaultTreeCellRenderer)this.serviceTree.getCellRenderer();
/* 226 */     defaultTreeCellRenderer.setClosedIcon(null);
/* 227 */     defaultTreeCellRenderer.setOpenIcon(null);
/* 228 */     defaultTreeCellRenderer.setLeafIcon(null);
/* 229 */     defaultTreeCellRenderer.setBackgroundNonSelectionColor(getBackground());
/* 230 */     this.serviceTree.setCellRenderer(defaultTreeCellRenderer);
/*     */     
/* 232 */     this.serviceScrollPane.setViewportView(this.serviceTree);
/*     */   }
/*     */   
/*     */   private void onClosing() {
/* 236 */     if (this.vaultHasBeenEdited) {
/*     */       
/* 238 */       boolean bool = this.saveFunction.save();
/*     */ 
/*     */       
/* 241 */       if (bool) {
/* 242 */         GUIUtils.showMessage(this, "Changes to your Safekeeper vault have been saved.", "Changes Saved");
/*     */ 
/*     */ 
/*     */         
/* 246 */         System.exit(0);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 251 */       System.exit(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createMenuBar() {
/* 256 */     JMenuBar jMenuBar = new JMenuBar();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     JMenu jMenu1 = new JMenu("Help");
/* 262 */     jMenuBar.add(jMenu1);
/* 263 */     jMenu1.add(new JMenuItem("Show User Guide"));
/*     */ 
/*     */     
/* 266 */     JMenu jMenu2 = new JMenu("Edit");
/* 267 */     jMenuBar.add(jMenu2);
/*     */     
/* 269 */     setJMenuBar(jMenuBar);
/*     */   }
/*     */ 
/*     */   
/*     */   private void createButtonPanel() {
/* 274 */     this.buttonPanel = new JPanel();
/* 275 */     this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 1));
/* 276 */     this.buttonPanel.setBorder(GUIUtils.createMarginBorder(10));
/*     */ 
/*     */     
/* 279 */     JPanel jPanel1 = new JPanel(new BorderLayout());
/* 280 */     this.createAccountButton = GUIUtils.makeButton("Create New Account", paramActionEvent -> createAccount());
/* 281 */     jPanel1.add(this.createAccountButton, "West");
/*     */ 
/*     */     
/* 284 */     this.createAccountDropdown = new JComboBox<>();
/* 285 */     this.createAccountDropdown.setFont(GUIUtils.fontButton);
/* 286 */     jPanel1.add(this.createAccountDropdown, "Center");
/*     */     
/* 288 */     this.buttonPanel.add(jPanel1);
/* 289 */     this.buttonPanel.add(GUIUtils.makeVerticalStrut(10));
/*     */ 
/*     */     
/* 292 */     JPanel jPanel2 = new JPanel(new BorderLayout());
/* 293 */     this.createServiceButton = GUIUtils.makeButton("Create New Service", paramActionEvent -> createService());
/* 294 */     jPanel2.add(this.createServiceButton, "West");
/*     */ 
/*     */     
/* 297 */     this.createServiceField = GUIUtils.makeTextField(true);
/* 298 */     this.createServiceField.addActionListener(paramActionEvent -> createService());
/* 299 */     jPanel2.add(this.createServiceField, "Center");
/*     */     
/* 301 */     this.buttonPanel.add(jPanel2);
/* 302 */     this.buttonPanel.add(GUIUtils.makeVerticalStrut(10));
/*     */ 
/*     */     
/* 305 */     JPanel jPanel3 = new JPanel(new BorderLayout());
/* 306 */     this.deleteServiceButton = GUIUtils.makeButton("Delete Service", paramActionEvent -> deleteService());
/* 307 */     this.deleteServiceButton.setEnabled(false);
/* 308 */     jPanel3.add(this.deleteServiceButton, "West");
/*     */ 
/*     */     
/* 311 */     this.deleteServiceDropdown = new JComboBox<>();
/* 312 */     this.deleteServiceDropdown.setFont(GUIUtils.fontButton);
/* 313 */     this.deleteServiceDropdown.setEnabled(false);
/* 314 */     jPanel3.add(this.deleteServiceDropdown, "Center");
/*     */     
/* 316 */     this.buttonPanel.add(jPanel3);
/* 317 */     this.buttonPanel.add(GUIUtils.makeVerticalStrut(10));
/*     */ 
/*     */     
/* 320 */     this.printAllButton = GUIUtils.makeButton("Print All Account Information", paramActionEvent -> printAllAccountInfo());
/* 321 */     this.buttonPanel.add(GUIUtils.makeStretchPanel(this.printAllButton));
/*     */     
/* 323 */     setUIEnabled(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createAccount() {
/* 329 */     if (this.accountWindow != null) {
/* 330 */       GUIUtils.showWarning("Cannot create a new account while\nediting another.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 335 */     ServiceGroup serviceGroup = (ServiceGroup)this.createAccountDropdown.getSelectedItem();
/* 336 */     if (serviceGroup == null)
/* 337 */       return;  CreateAccountWindow.newWindow(this, serviceGroup);
/*     */   }
/*     */ 
/*     */   
/*     */   private void createService() {
/* 342 */     if (!checkCanEditServices() || !checkCreateServiceNameIsValid()) {
/*     */       return;
/*     */     }
/* 345 */     int i = GUIUtils.showOptionChooser(this, "Are you sure you want to create a new service?\nServices cannot be renamed once created.\n" + this.createServiceField
/*     */ 
/*     */ 
/*     */         
/* 349 */         .getText(), "Create New Service?", new String[] { "Create Service", "Cancel" }, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     if (i != 0) {
/*     */       return;
/*     */     }
/* 358 */     this.sgl.serviceGroups.add(new ServiceGroup(this.createServiceField.getText()));
/* 359 */     vaultEdited();
/*     */   }
/*     */ 
/*     */   
/*     */   private void deleteService() {
/* 364 */     if (!checkCanEditServices()) {
/*     */       return;
/*     */     }
/* 367 */     ServiceGroup serviceGroup = (ServiceGroup)this.deleteServiceDropdown.getSelectedItem();
/* 368 */     if (serviceGroup == null)
/* 369 */       return;  if (serviceGroup.accountGroups.size() == 0) {
/*     */       
/* 371 */       int i = GUIUtils.showOptionChooser(this, "Are you sure you want to delete this service?\n" + serviceGroup.name, "Delete Service", new String[] { "Delete Permanently", "Cancel" }, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 379 */       if (i != 0)
/*     */         return; 
/*     */     } else {
/* 382 */       int i = GUIUtils.showOptionChooser(this, "Are you sure you want to delete this service, which\nhas " + serviceGroup.accountGroups
/*     */ 
/*     */           
/* 385 */           .size() + " connected account(s)?", "Delete Service", new String[] { "Delete Permanently", "Cancel" }, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 391 */       if (i != 0) {
/*     */         return;
/*     */       }
/*     */     } 
/* 395 */     this.sgl.serviceGroups.remove(serviceGroup);
/* 396 */     vaultEdited();
/*     */   }
/*     */   
/*     */   private void printAllAccountInfo() {
/* 400 */     int i = GUIUtils.showOptionChooser(this, "Are you sure you want to print all account information,\nincluding passwords, usernames, and email addresses, for\nfor all accounts in every service?", "Print All Account Info?", new String[] { "Print All Info", "Cancel" }, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 410 */     if (i != 0) {
/*     */       return;
/*     */     }
/*     */     try {
/* 414 */       SGLPrinter.printSGL(this.sgl);
/* 415 */     } catch (Exception exception) {
/* 416 */       GUIUtils.showWarning(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkCanEditServices() {
/* 424 */     if (this.accountWindow == null) return true;
/*     */ 
/*     */     
/* 427 */     GUIUtils.showWarning("You cannot edit services while editing an account.");
/* 428 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkCreateServiceNameIsValid() {
/* 435 */     if (!this.createServiceField.getText().isBlank()) return true;
/*     */ 
/*     */     
/* 438 */     GUIUtils.showWarning("The name of the service cannot be blank.");
/* 439 */     return false;
/*     */   }
/*     */   
/*     */   public static interface SaveFunction {
/*     */     boolean save();
/*     */   }
/*     */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\frames\MainWindow.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */