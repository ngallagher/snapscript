package org.snapscript.compile.instruction.define;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Context;
import org.snapscript.core.MapState;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;

public class StaticScope implements Scope {
   
   private final State state;;
   private final Scope scope;
   
   public StaticScope(Scope scope) {
      this.state = new MapState(scope);      
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
   public State getState() {
      return state;
   }

}
