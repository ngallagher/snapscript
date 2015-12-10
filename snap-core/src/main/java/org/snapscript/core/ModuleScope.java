package org.snapscript.core;

public class ModuleScope implements Scope {
   
   private final Module module;
   private final State state;
   
   public ModuleScope(Module module) {
      this.state = new MapState();
      this.module = module;
   }
   
   @Override
   public Scope getScope() {
      return new CompoundScope(this);
   } 
   
   @Override
   public Context getContext() {
      return module.getContext();
   } 

   @Override
   public State getState() {
      return state;
   }
   
   @Override
   public Type getType() {
      return null;
   }  

   @Override
   public Module getModule() {
      return module;
   }
}
