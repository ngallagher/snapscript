package org.snapscript.core;

import java.util.HashMap;
import java.util.Map;

public class InstanceScope implements Scope {
   
   private final Map<String, Value> values;
   private final Scope scope;
   private final Type type;
   
   public InstanceScope(Scope scope, Type type) {
      this.values = new HashMap<String, Value>();      
      this.scope = scope;
      this.type = type;
   }
   
   @Override
   public Type getType(){
      return type;
   }
   
   @Override
   public Scope getScope() {
      return new CompoundScope(this); // this goes too deep!!
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
   
   @Override
   public String toString(){
      return type.toString();
   }
}
