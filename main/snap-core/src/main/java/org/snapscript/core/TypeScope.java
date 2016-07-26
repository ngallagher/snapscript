package org.snapscript.core;

public class TypeScope implements Scope {
   
   private final State state;
   private final Type type;
   
   @Bug("we probably should provide the Type or Module here")
   public TypeScope(Type type) {
      this.state = new MapState(null, null);
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
   public Module getModule() {
      return type.getModule();
   }
   
   @Bug("This is broken")
   @Override
   public Context getContext() {
      return type.getModule().getContext();
   }   
   
   @Override
   public Model getModel() {
      return null;
   }
   
   @Override
   public Type getType(){
      return null;
   }  
   
   @Override
   public State getState() {
      return state;
   }
   
   @Override
   public String toString() {
      return String.valueOf(state);
   }

}
