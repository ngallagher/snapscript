package org.snapscript.core.define;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Constant;
import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Reference;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class TypeScope implements Scope {
   
   private final Map<String, Value> values;
   private final Scope scope;
   private final Type type;
   
   public TypeScope(Scope scope, Type type) {
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
   
   @Override
   public String toString(){
      return type.toString();
   }
}
