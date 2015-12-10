package org.snapscript.core;

public class ModelScope implements Scope {
   
   private final Module module;
   private final State state;
   
   public ModelScope(Model model, Module module) {
      this.state = new ModelState(model);
      this.module = module;
   }
   
   @Override
   public Scope getScope() {
      return new CompoundScope(this);
   } 
   
   @Override
   public Context getContext() {
      return module.getContext();
   } 

   @Override
   public State getState() {
      return state;
   }
   
   @Override
   public Type getType() {
      return null;
   }  

   @Override
   public Module getModule() {
      return module;
   }
   
   private static class ModelState implements State {
      
      private final Model model;
      private final State state;
      
      public ModelState(Model model) {
         this.state = new MapState();
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
