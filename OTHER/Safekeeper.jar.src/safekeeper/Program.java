/*     */ package safekeeper;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Paths;
/*     */ import javax.swing.JFrame;
/*     */ import safekeeper.crypto.Crypto;
/*     */ import safekeeper.groupings.ServiceGroupList;
/*     */ import safekeeper.gui.frames.MainWindow;
/*     */ import safekeeper.gui.frames.SecurePasswordLogin;
/*     */ import safekeeper.gui.frames.VaultFileSelector;
/*     */ import safekeeper.gui.util.GUIUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Program
/*     */ {
/*     */   private File vaultFile;
/*     */   private String password;
/*     */   private MainWindow mainWindow;
/*     */   private ServiceGroupList sgl;
/*     */   
/*     */   public Program(String[] paramArrayOfString) {
/*  51 */     GUIUtils.setWindowsStyle();
/*     */     
/*  53 */     this.mainWindow = new MainWindow(this::saveSGLSafely);
/*  54 */     loadAndLogin(paramArrayOfString);
/*     */   }
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
/*     */   private void loadAndLogin(String[] paramArrayOfString) {
/*  75 */     VaultFileSelector.LoadOption loadOption = VaultFileSelector.chooseCreateNewOrLoadExisting((JFrame)this.mainWindow);
/*     */     
/*  77 */     if (loadOption == VaultFileSelector.LoadOption.LOAD_EXISTING)
/*  78 */     { getVaultFile(paramArrayOfString);
/*  79 */       loadSGLSafely();
/*  80 */       this.mainWindow.setSGL(this.sgl); }
/*  81 */     else if (loadOption == VaultFileSelector.LoadOption.CREATE_NEW)
/*  82 */     { makeNewVaultFile();
/*  83 */       this.mainWindow.setSGL(this.sgl);
/*  84 */       this.mainWindow.vaultEdited(); }
/*  85 */     else { System.exit(0); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void makeNewVaultFile() {
/*  91 */     this.vaultFile = VaultFileSelector.getNewVaultFile((JFrame)this.mainWindow);
/*  92 */     if (this.vaultFile == null) System.exit(0);
/*     */ 
/*     */     
/*  95 */     this.password = VaultFileSelector.makeNewMasterPassword((JFrame)this.mainWindow);
/*  96 */     if (this.password == null) System.exit(0);
/*     */     
/*  98 */     this.sgl = new ServiceGroupList();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void saveServiceGroupList(File paramFile, String paramString, ServiceGroupList paramServiceGroupList) throws IOException, Crypto.AlgorithmException {
/* 103 */     String str = paramServiceGroupList.getCryptoSerialized(paramString);
/* 104 */     FileWriter fileWriter = new FileWriter(paramFile);
/*     */     
/*     */     try {
/* 107 */       fileWriter.write(str);
/*     */     } finally {
/* 109 */       fileWriter.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean saveSGLSafely() {
/*     */     try {
/* 115 */       saveServiceGroupList(this.vaultFile, this.password, this.sgl);
/* 116 */       return true;
/* 117 */     } catch (Exception exception) {
/* 118 */       GUIUtils.showWarning("Failed to save changes to password vault:\n" + exception.toString());
/* 119 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ServiceGroupList loadServiceGroupList(File paramFile, String paramString) throws Crypto.AlgorithmException, ServiceGroupList.CorruptedSerializationException, Crypto.IncorrectPasswordException, IOException, NoSuchFileException, Exception {
/* 127 */     String str = Files.readString(Paths.get(paramFile.getAbsolutePath(), new String[0]));
/* 128 */     return ServiceGroupList.fromCryptoSerialized(paramString, str);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadSGLSafely() {
/*     */     try {
/* 135 */       this.password = SecurePasswordLogin.login((JFrame)this.mainWindow, paramString -> {
/*     */             try {
/*     */               this.sgl = loadServiceGroupList(this.vaultFile, paramString);
/* 138 */             } catch (safekeeper.crypto.Crypto.AlgorithmException algorithmException) {
/*     */               GUIUtils.showFatalError((Exception)algorithmException);
/*     */               return false;
/* 141 */             } catch (NoSuchFileException noSuchFileException) {
/*     */               GUIUtils.showFatalError("The password vault file can no longer be found.\nEnsure that it has not been moved or deleted.");
/*     */ 
/*     */               
/*     */               return false;
/* 146 */             } catch (safekeeper.groupings.ServiceGroupList.CorruptedSerializationException corruptedSerializationException) {
/*     */               GUIUtils.showFatalError((Exception)corruptedSerializationException);
/*     */               return false;
/* 149 */             } catch (IOException iOException) {
/*     */               GUIUtils.showFatalError(iOException);
/*     */               return false;
/* 152 */             } catch (safekeeper.crypto.Crypto.IncorrectPasswordException incorrectPasswordException) {
/*     */               return false;
/* 154 */             } catch (Exception exception) {
/*     */               GUIUtils.showFatalError("Something has gone wrong, but we're not sure what.\nIt is likely that the password vault is corrupted.\n" + exception.getMessage());
/*     */ 
/*     */               
/*     */               return false;
/*     */             } 
/*     */ 
/*     */             
/*     */             return true;
/*     */           });
/* 164 */     } catch (InterruptedException interruptedException) {
/* 165 */       GUIUtils.showFatalError(interruptedException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getVaultFile(String[] paramArrayOfString) {
/* 173 */     if (paramArrayOfString.length == 1) {
/*     */       
/* 175 */       this.vaultFile = new File(paramArrayOfString[1]);
/*     */     } else {
/* 177 */       this.vaultFile = VaultFileSelector.selectVaultFile((JFrame)this.mainWindow);
/* 178 */       if (this.vaultFile == null) System.exit(0);
/*     */     
/*     */     } 
/*     */     
/* 182 */     if (this.vaultFile.exists())
/* 183 */     { if (!this.vaultFile.getName().endsWith(".skvault"))
/* 184 */         GUIUtils.showFatalError("The selected file is not an .skvault vault file.");  }
/* 185 */     else { GUIUtils.showFatalError("The selected vault file does not exist."); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\Program.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */