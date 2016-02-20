package org.snapscript.core.bind;

public class FunctionKey {      

   private final Object[] types;
   private final Object type;
   private final String name;
   private final int length;
   
   public FunctionKey(Object type, String name, Object[] types) {
      this.length = name.length();
      this.types = types;
      this.name = name;
      this.type = type;
   }
   
   @Override
   public boolean equals(Object key) {
      if(key instanceof FunctionKey) {
         return equals((FunctionKey)key);
      }
      return false;
   }
   
   public boolean equals(FunctionKey key) {
      if(key.type != type) {
         return false;
      }
      if(key.types.length != types.length) {
         return false;
      }
      if(key.length != length) {
         return false;
      }
      for(int i = 0; i < types.length; i++) {
         if(types[i] != key.types[i]) {
            return false;
         }         
      }
      return key.name.equals(name);
   }
   
   @Override
   public int hashCode() {
      return name.hashCode() ^ type.hashCode();
   }
   
   @Override
   public String toString() {
      return name;
   }
}
