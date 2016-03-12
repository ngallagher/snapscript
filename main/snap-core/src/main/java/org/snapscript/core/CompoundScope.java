package org.snapscript.core;

public class CompoundScope implements Scope {
   
   private Context context;
   private Module module;
   private State state;
   private Scope outer;
   private Model model;
   private Type type;
   
   public CompoundScope(Model model, Scope inner, Scope outer) {
      this.state = new MapState(model, inner);  
      this.outer = outer;
      this.model = model;
   } 
   
   @Override
   public Scope getOuter() {
      return outer;
   }  
  
   @Override
   public Scope getInner() {
      return new StateScope(model, this, outer);
   }  
   
   @Override
   public Type getType() {
      if(type == null) {
         type = outer.getType();
      }
      return type;
   }

   @Override
   public Module getModule() {
      if(module == null) {
         module = outer.getModule();
      }
      return module;
   }

   @Override
   public Context getContext() {
      if(context == null) {
         context = outer.getContext(); 
      }
      return context;
   }

   @Override
   public Model getModel() {
      return model;
   }
   
   @Override
   public State getState() {
      return state;
   }
   
   @Override
   public String toString() {
      return String.valueOf(state);
   }
   
   private static class StateScope implements Scope {
      
      private Context context;
      private Module module;
      private State state;
      private Scope outer;
      private Model model;
      private Type type;
      
      public StateScope(Model model, Scope inner, Scope outer) {
         this.state = new MapState(model, inner);
         this.outer = outer;
         this.model = model;
      }
      
      @Override
      public Scope getOuter() {
         return outer;
      }
      
      @Override
      public Scope getInner() {
         return new StateScope(model, this, outer);
      }
      
      @Override
      public Type getType() {
         if(type == null) {
            type = outer.getType();
         }
         return type;
      }

      @Override
      public Module getModule() {
         if(module == null) {
            module = outer.getModule();
         }
         return module;
      }

      @Override
      public Context getContext() {
         if(context == null) {
            context = outer.getContext(); // this is expensive
         }
         return context;
      }
      
      @Override
      public Model getModel() {
         return model;
      }

      @Override
      public State getState() {
         return state;
      }
      
      @Override
      public String toString() {
         return String.valueOf(state);
      }
   }
}
