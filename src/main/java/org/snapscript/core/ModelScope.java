package org.snapscript.core;

import org.snapscript.Model;

public class ModelScope implements Scope {
   
   private final Model model;
   private final Scope scope;
   
   public ModelScope(Scope scope, Model model) {
      this.model = model;
      this.scope = scope;
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
      return scope.getModule();
   }
   
   @Override
   public Context getContext() {
      return scope.getContext();
   }   

   @Override
   public Value getValue(String name) {
      Value variable = scope.getValue(name);
      
      if(variable == null) {
         Object object = model.getAttribute(name);
         
         if(object != null) {
            return new Constant(object, name);
         }
      }
      return variable;
   }

   @Override
   public void setValue(String name, Value value) {
      scope.setValue(name, value);
   }
   
   @Override
   public void addVariable(String name, Reference value) {
      scope.addVariable(name, value);
   }
   
   @Override
   public void addConstant(String name, Constant value) {
      scope.addConstant(name, value);
   }    
}
