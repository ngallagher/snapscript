package org.snapscript.compile.instruction;

import org.snapscript.core.Bug;

@Bug("This key builder is no good, need to consider Type instead of class")
public class VariableKeyBuilder {

   public Object create(Object left, String name) {
      if(left != null) {
         Class type = left.getClass();
         return new VariableKey(name, type);// this key is no good?
      }
      return new VariableKey(name, null);
   }
}
