
// Program.java, Gabriel Seaver, 2021

package safekeeper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import safekeeper.crypto.Crypto.AlgorithmException;
import safekeeper.crypto.Crypto.CorruptedVaultException;
import safekeeper.crypto.Crypto.IncorrectPasswordException;
import safekeeper.groupings.ServiceGroupList;
import safekeeper.groupings.ServiceGroupList.CorruptedSerializationException;
import safekeeper.gui.frames.MainWindow;
import safekeeper.gui.frames.SecurePasswordLogin;
import safekeeper.gui.frames.VaultFileSelector;
import safekeeper.gui.frames.VaultFileSelector.LoadOption;
import safekeeper.gui.util.GUIUtils;

public class Program {
	
	private File vaultFile;
	private String password;
	private MainWindow mainWindow;
	private ServiceGroupList sgl;
	
	public Program (String[] cmdArgs) {
		GUIUtils.setWindowsStyle();
		mainWindow = new MainWindow(this::saveSGLSafely);
		loadAndLogin(cmdArgs);
	}
	
	// Sets sgl, password, and vaultFile fields
	private void loadAndLogin (String[] cmdArgs) {
		
		// Chooses the load option
		LoadOption loadOption = VaultFileSelector.chooseCreateNewOrLoadExisting(mainWindow);
		
		if (loadOption == LoadOption.LOAD_EXISTING) {
			
			// Load an existing vault file
			getVaultFile(cmdArgs);
			loadSGLSafely();
			mainWindow.setSGL(sgl);
			
		} else if (loadOption == LoadOption.CREATE_NEW) {
			
			// Create a new vault file
			makeNewVaultFile();
			mainWindow.setSGL(sgl);
			mainWindow.vaultEdited();
			
		} else System.exit(0); // LoadOption.NONE selected
		
	}
	
	// Creates a new password vault file
	private void makeNewVaultFile () {
		// Get a new vault file
		vaultFile = VaultFileSelector.getNewVaultFile(mainWindow);
		
		// Exit if no vault file was created
		if (vaultFile == null) System.exit(0);
		
		// Make a new master password
		password = VaultFileSelector.makeNewMasterPassword(mainWindow);
		
		// Exit if no master password was set
		if (password == null) System.exit(0);
		
		// New SGL
		sgl = new ServiceGroupList();
	}
	
	private void saveServiceGroupList () throws IOException, AlgorithmException {
		
		// Gets the ciphertext to save and the file writer
		String ciphertext = sgl.getCryptoSerialized(password);
		FileWriter fileWriter = new FileWriter(vaultFile);
		
		// Attempt to save
		try {
			fileWriter.write(ciphertext);
		} finally {
			fileWriter.close();
		}
	}
	
	private boolean saveSGLSafely () {
		try {
			// Attempt to save SGL
			saveServiceGroupList();
			return true;
		} catch (Exception e) {
			GUIUtils.showWarning("Failed to save changes to password vault:\n" + e.toString());
			return false;
		}
	}
	
	private ServiceGroupList loadServiceGroupList (String submittedPassword)
			throws	AlgorithmException, CorruptedSerializationException, IncorrectPasswordException, CorruptedVaultException,
					IOException, NoSuchFileException, Exception {
		
		// Decrypt serial info and load SGL
		String decryptedSerialInfo = Files.readString(Paths.get(vaultFile.getAbsolutePath(), new String[0]));
		return ServiceGroupList.fromCryptoSerialized(submittedPassword, decryptedSerialInfo);
	}
	
	private void loadSGLSafely () {
		// Try to login
		try {
			password = SecurePasswordLogin.login(mainWindow, submittedPassword -> {
				// Try to load SGL given a password
				try {
					sgl = loadServiceGroupList(submittedPassword);
				} catch (AlgorithmException e) {
					GUIUtils.showFatalError(e);
					return false;
				} catch (NoSuchFileException e) {
					GUIUtils.showFatalError(
						"The password vault file can no longer be found.\n" +
						"Ensure that it has not been moved or deleted.");
					return false;
				} catch (CorruptedSerializationException e) {
					GUIUtils.showFatalError(e);
					return false;
				} catch (IOException e) {
					GUIUtils.showFatalError(e);
					return false;
				} catch (IncorrectPasswordException e) {
					// Do not show error, just return false because the password is not correct
					return false;
				} catch (CorruptedVaultException e) {
					GUIUtils.showFatalError(e);
				} catch (Exception e) {
					GUIUtils.showFatalError(
						"Something has gone wrong, but we're not sure what.\n" +
						"It is likely that the password vault is corrupted.\n" +
						e.getMessage());
					return false;
				}
				return true;
			});
		} catch (InterruptedException e) {
			GUIUtils.showFatalError(e);
		}
	}
	
	private void getVaultFile (String[] cmdArgs) {
		if (cmdArgs.length == 1) {
			// Vault file specified in args
			vaultFile = new File(cmdArgs[1]);
		} else {
			// Vault file not specified in args; select a vault file
			vaultFile = VaultFileSelector.selectVaultFile(mainWindow);
			
			// Exit if no vault file was chosen
			if (vaultFile == null) System.exit(0);
		}
		
		if (vaultFile.exists()) {
			if (!vaultFile.getName().endsWith("."+VaultFileSelector.EXTENSION))
				GUIUtils.showFatalError("The selected file is not an ."+VaultFileSelector.EXTENSION+" vault file.");
		} else {
			GUIUtils.showFatalError("The selected vault file does not exist.");
		}
	}
	
}