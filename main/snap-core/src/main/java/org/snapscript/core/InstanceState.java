package org.snapscript.core;

import java.util.Set;

public class InstanceState implements State {

   private final State state;
   private final Scope base;
   
   public InstanceState(Model model, Scope scope, Scope base) {
      this.state = new MapState(model, scope);
      this.base = base;
   }
   
   @Override
   public Set<String> getNames() {
      Set<String> names = state.getNames();
      
      if(base != null) {
         State state = base.getState();
         Set<String> inner = state.getNames();
         
         if(!inner.isEmpty()) {
            names.addAll(inner);
         }
      }
      return names;
   }

   @Override
   public Value getValue(String name) {
      Value variable = state.getValue(name);
      
      if(variable == null && base != null) {
         State state = base.getState();
         
         if(state == null) {
            throw new InternalStateException("Scope for '" + name + "' does not exist");
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