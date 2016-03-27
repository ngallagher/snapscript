package org.snapscript.core;

public class SuperInstance extends ObjectInstance {

   private final Type base;
   
   public SuperInstance(Model model, Instance scope, Type base, int depth) {
      super(model, scope, depth);
      this.base = base;
   }
   
   public Type getSuper() {
      return base;
   }
}
