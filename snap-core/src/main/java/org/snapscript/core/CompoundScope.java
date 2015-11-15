package org.snapscript.core;

public class CompoundScope implements Scope {
   
   private final State state;
   private final Scope scope;
   
   public CompoundScope(Scope scope) {
      this.state = new MapState(scope);    
      this.scope = scope;
   } 
  
   @Override
   public Scope getScope() {
      return new StateScope(scope, this);
   }  
   
   @Override
   public Type getType() {
      return scope.getType();
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
   
   private static class StateScope implements Scope {
      
      private final State state;
      private final Scope outer;
      
      public StateScope(Scope outer, Scope inner) {
         this.state = new MapState(inner);
         this.outer = outer;
      }

      @Override
      public Scope getScope() {
         return new StateScope(outer, this); // check state before deciding "outer" = quick, "inner" = slow
      }

      @Override
      public Type getType() {
         return outer.getType();
      }

      @Override
      public Module getModule() {
         return outer.getModule();
      }

      @Override
      public Context getContext() {
         return outer.getContext();
      }

      @Override
      public State getState() {
         return state;
      }
   }
}
