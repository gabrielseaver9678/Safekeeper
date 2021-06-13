
// ServiceGroupList.java, Gabriel Seaver, 2021

package safekeeper.groupings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.NoSuchFileException;
import java.util.HashSet;

import safekeeper.crypto.Crypto;
import safekeeper.crypto.Crypto.AlgorithmException;
import safekeeper.crypto.Crypto.CorruptedVaultException;
import safekeeper.crypto.Crypto.IncorrectPasswordException;

public class ServiceGroupList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static class CorruptedSerializationException extends Exception {
		public CorruptedSerializationException (String message) {
			super(message);
		}
	}
	
	public final HashSet<ServiceGroup> serviceGroups = new HashSet<>();
	
	public static ServiceGroupList fromCryptoSerialized (String password, String threePartCiphertext)
			throws	AlgorithmException, CorruptedSerializationException, IncorrectPasswordException, CorruptedVaultException,
					IOException, NoSuchFileException, Exception {
		
		// Decrypts the ciphertext
		byte[] plaintext = Crypto.decrypt(password, threePartCiphertext);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(plaintext);
		
		// Attempts to deserialize the service group list
		try {
			ObjectInputStream objInStream = new ObjectInputStream(byteArrayInputStream);
			return (ServiceGroupList)objInStream.readObject();
		} catch (Exception e) {
			throw new CorruptedSerializationException("The password vault is corrupted: " + e.getMessage());
		}
		
	}
	
	public String getCryptoSerialized (String password) throws AlgorithmException, IOException {
		// Writes the sgl to an output stream
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);
		objOutStream.writeObject(this);
		
		// Encrypts the serialized data
		String ciphertext = Crypto.encrypt(password, byteOutStream.toByteArray());
		byteOutStream.close();
		objOutStream.close();
		return ciphertext;
	}
	
	public Object[] getServicesAlphabetical () {
		return Alphabetical.orderSetAlphabetically(serviceGroups, service -> ((ServiceGroup)service).name);
	}
	
}