package safekeeper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import javax.swing.JFrame;
import safekeeper.crypto.Crypto;
import safekeeper.groupings.ServiceGroupList;
import safekeeper.gui.frames.MainWindow;
import safekeeper.gui.frames.SecurePasswordLogin;
import safekeeper.gui.frames.VaultFileSelector;
import safekeeper.gui.util.GUIUtils;

public class Program {
	private File vaultFile;
	
	private String password;
	
	private MainWindow mainWindow;
	
	private ServiceGroupList sgl;
	
	public Program(String[] paramArrayOfString) {
		GUIUtils.setWindowsStyle();
		this.mainWindow = new MainWindow(this::saveSGLSafely);
		loadAndLogin(paramArrayOfString);
	}
	
	private void loadAndLogin(String[] paramArrayOfString) {
		VaultFileSelector.LoadOption loadOption = VaultFileSelector.chooseCreateNewOrLoadExisting((JFrame)this.mainWindow);
		if (loadOption == VaultFileSelector.LoadOption.LOAD_EXISTING) {
			getVaultFile(paramArrayOfString);
			loadSGLSafely();
			this.mainWindow.setSGL(this.sgl);
		} else if (loadOption == VaultFileSelector.LoadOption.CREATE_NEW) {
			makeNewVaultFile();
			this.mainWindow.setSGL(this.sgl);
			this.mainWindow.vaultEdited();
		} else {
			System.exit(0);
		}
	}
	
	private void makeNewVaultFile() {
		this.vaultFile = VaultFileSelector.getNewVaultFile((JFrame)this.mainWindow);
		if (this.vaultFile == null)
			System.exit(0);
		this.password = VaultFileSelector.makeNewMasterPassword((JFrame)this.mainWindow);
		if (this.password == null)
			System.exit(0);
		this.sgl = new ServiceGroupList();
	}
	
	private static void saveServiceGroupList(File paramFile, String paramString, ServiceGroupList paramServiceGroupList) throws IOException, Crypto.AlgorithmException {
		String str = paramServiceGroupList.getCryptoSerialized(paramString);
		FileWriter fileWriter = new FileWriter(paramFile);
		try {
			fileWriter.write(str);
		} finally {
			fileWriter.close();
		}
	}
	
	private boolean saveSGLSafely() {
		try {
			saveServiceGroupList(this.vaultFile, this.password, this.sgl);
			return true;
		} catch (Exception exception) {
			GUIUtils.showWarning("Failed to save changes to password vault:\n" + exception.toString());
			return false;
		}
	}
	
	private static ServiceGroupList loadServiceGroupList(File paramFile, String paramString) throws Crypto.AlgorithmException, ServiceGroupList.CorruptedSerializationException, Crypto.IncorrectPasswordException, IOException, NoSuchFileException, Exception {
		String str = Files.readString(Paths.get(paramFile.getAbsolutePath(), new String[0]));
		return ServiceGroupList.fromCryptoSerialized(paramString, str);
	}
	
	private void loadSGLSafely() {
		try {
			this.password = SecurePasswordLogin.login((JFrame)this.mainWindow, paramString -> {
						try {
							this.sgl = loadServiceGroupList(this.vaultFile, paramString);
						} catch (safekeeper.crypto.Crypto.AlgorithmException algorithmException) {
							GUIUtils.showFatalError((Exception)algorithmException);
							return false;
						} catch (NoSuchFileException noSuchFileException) {
							GUIUtils.showFatalError("The password vault file can no longer be found.\nEnsure that it has not been moved or deleted.");
							return false;
						} catch (safekeeper.groupings.ServiceGroupList.CorruptedSerializationException corruptedSerializationException) {
							GUIUtils.showFatalError((Exception)corruptedSerializationException);
							return false;
						} catch (IOException iOException) {
							GUIUtils.showFatalError(iOException);
							return false;
						} catch (safekeeper.crypto.Crypto.IncorrectPasswordException incorrectPasswordException) {
							return false;
						} catch (Exception exception) {
							GUIUtils.showFatalError("Something has gone wrong, but we're not sure what.\nIt is likely that the password vault is corrupted.\n" + exception.getMessage());
							return false;
						}
						return true;
					});
		} catch (InterruptedException interruptedException) {
			GUIUtils.showFatalError(interruptedException);
		}
	}
	
	private void getVaultFile(String[] paramArrayOfString) {
		if (paramArrayOfString.length == 1) {
			this.vaultFile = new File(paramArrayOfString[1]);
		} else {
			this.vaultFile = VaultFileSelector.selectVaultFile((JFrame)this.mainWindow);
			if (this.vaultFile == null)
				System.exit(0);
		}
		if (this.vaultFile.exists()) {
			if (!this.vaultFile.getName().endsWith(".skvault"))
				GUIUtils.showFatalError("The selected file is not an .skvault vault file.");
		} else {
			GUIUtils.showFatalError("The selected vault file does not exist.");
		}
	}
}