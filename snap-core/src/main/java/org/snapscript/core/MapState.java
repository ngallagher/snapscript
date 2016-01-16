package org.snapscript.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapState implements State {

   private final Map<String, Value> values;
   private final Scope scope;
   
   public MapState() {
      this(null);
   }
   
   public MapState(Scope scope) {
      this.values = new HashMap<String, Value>();      
      this.scope = scope;
   }
   
   @Bug("clean up")
   @Override
   public Set<String> getNames() {
      Set<String> names = new HashSet<String>();   
      
      if(!values.isEmpty()) {
         Set<String> outer = values.keySet();
         names.addAll(outer);
      }
      if(scope != null) {
         State state = scope.getState();
         
         if(state != null) {
            Set<String> inner = state.getNames();
            names.addAll(inner);
         }
      }
      return names;
   }

   @Override
   public Value getValue(String name) {
      Value value = values.get(name);
      
      if(value == null && scope != null) {
         State state = scope.getState();
         
         if(state == null) {
            throw new IllegalStateException("Scope for '" + name + "' does not exist");
         }
         return state.getValue(name);
      }
      return value;
   }

   @Override
   public void setValue(String name, Value value) {
      Value variable = values.get(name);
      
      if(variable == null && scope != null) {
         State state = scope.getState();
         
         if(state == null) {
            throw new IllegalStateException("Scope for '" + name + "' does not exist");
         }
         variable = state.getValue(name);
      }
      Object data = value.getValue();

      if(variable == null) {
         throw new IllegalStateException("Variable '" + name + "' does not exist");
      }
      variable.setValue(data);      
   }
   
   @Override
   public void addVariable(String name, Value value) {
      Value variable = values.get(name);

      if(variable != null) {
         throw new IllegalStateException("Variable '" + name + "' already exists");
      }
      values.put(name, value);      
   }
   
   @Override
   public void addConstant(String name, Value value) {
      Value variable = values.get(name);

      if(variable != null) {
         throw new IllegalStateException("Variable '" + name + "' already exists");
      }
      values.put(name, value);     
   }
}
