package org.snapscript.compile.instruction;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Transient;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class Parameter implements Evaluation{
   
   private final Evaluation evaluation;
   private final Constraint constraint;
   
   public Parameter(Evaluation evaluation){
      this(evaluation, null);
   }
   
   public Parameter(Evaluation evaluation, Constraint constraint){
      this.evaluation = evaluation;
      this.constraint = constraint;
   }
   
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value reference = evaluation.evaluate(scope, left);
      String name = reference.getString();
      
      if(constraint != null && name != null) {
         Value value = constraint.evaluate(scope, left);  
         String alias = value.getString();
         Module module = scope.getModule();
         Type type = module.getType(alias);
         
         if(type == null) {
            throw new IllegalStateException("Constraint '" + alias + "' for '" +name + "' was not imported");
         }
         return new Transient(name, type);
      }
      return new Transient(name);
   }
}