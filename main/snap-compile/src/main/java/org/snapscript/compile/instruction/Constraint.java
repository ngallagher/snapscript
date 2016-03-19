package org.snapscript.compile.instruction;

import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class Constraint{
   
   private Evaluation constraint;
   private Type type;
   
   public Constraint(Evaluation constraint) {
      this.constraint = constraint;
   }
   
   public Type create(Scope scope) throws Exception {
      if(type == null) {
         Value value = constraint.evaluate(scope, null);
         
         if(value == null) {
            throw new InternalStateException("Could not determine constraint");
         }
         type = value.getValue();
      }
      return type;
   }
}
