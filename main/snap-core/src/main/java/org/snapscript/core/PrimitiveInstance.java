package org.snapscript.core;

import java.util.concurrent.atomic.AtomicReference;

public class PrimitiveInstance implements Instance {
   
   private final AtomicReference<Instance> reference;
   private final State state;
   private final Scope scope;
   private final Model model;
   private final Type type;
   
   public PrimitiveInstance(Model model, Scope scope, Type type) {
      this.reference = new AtomicReference<Instance>(this);
      this.state = new MapState(model, scope);
      this.scope = scope;
      this.model = model;
      this.type = type;
   }
   
   @Override
   public Instance getInner() {
      return new CompoundInstance(model, this, this);
   } 
   
   @Override
   public Instance getOuter() {
      return this;
   } 
   
   @Override
   public Instance getInstance() {
      return reference.get();
   }
   
   @Override
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
   
   @Override
   public String toString(){
      return type.toString();
   }
}
