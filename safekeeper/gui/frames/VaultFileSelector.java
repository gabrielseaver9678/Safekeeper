package safekeeper.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import safekeeper.gui.components.PasswordField;
import safekeeper.gui.components.PasswordGeneratorPanel;
import safekeeper.gui.layout.MatchedPairsLayout;
import safekeeper.gui.util.GUIUtils;

public class VaultFileSelector {
  public static final String EXTENSION = "skvault";
  
  public enum LoadOption {
    CREATE_NEW, LOAD_EXISTING, NONE;
  }
  
  public static LoadOption chooseCreateNewOrLoadExisting(JFrame paramJFrame) {
    int i = GUIUtils.showOptionChooser(paramJFrame, "Load an existing Safekeeper vault file,\nor create a new one?", "Vault Loading Options", new String[] { "Load Existing", "Create New" }, 0);
    if (i == 1)
      return LoadOption.CREATE_NEW; 
    if (i == 0)
      return LoadOption.LOAD_EXISTING; 
    return LoadOption.NONE;
  }
  
  public static File selectVaultFile(JFrame paramJFrame) {
    JFileChooser jFileChooser = new JFileChooser();
    jFileChooser.setFileFilter(new FileNameExtensionFilter("Safekeeper Vault File (.skvault)", new String[] { "skvault" }));
    if (jFileChooser.showOpenDialog(paramJFrame) == 0)
      return jFileChooser.getSelectedFile(); 
    return null;
  }
  
  public static File getNewVaultFile(JFrame paramJFrame) {
    JFileChooser jFileChooser = new JFileChooser();
    jFileChooser.setFileFilter(new FileNameExtensionFilter("Safekeeper Vault File (.skvault)", new String[] { "skvault" }));
    if (jFileChooser.showSaveDialog(paramJFrame) == 0) {
      File file = jFileChooser.getSelectedFile();
      if (!file.getName().endsWith(".skvault"))
        return new File(file.getAbsolutePath() + ".skvault"); 
      return file;
    } 
    return null;
  }
  
  private static class ObjectHolder<T> {
    private T object;
    
    private ObjectHolder(T param1T) {
      this.object = param1T;
    }
  }
  
  private static final Color INVALID_MASTER_PASSWORD_COLOR = new Color(255, 200, 200);
  
  public static String makeNewMasterPassword(final JFrame parentFrame) {
    final CountDownLatch latch = new CountDownLatch(1);
    final ObjectHolder password = new ObjectHolder(null);
    JDialog jDialog = GUIUtils.makeNewDialog("Set Master Password", parentFrame, () -> paramCountDownLatch.countDown());
    JPanel jPanel1 = new JPanel(new BorderLayout());
    JLabel jLabel = GUIUtils.makeLabel("Your master password will be used to log into your password vault,\nso it is vital that it is very secure. Please use at least 15\ncharacters, including symbols, numbers, and letters. Your master\npassword should not include any words or names. It is recommended\nthat you write it down in a safe location, because if it is lost,\nit will be impossible to open your password vault.", false);
    jLabel.setBorder(GUIUtils.createMarginBorder(10));
    jPanel1.add(jLabel, "North");
    JPanel jPanel2 = new JPanel();
    MatchedPairsLayout matchedPairsLayout = new MatchedPairsLayout(jPanel2);
    jPanel2.setLayout((LayoutManager)matchedPairsLayout);
    jPanel2.setBorder(GUIUtils.createMarginBorder(10));
    final PasswordField passwordField = new PasswordField(1);
    matchedPairsLayout.addMatch(
        GUIUtils.makeLabel("New Master Password", true), (Container)passwordField1);
    final PasswordField passwordReentryField = new PasswordField(1);
    matchedPairsLayout.addMatch(
        GUIUtils.makeLabel("Password Re-Entry", true), (Container)passwordField2);
    final Runnable updatePasswordFieldBackground = new Runnable() {
        public void run() {
          boolean bool = VaultFileSelector.checkMasterPasswordValid(passwordField.getPassword(), false);
          Color color = bool ? Color.WHITE : VaultFileSelector.INVALID_MASTER_PASSWORD_COLOR;
          passwordField.setFieldBackground(color);
          passwordReentryField.setFieldBackground(color);
        }
      };
    passwordField1.getDocument().addDocumentListener(new DocumentListener() {
          public void insertUpdate(DocumentEvent param1DocumentEvent) {
            updatePasswordFieldBackground.run();
          }
          
          public void removeUpdate(DocumentEvent param1DocumentEvent) {
            updatePasswordFieldBackground.run();
          }
          
          public void changedUpdate(DocumentEvent param1DocumentEvent) {}
        });
    runnable.run();
    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent param1ActionEvent) {
          if (passwordField.getPassword().isEmpty() || 
            !VaultFileSelector.checkMasterPasswordValid(passwordField.getPassword(), true))
            return; 
          if (passwordReentryField.getPassword().isEmpty()) {
            GUIUtils.showWarning("You must re-enter the master password into the re-entry field.");
            return;
          } 
          if (!passwordField.getPassword().equals(passwordReentryField.getPassword())) {
            GUIUtils.showWarning("The master password and the re-entered password do not match.");
            return;
          } 
          int i = GUIUtils.showOptionChooser(parentFrame, "Are you sure you want this to be your master password,\nwhich will be used to access this Safekeeper password\nvault in the future?", "Confirm Master Password", new String[] { "Confirm", "Cancel" }, 0);
          if (i != 0)
            return; 
          password.object = (T)passwordField.getPassword();
          latch.countDown();
        }
      };
    passwordField1.addPasswordSubmittedListener(actionListener);
    passwordField2.addPasswordSubmittedListener(actionListener);
    matchedPairsLayout.addMatch(new JLabel(), 
        
        GUIUtils.makeStretchPanel(GUIUtils.makeButton("Submit", actionListener)));
    matchedPairsLayout.setAutoCreateGaps(true);
    matchedPairsLayout.finalizeLayout();
    jPanel1.add(jPanel2, "Center");
    ObjectHolder objectHolder2 = new ObjectHolder(null);
    objectHolder2.object = (T)new PasswordGeneratorPanel(12, 20, paramActionEvent -> {
          paramPasswordField.setPassword(((PasswordGeneratorPanel)paramObjectHolder.object).getPassword());
          paramPasswordField.setPasswordIsVisible(true);
        });
    jPanel1.add(GUIUtils.addMargin((Component)objectHolder2.object, 10), "South");
    jDialog.add(jPanel1);
    jDialog.pack();
    try {
      countDownLatch.await();
    } catch (Exception exception) {}
    jDialog.dispose();
    return (String)objectHolder1.object;
  }
  
  private static boolean checkMasterPasswordValid(String paramString, boolean paramBoolean) {
    if (paramString.length() < 12) {
      if (paramBoolean)
        GUIUtils.showWarning("The master password must be at least 12\ncharacters, 15 characters is recommended."); 
      return false;
    } 
    return true;
  }
}
