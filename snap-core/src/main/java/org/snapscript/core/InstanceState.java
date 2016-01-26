package org.snapscript.core;

import java.util.Set;

public class InstanceState implements State {

   private final State state;
   private final Scope base;
   
   public InstanceState(Model model, Scope scope, Scope base) {
      this.state = new MapState(model, scope);
      this.base = base;
   }
   
   @Bug("What about super names i.e 'base'!!")
   @Override
   public Set<String> getNames() {
      return state.getNames();
   }

   @Override
   public Value getValue(String name) {
      Value variable = state.getValue(name);
      
      if(variable == null && base != null) {
         State state = base.getState();
         
         if(state == null) {
            throw new IllegalStateException("Scope for '" + name + "' does not exist");
         }
         variable = state.getValue(name);
      }
      return variable;
   }

   @Override
   public void setValue(String name, Value value) {
      state.setValue(name, value);
   }
   
   @Override
   public void addVariable(String name, Value value) {
      state.addVariable(name, value);
   }
   
   @Override
   public void addConstant(String name, Value value) {
      state.addConstant(name, value);
   }
   
   @Override
   public String toString() {
      return String.valueOf(state);
   }
}
