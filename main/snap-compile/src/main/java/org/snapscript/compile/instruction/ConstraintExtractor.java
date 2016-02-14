package org.snapscript.compile.instruction;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class ConstraintExtractor {
   
   private final Constraint constraint;
   
   public ConstraintExtractor(Constraint constraint) {
      this.constraint = constraint;
   }

   public Type extract(Scope scope) throws Exception {
      if(constraint != null) {
         Value value = constraint.evaluate(scope, null);
         Module module = scope.getModule();
         String name = value.getString();
         Type type = module.getType(name);
         
         if(type == null) {
            throw new InternalStateException("Constraint '" + name +"' has not been imported");
         }
         return type;
      }
      return null;
   }
}
