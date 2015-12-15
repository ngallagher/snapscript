package org.snapscript.compile.instruction;

public class VariableKey {

   private final String name;
   private final Class type;
   
   public VariableKey(String name, Class type) {
      this.name = name;
      this.type = type;
   }
   
   @Override
   public boolean equals(Object object) {
      if(this != object) {
         return equals((VariableKey)object);
      }
      return true;
   }
   
   public boolean equals(VariableKey object) {
      if(object.type != type) {
         return false;
      }
      return object.name.equals(name);
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
