package org.snapscript.core;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;

public class ModuleScope implements Scope {
   
   private final Map<String, Value> values;
   private final Module module;
   
   public ModuleScope(Module module) {
      this.values = new HashMap<String, Value>();      
      this.module = module;
   }
   
   @Override
   public Type getType() {
      return null;
   }
   
   @Override
   public Scope getScope() {
      return new CompoundScope(this);
   }   

   @Override
   public Module getModule() {
      return module;
   }
   
   @Override
   public Context getContext() {
      return module.getContext();
   }      

   @Override
   public Value getValue(String name) {
      return values.get(name);
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
}
