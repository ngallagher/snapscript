package org.snapscript.core;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class PrimitiveInstance extends Instance {
   
   public PrimitiveInstance(Model model, Scope scope, Type type) {
      super(model, scope, null, type);
   }
   
   public Instance getInstance() {
      return this;
   }
}
