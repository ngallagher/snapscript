package org.snapscript.core;

import java.util.HashMap;
import java.util.Map;

public class CompoundScope implements Scope {
   
   private final Map<String, Value> values;
   private final Context context;
   private final Module module;
   private final Scope scope;
   private final Type type;
   
   public CompoundScope(Scope scope) {
      this.values = new HashMap<String, Value>();    
      this.context = scope.getContext();
      this.module = scope.getModule();
      this.type = scope.getType();
      this.scope = scope;
   } 
   
   @Override
   public Type getType() {
      return type;
   }
   
   @Override
   public Scope getScope() {
      return new CompoundScope(this); // this goes too deep!!
   }   

   @Override
   public Module getModule() {
      return module;
   }
   
   @Override
   public Context getContext() {
      return context;
   }   

   @Override
   public Value getValue(String name) {
      Value value = values.get(name);
      
      if(value == null) {
         return scope.getValue(name);
      }
      return value;
   }

   @Override
   public void setValue(String name, Value value) {
      Value variable = values.get(name);
      Object data = value.getValue();

      if(variable == null) {
         throw new IllegalStateException("Variable '" + name + "' does not exist");
      }
      variable.setValue(data);      
   }
   
   @Override
   public void addVariable(String name, Reference value) {
      Value variable = values.get(name);

      if(variable != null) {
         throw new IllegalStateException("Variable '" + name + "' already exists");
      }
      values.put(name, value);      
   }
   
   @Override
   public void addConstant(String name, Constant value) {
      Value variable = values.get(name);

      if(variable != null) {
         throw new IllegalStateException("Variable '" + name + "' already exists");
      }
      values.put(name, value);     
   }   
}
