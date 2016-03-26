package org.snapscript.core;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class SuperInstance extends Instance {

   private final Type base;
   
   public SuperInstance(Model model, Scope scope, Type type, Type base) {
      super(model, scope, null, type);
      this.base = base;
   }
   
   public Type getSuper() {
      return base;
   }
}
