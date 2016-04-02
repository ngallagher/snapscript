package org.snapscript.core.instance;

import org.snapscript.core.Context;
import org.snapscript.core.MapState;
import org.snapscript.core.Model;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;

public class StaticInstance implements Instance {
   
   private final Instance outer;
   private final Module module;
   private final State state;
   private final Model model;
   private final Type type;
   
   public StaticInstance(Module module, Model model, Scope inner, Instance outer, Type type) {
      this.state = new MapState(model, inner);
      this.module = module;
      this.outer = outer;
      this.model = model;
      this.type = type;
   }
   
   @Override
   public Instance getInner() {
      return new StaticInstance(module, model, this, outer, type);
   } 
   
   @Override
   public Instance getOuter() {
      return outer; 
   } 
   
   @Override
   public Context getContext() {
      return module.getContext();
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
      return module;
   }  
   
   @Override
   public Type getType(){
      return type;
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
