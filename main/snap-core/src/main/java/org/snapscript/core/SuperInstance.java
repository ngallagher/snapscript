package org.snapscript.core;

public class SuperInstance extends ObjectInstance {

   private final Type base;
   
   public SuperInstance(Model model, Scope primitive, Instance scope, Type type, Type base, int depth) {
      super(model, primitive, scope, null, type, depth);
      this.base = base;
   }
   
   public Type getSuper() {
      return base;
   }
}
