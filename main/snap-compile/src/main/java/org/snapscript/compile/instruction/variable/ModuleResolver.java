package org.snapscript.compile.instruction.variable;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Value;

public class ModuleResolver implements ValueResolver<Module> {
   
   private final String name;
   
   public ModuleResolver(String name) {
      this.name = name;
   }
   
   @Override
   public Value resolve(Scope scope, Module left) {
      Scope inner = left.getScope();
      State state = inner.getState();
      
      return state.getValue(name);
   }
}