package org.snapscript.core;

public class Instance implements Scope {
   
   private final Instance base;
   private final State state;
   private final Scope scope;
   private final Type type;
   
   public Instance(Model model, Scope scope, Instance base, Type type) {
      this.state = new InstanceState(model, scope, base);      
      this.scope = scope;
      this.base = base;
      this.type = type;
   }
   
   @Override
   public Scope getInner() {
      return new CompoundScope(null, this, this);
   } 
   
   @Override
   public Scope getOuter() {
      return this;
   } 
   
   public Instance getInstance() {
      return base;
   }
   
   @Override
   public Type getType(){
      return type;
   }
  
   @Override
   public Module getModule() {
      return scope.getModule();
   }
   
   @Override
   public Context getContext() {
      return scope.getContext();
   }   
   
   @Override
   public Model getModel() {
      return scope.getModel();
   }

   @Override
   public State getState() {
      return state;
   }
   
   @Override
   public String toString(){
      return type.toString();
   }
}