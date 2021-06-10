package safekeeper.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import safekeeper.Debug;

public class Crypto {
  private static final int secretKeyHashIterations = 1000000;
  
  private static final String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
  
  private static final String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  
  private static final String numberChars = "1234567890";
  
  private static final String symbolChars = "!@#$%&*?^";
  
  private static final double dayCombos = 8.64E13D;
  
  public static class AlgorithmException extends Exception {
    public AlgorithmException(String param1String) {
      super(param1String);
    }
  }
  
  public static class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(String param1String) {
      super(param1String);
    }
  }
  
  private static SecretKeySpec getSecretKey(byte[] paramArrayOfbyte, String paramString) throws NoSuchAlgorithmException {
    byte[] arrayOfByte1 = paramString.getBytes(StandardCharsets.UTF_8);
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    byte[] arrayOfByte2 = new byte[32];
    for (int i = 0; i < 1000000; i++) {
      messageDigest.update(paramArrayOfbyte);
      messageDigest.update(arrayOfByte1);
      messageDigest.update(arrayOfByte2);
      arrayOfByte2 = messageDigest.digest();
    } 
    return new SecretKeySpec(arrayOfByte2, "AES");
  }
  
  public static String encrypt(String paramString, byte[] paramArrayOfbyte) throws AlgorithmException {
    SecureRandom secureRandom = new SecureRandom();
    byte[] arrayOfByte1 = new byte[32];
    secureRandom.nextBytes(arrayOfByte1);
    byte[] arrayOfByte2 = new byte[12];
    secureRandom.nextBytes(arrayOfByte2);
    try {
      SecretKeySpec secretKeySpec = getSecretKey(arrayOfByte1, paramString);
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      cipher.init(1, secretKeySpec, new GCMParameterSpec(128, arrayOfByte2));
      byte[] arrayOfByte = cipher.doFinal(paramArrayOfbyte);
      String str1 = Base64.getEncoder().encodeToString(arrayOfByte1);
      String str2 = Base64.getEncoder().encodeToString(arrayOfByte2);
      String str3 = Base64.getEncoder().encodeToString(arrayOfByte);
      return str1 + " " + str2 + " " + str3;
    } catch (Exception exception) {
      throw new AlgorithmException("Something went wrong in encryption: " + exception.getMessage());
    } 
  }
  
  public static byte[] decrypt(String password, String ciphertext) throws AlgorithmException, IncorrectPasswordException, Exception {
    String[] arrayOfString = ciphertext.split(" ");
    byte[] arrayOfByte1 = Base64.getDecoder().decode(arrayOfString[0]);
    byte[] arrayOfByte2 = Base64.getDecoder().decode(arrayOfString[1]);
    byte[] arrayOfByte3 = Base64.getDecoder().decode(arrayOfString[2]);
    try {
      SecretKeySpec secretKeySpec = getSecretKey(arrayOfByte1, password);
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      cipher.init(2, secretKeySpec, new GCMParameterSpec(128, arrayOfByte2));
      return cipher.doFinal(arrayOfByte3);
    } catch (AEADBadTagException aEADBadTagException) {
      throw new IncorrectPasswordException("Incorrect password");
    } catch (Exception exception) {
      throw new AlgorithmException("Something went wrong in decryption: " + exception.getMessage());
    } 
  }
  
  public static String generatePassword(boolean useLower, boolean useUpper, boolean useNums, boolean useSymbols, int length) {
    String str1 = "";
    if (useLower)
      str1 = str1 + "abcdefghijklmnopqrstuvwxyz"; 
    if (useUpper)
      str1 = str1 + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
    if (useNums)
      str1 = str1 + "1234567890"; 
    if (useSymbols)
      str1 = str1 + "!@#$%&*?^";
    if (str1.equals(""))
      return ""; 
    SecureRandom secureRandom = new SecureRandom();
    String str2 = "";
    for (byte b = 0; b < length; b++)
      str2 += str1.charAt((int)(secureRandom.nextDouble()*str1.length()));
    return str2;
  }
  
  private static boolean stringContainsAnyOf(String paramString1, String paramString2) {
    for (byte b = 0; b < paramString2.length(); b++) {
      if (paramString1.contains(paramString2.substring(b, b + 1)))
        return true; 
    } 
    return false;
  }
  
  public static String generatePasswordUseAll(boolean useLower, boolean useUpper, boolean useNums, boolean useSymbols, int length) {
    length = Math.max(4, length);
    String str = generatePassword(useLower, useUpper, useNums, useSymbols, length);
    while ((!stringContainsAnyOf(str, "abcdefghijklmnopqrstuvwxyz") && useLower) || (
      !stringContainsAnyOf(str, "ABCDEFGHIJKLMNOPQRSTUVWXYZ") && useUpper) || (
      !stringContainsAnyOf(str, "1234567890") && useNums) || (
      !stringContainsAnyOf(str, "!@#$%&*?^") && useSymbols)) {
       str = generatePassword(useLower, useUpper, useNums, useSymbols, length);
	  }
    return str;
  }
  
  public static double getCombinations(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt) {
    double d = ((paramBoolean1 ? "abcdefghijklmnopqrstuvwxyz".length() : 0) + (paramBoolean2 ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ".length() : 0) + (paramBoolean3 ? "1234567890".length() : 0) + (paramBoolean4 ? "!@#$%&*?^".length() : 0));
    return Math.pow(d, paramInt);
  }
  
  public enum SecurityLevel {
    EXTREMELY_UNSECURE(1.728E14D),
    UNSECURE(1.5768E17D),
    SECURE(9.4608E18D),
    VERY_SECURE(Double.POSITIVE_INFINITY);
    
    private final double numCombos;
    
    SecurityLevel(double param1Double) {
      this.numCombos = param1Double;
    }
    
    public static SecurityLevel getSecurityLevel(double param1Double) {
      if (param1Double <= EXTREMELY_UNSECURE.numCombos)
        return EXTREMELY_UNSECURE; 
      if (param1Double <= UNSECURE.numCombos)
        return UNSECURE; 
      if (param1Double <= SECURE.numCombos)
        return SECURE; 
      return VERY_SECURE;
    }
  }
  
  public static SecurityLevel getSecurityLevel(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt) {
    return SecurityLevel.getSecurityLevel(getCombinations(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramInt));
  }
}
