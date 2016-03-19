package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ConstraintExtractor {
   
   private final Constraint constraint;
   
   public ConstraintExtractor(Constraint constraint) {
      this.constraint = constraint;
   }

   public Type extract(Scope scope) throws Exception {
      if(constraint != null) {
         return constraint.create(scope);
      }
      return null;
   }
}
