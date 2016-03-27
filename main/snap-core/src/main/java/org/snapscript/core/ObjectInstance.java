package org.snapscript.core;

public class ObjectInstance implements Instance {
   
   private final Instance outer;
   private final State state;
   private final Model model;
   private final int depth;
   
   public ObjectInstance(Model model, Instance outer, int depth) {
      this.state = new MapState(model, outer); 
      this.outer = outer;
      this.depth = depth;
      this.model = model;
   }
   
   @Override
   public Instance getInner() {
      return new CompoundInstance(model, this, this, depth + 1);
   } 
   
   @Override
   public Instance getOuter() {
      return this; // this is the final one!!
   } 
   
   @Override
   public Instance getInstance() {
      return outer.getInstance(); // this is the final one!!
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
   
   public int getDepth(){
      return depth;
   }
   
   @Override
   public String toString(){
      return outer.toString();
   }
}
