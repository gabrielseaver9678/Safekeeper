/*    */ package safekeeper.groupings;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Set;
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
/*    */ public class Alphabetical
/*    */ {
/*    */   public static <T> Object[] orderSetAlphabetically(Set<T> paramSet, StringFromObject paramStringFromObject) {
/* 31 */     Object[] arrayOfObject = paramSet.toArray();
/* 32 */     Arrays.sort(arrayOfObject, (paramObject1, paramObject2) -> paramStringFromObject.getString(paramObject1).toLowerCase().compareTo(paramStringFromObject.getString(paramObject2).toLowerCase()));
/*    */ 
/*    */     
/* 35 */     return arrayOfObject;
/*    */   }
/*    */   
/*    */   public static interface StringFromObject {
/*    */     String getString(Object param1Object);
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\groupings\Alphabetical.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */