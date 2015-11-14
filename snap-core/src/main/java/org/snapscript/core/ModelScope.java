package org.snapscript.core;

public class ModelScope implements Scope {
   
   private final State state;
   private final Scope scope;
   
   public ModelScope(Scope scope, Model model) {
      this.state = new ModelState(scope, model);
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
   public State getState() {
      return state;
   }

   private static class ModelState implements State {
      
      private final Model model;
      private final State state;
      
      public ModelState(Scope scope, Model model) {
         this.state = scope.getState();
         this.model = model;
      }
      
      @Override
      public Value getValue(String name) {
         Value variable = state.getValue(name);
         
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
   }
}
