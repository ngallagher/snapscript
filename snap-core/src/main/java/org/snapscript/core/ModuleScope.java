package org.snapscript.core;

public class ModuleScope implements Scope {
   
   private final MapState state;
   private final Module module;
   
   public ModuleScope(Module module) {
      this.state = new MapState();  
      this.module = module;
   }
   
   @Override
   public Type getType() {
      return null;
   }
   
   @Override
   public Scope getScope() {
      return new CompoundScope(this);
   }   

   @Override
   public Module getModule() {
      return module;
   }
   
   @Override
   public Context getContext() {
      return module.getContext();
   }      

   @Override
   public State getState() {
      return state;
   }
}
