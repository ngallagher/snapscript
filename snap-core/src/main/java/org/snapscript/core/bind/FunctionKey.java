package org.snapscript.core.bind;

import org.snapscript.core.Type;

public class FunctionKey {      

   private final Type[] types;
   private final Object type;
   private final String name;
   private final int length;
   
   public FunctionKey(Object type, String name, Type[] types) {
      this.length = name.length();
      this.types = types;
      this.name = name;
      this.type = type;
   }
   
   @Override
   public boolean equals(Object object) {
      if(this != object) {
         return equals((FunctionKey)object);
      }
      return true;
   }
   
   public boolean equals(FunctionKey object) {
      if(object.type != type) {
         return false;
      }
      if(object.types.length != types.length) {
         return false;
      }
      if(object.length != length) {
         return false;
      }
      for(int i = 0; i < types.length; i++) {
         if(types[i] != object.types[i]) {
            return false;
         }         
      }
      return object.name.equals(name);
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
