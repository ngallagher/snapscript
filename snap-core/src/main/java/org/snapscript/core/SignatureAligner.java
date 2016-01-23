package org.snapscript.core;

import java.lang.reflect.Array;
import java.util.List;

public class SignatureAligner {

   private final Signature signature;
   
   public SignatureAligner(Signature signature) {
      this.signature = signature;
   }
   
   public Object[] align(Object... list) throws Exception {
      List<String> names = signature.getNames();
      int modifiers = signature.getModifiers();
      
      if((modifiers & 0x00000080) != 0) {
         int length = names.size();
         int start = length - 1;
         int remaining = list.length - start;
         
         if(remaining > 0) {
            Object array = new Object[remaining];
            
            for(int i = 0; i < remaining; i++) {
               try {
                  Array.set(array, i, list[i + start]);
               } catch(Exception e){
                  throw new IllegalStateException("Invalid argument at " + i + " for" + signature, e);
               }
            }
            list[start] = array;
         }
         Object[] copy = new Object[length];
         
         for(int i = 0; i < length; i++) {
            copy[i] = list[i];
         }
         return copy;
      }
      return list;
   }
}
