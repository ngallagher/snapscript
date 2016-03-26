package org.snapscript.core;

public class CompoundInstance implements Instance {
   
   private final Instance outer;
   private final Scope primitive;
   private final State state;
   private final Model model;
   private final Type type;
   private final int depth;
   
   public CompoundInstance(Model model, Scope primitive, Scope inner, Instance outer, Type type, int depth) {
      this.state = new MapState(model, inner); 
      this.primitive = primitive;
      this.outer = outer;
      this.depth = depth;
      this.model = model;
      this.type = type;
   }
   
   @Override
   public Instance getInner() {
      return new CompoundInstance(model, primitive, this, outer, type, depth + 1);
   } 
   
   @Override
   public Instance getOuter() {
      return outer; 
   } 
   
   @Override
   public Scope getScope() {
      return primitive; 
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
   public Model getModel() {
      return model;
   }

   @Override
   public State getState() {
      return state;
   }
   
   @Override
   public Type getType(){
      return type;
   }
   
   public int getDepth(){
      return depth;
   }
   
   @Override
   public String toString(){
      return type.toString();
   }
}
