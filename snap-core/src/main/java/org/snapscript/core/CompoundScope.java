package org.snapscript.core;

public class CompoundScope implements Scope {
   
   private final State state;
   private final Scope outer;
   
   public CompoundScope(Scope inner, Scope outer) {
      this.state = new MapState(inner);  
      this.outer = outer;
   } 
  
   @Override
   public Scope getInner() {
      return new StateScope(this, outer);
   }  
   
   @Override
   public Scope getOuter() {
      return outer;
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
   
   private static class StateScope implements Scope {
      
      private final State state;
      private final Scope outer;
      
      public StateScope(Scope inner, Scope outer) {
         this.state = new MapState(inner);
         this.outer = outer;
      }

      @Override
      public Scope getInner() {
         return new StateScope(this, outer);
      }
      
      @Override
      public Scope getOuter() {
         return outer;
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
