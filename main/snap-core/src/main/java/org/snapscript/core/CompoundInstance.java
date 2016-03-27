package org.snapscript.core;

public class CompoundInstance implements Instance {
   
   private final Instance outer;
   private final State state;
   private final Model model;
   
   public CompoundInstance(Model model, Scope inner, Instance outer) {
      this.state = new MapState(model, inner); 
      this.outer = outer;
      this.model = model;
   }
   
   @Override
   public Instance getInner() {
      return new CompoundInstance(model, this, outer);
   } 
   
   @Override
   public Instance getOuter() {
      return outer; 
   } 
   
   @Override
   public Instance getInstance() {
      return outer.getInstance(); 
   } 
   
   @Override
   public void setInstance(Instance instance) {
      outer.setInstance(instance);
   }
  
   @Override
   public Module getModule() {
      return outer.getModule();
   }
   
   @Override
   public Context getContext() {
      return outer.getContext();
   }   
   
   @Override
   public Type getType(){
      return outer.getType();
   }
   
   @Override
   public Model getModel() {
      return model;
   }

   @Override
   public State getState() {
      return state;
   }
   
   @Override
   public String toString(){
      return outer.toString();
   }
}
