package org.snapscript.core;

import java.util.concurrent.atomic.AtomicReference;

public class PrimitiveInstance implements Instance {
   
   private final AtomicReference<Instance> reference;
   private final State state;
   private final Scope scope;
   private final Model model;
   private final Type type;
   private final int depth;
   
   public PrimitiveInstance(Model model, Scope scope, Type type, int depth) {
      this.state = new InstanceState(model, scope, null);
      this.reference = new AtomicReference<Instance>();
      this.scope = scope;
      this.depth = depth;
      this.model = model;
      this.type = type;
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
      return reference.get();
   }
   
   public void setInstance(Instance instance) {
      reference.set(instance);
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
