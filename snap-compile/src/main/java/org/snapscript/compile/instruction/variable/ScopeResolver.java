package org.snapscript.compile.instruction.variable;

import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;

public class ScopeResolver implements ValueResolver<Scope> {
   
   private final String name;
   
   public ScopeResolver(String name) {
      this.name = name;
   }
   
   @Override
   public Value resolve(Scope scope, Scope left) {
      State state = left.getState();
      return state.getValue(name);
   }
}