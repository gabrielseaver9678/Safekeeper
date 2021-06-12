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
import safekeeper.crypto.Crypto.CorruptedVaultException;

public class ServiceGroupList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static class CorruptedSerializationException extends Exception {
		public CorruptedSerializationException(String param1String) {
			super(param1String);
		}
	}
	
	public final HashSet<ServiceGroup> serviceGroups = new HashSet<>();
	
	public static ServiceGroupList fromCryptoSerialized(String paramString1, String paramString2) throws Crypto.AlgorithmException, CorruptedSerializationException, Crypto.IncorrectPasswordException, CorruptedVaultException, IOException, NoSuchFileException, Exception {
		byte[] arrayOfByte = Crypto.decrypt(paramString1, paramString2);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			return (ServiceGroupList)objectInputStream.readObject();
		} catch (Exception exception) {
			throw new CorruptedSerializationException("The password vault is corrupted: " + exception.getMessage());
		}
	}
	
	public String getCryptoSerialized(String paramString) throws Crypto.AlgorithmException, IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(this);
		String str = Crypto.encrypt(paramString, byteArrayOutputStream.toByteArray());
		byteArrayOutputStream.close();
		objectOutputStream.close();
		return str;
	}
	
	public Object[] getServicesAlphabetical() {
		return Alphabetical.orderSetAlphabetically(this.serviceGroups, paramObject -> ((ServiceGroup)paramObject).name);
	}
}
