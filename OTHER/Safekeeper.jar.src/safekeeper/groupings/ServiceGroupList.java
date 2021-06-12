/*    */ package safekeeper.groupings;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import java.util.HashSet;
/*    */ import safekeeper.crypto.Crypto;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServiceGroupList
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public static class CorruptedSerializationException
/*    */     extends Exception
/*    */   {
/*    */     public CorruptedSerializationException(String param1String) {
/* 44 */       super(param1String);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 49 */   public final HashSet<ServiceGroup> serviceGroups = new HashSet<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ServiceGroupList fromCryptoSerialized(String paramString1, String paramString2) throws Crypto.AlgorithmException, CorruptedSerializationException, Crypto.IncorrectPasswordException, IOException, Exception {
/* 58 */     byte[] arrayOfByte = Crypto.decrypt(paramString1, paramString2);
/* 59 */     ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/*    */     
/*    */     try {
/* 62 */       ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
/* 63 */       return (ServiceGroupList)objectInputStream.readObject();
/* 64 */     } catch (Exception exception) {
/* 65 */       throw new CorruptedSerializationException("The password vault is corrupted: " + exception.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCryptoSerialized(String paramString) throws Crypto.AlgorithmException, IOException {
/* 74 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 75 */     ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
/* 76 */     objectOutputStream.writeObject(this);
/*    */     
/* 78 */     String str = Crypto.encrypt(paramString, byteArrayOutputStream.toByteArray());
/* 79 */     byteArrayOutputStream.close();
/* 80 */     objectOutputStream.close();
/*    */     
/* 82 */     return str;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object[] getServicesAlphabetical() {
/* 87 */     return Alphabetical.orderSetAlphabetically(this.serviceGroups, paramObject -> ((ServiceGroup)paramObject).name);
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\groupings\ServiceGroupList.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */