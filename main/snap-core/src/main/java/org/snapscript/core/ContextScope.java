package org.snapscript.core;

public class ContextScope implements Scope {
   
   private final Context context;
   private final State state;
   
   public ContextScope(Context context) {
      this.state = new MapState();
      this.context = context;
   }
   
   @Override
   public Scope getInner() {
      return new CompoundScope(null, this, this);
   } 
   
   @Override
   public Scope getOuter() {
      return this;
   }
   
   @Override
   public Type getType() {
      return null;
   }
   
   @Override
   public Module getModule() {
      return null;
   }

   @Override
   public Context getContext() {
      return context;
   }

   @Override
   public State getState() {
      return state;
   }

   @Override
   public Model getModel() {
      return null;
   }
}
