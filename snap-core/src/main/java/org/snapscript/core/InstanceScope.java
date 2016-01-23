package org.snapscript.core;

public class InstanceScope implements Scope {
   
   private final State state;
   private final Scope scope;
   private final Type type;
   
   public InstanceScope(Scope scope, Type type) {
      this.state = new MapState(null, scope);      
      this.scope = scope;
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
