/*     */ package safekeeper.crypto;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Base64;
/*     */ import javax.crypto.AEADBadTagException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.spec.GCMParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
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
/*     */ public class Crypto
/*     */ {
/*     */   private static final int secretKeyHashIterations = 1000000;
/*     */   private static final String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
/*     */   private static final String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
/*     */   private static final String numberChars = "1234567890";
/*     */   private static final String symbolChars = "!@#$%&*?^";
/*     */   private static final double dayCombos = 8.64E13D;
/*     */   
/*     */   public static class AlgorithmException
/*     */     extends Exception
/*     */   {
/*     */     public AlgorithmException(String param1String) {
/*  38 */       super(param1String);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class IncorrectPasswordException extends Exception {
/*     */     public IncorrectPasswordException(String param1String) {
/*  44 */       super(param1String);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static SecretKeySpec getSecretKey(byte[] paramArrayOfbyte, String paramString) throws NoSuchAlgorithmException {
/*  50 */     byte[] arrayOfByte1 = paramString.getBytes(StandardCharsets.UTF_8);
/*     */     
/*  52 */     MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
/*     */     
/*  54 */     byte[] arrayOfByte2 = new byte[32];
/*     */     
/*  56 */     for (byte b = 0; b < 1000000; b++) {
/*     */       
/*  58 */       messageDigest.update(paramArrayOfbyte);
/*  59 */       messageDigest.update(arrayOfByte1);
/*  60 */       messageDigest.update(arrayOfByte2);
/*  61 */       arrayOfByte2 = messageDigest.digest();
/*     */     } 
/*     */ 
/*     */     
/*  65 */     return new SecretKeySpec(arrayOfByte2, "AES");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encrypt(String paramString, byte[] paramArrayOfbyte) throws AlgorithmException {
/*  75 */     SecureRandom secureRandom = new SecureRandom();
/*     */     
/*  77 */     byte[] arrayOfByte1 = new byte[32];
/*  78 */     secureRandom.nextBytes(arrayOfByte1);
/*     */     
/*  80 */     byte[] arrayOfByte2 = new byte[12];
/*  81 */     secureRandom.nextBytes(arrayOfByte2);
/*     */ 
/*     */     
/*     */     try {
/*  85 */       SecretKeySpec secretKeySpec = getSecretKey(arrayOfByte1, paramString);
/*     */       
/*  87 */       Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
/*  88 */       cipher.init(1, secretKeySpec, new GCMParameterSpec(128, arrayOfByte2));
/*     */       
/*  90 */       byte[] arrayOfByte = cipher.doFinal(paramArrayOfbyte);
/*     */       
/*  92 */       String str1 = Base64.getEncoder().encodeToString(arrayOfByte1);
/*  93 */       String str2 = Base64.getEncoder().encodeToString(arrayOfByte2);
/*  94 */       String str3 = Base64.getEncoder().encodeToString(arrayOfByte);
/*     */       
/*  96 */       return str1 + " " + str1 + " " + str2;
/*     */     }
/*  98 */     catch (Exception exception) {
/*  99 */       throw new AlgorithmException("Something went wrong in encryption: " + exception.getMessage());
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
/*     */   public static byte[] decrypt(String paramString1, String paramString2) throws AlgorithmException, IncorrectPasswordException, Exception {
/* 111 */     String[] arrayOfString = paramString2.split(" ");
/*     */     
/* 113 */     byte[] arrayOfByte1 = Base64.getDecoder().decode(arrayOfString[0]);
/* 114 */     byte[] arrayOfByte2 = Base64.getDecoder().decode(arrayOfString[1]);
/* 115 */     byte[] arrayOfByte3 = Base64.getDecoder().decode(arrayOfString[2]);
/*     */ 
/*     */     
/*     */     try {
/* 119 */       SecretKeySpec secretKeySpec = getSecretKey(arrayOfByte1, paramString1);
/*     */       
/* 121 */       Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
/* 122 */       cipher.init(2, secretKeySpec, new GCMParameterSpec(128, arrayOfByte2));
/*     */       
/* 124 */       return cipher.doFinal(arrayOfByte3);
/*     */     }
/* 126 */     catch (AEADBadTagException aEADBadTagException) {
/* 127 */       throw new IncorrectPasswordException("Incorrect password");
/* 128 */     } catch (Exception exception) {
/* 129 */       throw new AlgorithmException("Something went wrong in decryption: " + exception.getMessage());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String generatePassword(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt) {
/* 150 */     String str1 = "";
/* 151 */     if (paramBoolean1) str1 = str1 + "abcdefghijklmnopqrstuvwxyz"; 
/* 152 */     if (paramBoolean2) str1 = str1 + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
/* 153 */     if (paramBoolean3) str1 = str1 + "1234567890"; 
/* 154 */     if (paramBoolean4) str1 = str1 + "!@#$%&*?^";
/*     */     
/* 156 */     if (str1.equals("")) return "";
/*     */     
/* 158 */     SecureRandom secureRandom = new SecureRandom();
/*     */     
/* 160 */     String str2 = "";
/* 161 */     for (byte b = 0; b < paramInt; b++) {
/* 162 */       str2 = str2 + str2;
/*     */     }
/*     */     
/* 165 */     return str2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean stringContainsAnyOf(String paramString1, String paramString2) {
/* 173 */     for (byte b = 0; b < paramString2.length(); b++) {
/* 174 */       if (paramString1.contains(paramString2.substring(b, b + 1))) return true; 
/* 175 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String generatePasswordUseAll(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt) {
/* 182 */     paramInt = Math.max(4, paramInt);
/* 183 */     String str = generatePassword(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramInt);
/*     */     
/* 185 */     while ((!stringContainsAnyOf(str, "abcdefghijklmnopqrstuvwxyz") && paramBoolean1) || (
/* 186 */       !stringContainsAnyOf(str, "ABCDEFGHIJKLMNOPQRSTUVWXYZ") && paramBoolean2) || (
/* 187 */       !stringContainsAnyOf(str, "1234567890") && paramBoolean3) || (
/* 188 */       !stringContainsAnyOf(str, "!@#$%&*?^") && paramBoolean4))
/* 189 */       str = generatePassword(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramInt); 
/* 190 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getCombinations(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt) {
/* 201 */     double d = ((paramBoolean1 ? "abcdefghijklmnopqrstuvwxyz".length() : 0) + (paramBoolean2 ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ".length() : 0) + (paramBoolean3 ? "1234567890".length() : 0) + (paramBoolean4 ? "!@#$%&*?^".length() : 0));
/* 202 */     return Math.pow(d, paramInt);
/*     */   }
/*     */   
/*     */   public enum SecurityLevel
/*     */   {
/* 207 */     EXTREMELY_UNSECURE(1.728E14D),
/* 208 */     UNSECURE(1.5768E17D),
/* 209 */     SECURE(9.4608E18D),
/* 210 */     VERY_SECURE(Double.POSITIVE_INFINITY);
/*     */     
/*     */     private final double numCombos;
/*     */     
/*     */     SecurityLevel(double param1Double) {
/* 215 */       this.numCombos = param1Double;
/*     */     }
/*     */     
/*     */     public static SecurityLevel getSecurityLevel(double param1Double) {
/* 219 */       if (param1Double <= EXTREMELY_UNSECURE.numCombos)
/* 220 */         return EXTREMELY_UNSECURE; 
/* 221 */       if (param1Double <= UNSECURE.numCombos)
/* 222 */         return UNSECURE; 
/* 223 */       if (param1Double <= SECURE.numCombos)
/* 224 */         return SECURE; 
/* 225 */       return VERY_SECURE;
/*     */     }
/*     */   }
/*     */   
/*     */   public static SecurityLevel getSecurityLevel(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt) {
/* 230 */     return SecurityLevel.getSecurityLevel(getCombinations(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramInt));
/*     */   }
/*     */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\crypto\Crypto.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */