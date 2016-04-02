package org.snapscript.core.instance;

import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Context;
import org.snapscript.core.MapState;
import org.snapscript.core.Model;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;

public class PrimitiveInstance implements Instance {
   
   private final AtomicReference<Instance> reference;
   private final Module module;
   private final State state;
   private final Model model;
   private final Type type;
   
   public PrimitiveInstance(Module module, Model model, Scope scope, Type type) {
      this.reference = new AtomicReference<Instance>(this);
      this.state = new MapState(model, scope);
      this.module = module;
      this.model = model;
      this.type = type;
   }
   
   @Override
   public Instance getInner() {
      return new ObjectInstance(module, model, this, type);
   } 
   
   @Override
   public Instance getOuter() {
      return this;
   } 
   
   @Override
   public Context getContext() {
      return module.getContext();
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
      return module;
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
