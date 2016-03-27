package org.snapscript.core;

public class SuperInstance extends ObjectInstance {

   private final Type base;
   
   public SuperInstance(Model model, Instance scope, Type base) {
      super(model, scope);
      this.base = base;
   }
   
   public Type getSuper() {
      return base;
   }
}
