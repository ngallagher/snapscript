package org.snapscript.compile.instruction;

public class VariableKeyBuilder {

   public Object create(Object left, String name) {
      if(left != null) {
         Class type = left.getClass();
         return new VariableKey(name, type);
      }
      return new VariableKey(name, null);
   }
}
