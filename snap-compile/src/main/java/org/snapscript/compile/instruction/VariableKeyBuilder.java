package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;

public class VariableKeyBuilder {

   public Object create(Object left, String name) {
      if(left != null) {
         Class type = left.getClass();
         return new VariableKey(name, type);// this key is no good?
      }
      return new VariableKey(name, null);
   }
}
