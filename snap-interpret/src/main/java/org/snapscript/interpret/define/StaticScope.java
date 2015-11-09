package org.snapscript.interpret.define;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class StaticScope implements Scope {
   
   private final Map<String, Value> values;
   private final Scope scope;
   
   public StaticScope(Scope scope) {
      this.values = new HashMap<String, Value>();      
      this.scope = scope;
   }
   
   @Override
   public Type getType(){
      return null;
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
}
