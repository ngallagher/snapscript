package org.snapscript.core;

public class SuperInstance extends ObjectInstance {

   private final Type base;
   
   public SuperInstance(Module module, Model model, Instance scope, Type base, Type real) {
      super(module, model, scope, real);
      this.base = base;
   }
   
   public Type getSuper() {
      return base;
   }
}
