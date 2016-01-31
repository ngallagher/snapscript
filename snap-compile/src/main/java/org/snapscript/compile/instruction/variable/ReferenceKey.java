package org.snapscript.compile.instruction.variable;

import org.snapscript.core.Type;

public class ReferenceKey {

   private final String name;
   private final Type type;
   
   public ReferenceKey(String name, Type type) {
      this.name = name;
      this.type = type;
   }
   
   @Override
   public boolean equals(Object key) {
      if(this != key) {
         return equals((ReferenceKey)key);
      }
      return true;
   }
   
   public boolean equals(ReferenceKey key) {
      if(key.type != type) {
         return false;
      }
      return key.name.equals(name);
   }
   
   @Override
   public int hashCode() {
      if(type != null) {
         return name.hashCode() ^ type.hashCode();
      }
      return name.hashCode();
   }
   
   @Override
   public String toString() {
      return name;
   }
}
