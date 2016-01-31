package org.snapscript.compile.instruction.variable;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleBuilder;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class LocalResolver implements ValueResolver<Object> {
   
   private final String name;
   
   public LocalResolver(String name) {
      this.name = name;
   }
   
   @Override
   public Value resolve(Scope scope, Object left) {
      State state = scope.getState();
      Value variable = state.getValue(name);
      
      if(variable == null) { 
         Context context = scope.getContext();
         Module module = scope.getModule();
         Type type = module.getType(name);
         
         if(type == null) {
            ModuleBuilder builder = context.getBuilder();
            Object result = builder.resolve(name);
            
            if(result != null) {
               return ValueType.getTransient(result);
            }
            return null;
         }
         return ValueType.getTransient(type);
      }
      return variable;
   }
}