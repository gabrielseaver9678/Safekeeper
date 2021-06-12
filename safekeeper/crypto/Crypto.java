
// Crypto.java, Gabriel Seaver, 2021

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

public class Crypto {
	
	// Cryptography exceptions
	public static class AlgorithmException extends Exception {
		public AlgorithmException (String message) {
			super(message);
		}
	}
	
	public static class CorruptedVaultException extends Exception {
		public CorruptedVaultException () {
			super("The password vault file is corrupted.");
		}
	}
	
	public static class IncorrectPasswordException extends Exception {
		public IncorrectPasswordException (String message) {
			super(message);
		}
	}
	
	public static String encodeBase64 (byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}
	
	public static byte[] decodeBase64 (String dataBase64) {
		return Base64.getDecoder().decode(dataBase64);
	}
	
	// Number of SHA-256 iterations to perform
	private static final int secretKeyHashIterations = 1000000;
	
	private static SecretKeySpec getSecretKey (byte[] saltBytes, String password) throws NoSuchAlgorithmException {
		// Gets byte array from password
		byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
		
		// Gets SHA-256 algorithm and sets up an array for the final output
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] outputBytes = new byte[32];
		
		// Runs through many hash iterations
		for (int i = 0; i < secretKeyHashIterations; i++) {
			messageDigest.update(saltBytes);
			messageDigest.update(passwordBytes);
			messageDigest.update(outputBytes);
			outputBytes = messageDigest.digest();
		}
		
		// Returns the new secret key
		return new SecretKeySpec(outputBytes, "AES");
	}
	
	public static String encrypt (String password, byte[] plaintext) throws AlgorithmException {
		// Generates random salt bytes
		SecureRandom secureRandom = new SecureRandom();
		byte[] saltBytes = new byte[32];
		secureRandom.nextBytes(saltBytes);
		
		// Generates random initialization vector
		byte[] initVector = new byte[12];
		secureRandom.nextBytes(initVector);
		try {
			// Gets a new secret key using the password and salt bytes
			SecretKeySpec secretKeySpec = getSecretKey(saltBytes, password);
			
			// Uses AES cipher to encrypt the plaintext 
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(1, secretKeySpec, new GCMParameterSpec(128, initVector));
			byte[] ciphertext = cipher.doFinal(plaintext);
			
			// Generates the three-part ciphertext using the salt bytes, the IV, and the ciphertext
			String saltString = encodeBase64(saltBytes);
			String ivString = encodeBase64(initVector);
			String ciphertextString = encodeBase64(ciphertext);
			
			return saltString + " " + ivString + " " + ciphertextString;
		} catch (Exception e) {
			throw new AlgorithmException("Something went wrong in encryption: " + e.getMessage());
		}
	}
	
	public static byte[] decrypt (String password, String threePartCiphertext)
			throws AlgorithmException, IncorrectPasswordException, CorruptedVaultException, Exception {
		// Get the parts of the three-part ciphertext
		String[] splitTPC = threePartCiphertext.split(" ");
		if (splitTPC.length != 3)
			throw new CorruptedVaultException();
		
		// Get base-64 decoded three part ciphertext
		byte[] saltBytes, ivBytes, ciphertext;
		try {
			saltBytes = decodeBase64(splitTPC[0]);
			ivBytes = decodeBase64(splitTPC[1]);
			ciphertext = decodeBase64(splitTPC[2]);
		} catch (IllegalArgumentException e) {
			throw new CorruptedVaultException();
		}
		
		// Decrypt and return
		try {
			// Gets the secret key
			SecretKeySpec secretKeySpec = getSecretKey(saltBytes, password);
			
			// Decrypts the three-part ciphertext and returns the plaintext
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(2, secretKeySpec, new GCMParameterSpec(128, ivBytes));
			return cipher.doFinal(ciphertext);
		} catch (AEADBadTagException aEADBadTagException) {
			// Secret key was incorrect; incorrect password
			throw new IncorrectPasswordException("Incorrect password");
		} catch (Exception exception) {
			// Some other problem with the decryption
			throw new AlgorithmException("Something went wrong in decryption: " + exception.getMessage());
		}
	}
	
	private static final String
		lowercaseChars = "abcdefghijklmnopqrstuvwxyz",
		uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
		numberChars = "1234567890",
		symbolChars = "!@#$%&*?^";
	
	public static String generatePassword (boolean useLower, boolean useUpper, boolean useNums, boolean useSymbols, int length) {
		// Gets a string of all usable characters
		String chars = "";
		if (useLower)
			chars += lowercaseChars;
		if (useUpper)
			chars += uppercaseChars;
		if (useNums)
			chars += numberChars;
		if (useSymbols)
			chars += symbolChars;
		if (chars.equals(""))
			return "";
		
		// Uses a secure random number generator to add random characters to the password
		SecureRandom secureRandom = new SecureRandom();
		String password = "";
		for (byte i = 0; i < length; i++)
			password += chars.charAt((int)(secureRandom.nextDouble() * chars.length()));
		return password;
	}
	
	private static boolean stringContainsAnyOf (String string, String substringChars) {
		// Loops through substring chars to see if any are contained in the string
		for (byte i = 0; i < substringChars.length(); i++)
			if (string.contains(substringChars.charAt(i)+""))
				return true;
		return false;
	}
	
	/**
	 * Generates a password which is guaranteed to use all given character sets.
	 */
	public static String generatePasswordUseAll (boolean useLower, boolean useUpper, boolean useNums, boolean useSymbols, int length) {
		length = Math.max(4, length);
		
		// Generates a password
		String password = generatePassword(useLower, useUpper, useNums, useSymbols, length);
		
		// Generate new passwords until it contains at least one character from each set
		while ((!stringContainsAnyOf(password, lowercaseChars) && useLower) || (
			!stringContainsAnyOf(password, uppercaseChars) && useUpper) || (
			!stringContainsAnyOf(password, numberChars) && useNums) || (
			!stringContainsAnyOf(password, symbolChars) && useSymbols)) {
			password = generatePassword(useLower, useUpper, useNums, useSymbols, length);
		} return password;
	}
	
	public static double getCombinations (boolean lower, boolean upper, boolean nums, boolean symbols, int length) {
		double combinations =
			((lower ? lowercaseChars.length() : 0) +
			(upper ? uppercaseChars.length() : 0) +
			(nums ? numberChars.length() : 0) +
			(symbols ? symbolChars.length() : 0));
		return Math.pow(combinations, length);
	}
	
	private static final double dayCombos = 1_000_000_000. * 60. * 60. * 24.;
	public enum SecurityLevel {
		EXTREMELY_UNSECURE	(2.*dayCombos),
		UNSECURE			(5.*365.*dayCombos),
		SECURE				(300.*365.*dayCombos),
		VERY_SECURE			(Double.POSITIVE_INFINITY);
		
		private final double numCombos;
		SecurityLevel (double numCombos) {
			this.numCombos = numCombos;
		}
		
		public static SecurityLevel getPasswordSecurityLevel (double combinations) {
			if (combinations <= EXTREMELY_UNSECURE.numCombos)
				return EXTREMELY_UNSECURE;
			if (combinations <= UNSECURE.numCombos)
				return UNSECURE;
			if (combinations <= SECURE.numCombos)
				return SECURE;
			return VERY_SECURE;
		}
	}
	
	public static SecurityLevel getPasswordSecurityLevel (boolean lower, boolean upper, boolean nums, boolean symbols, int length) {
		return SecurityLevel.getPasswordSecurityLevel(getCombinations(lower, upper, nums, symbols, length));
	}
}
