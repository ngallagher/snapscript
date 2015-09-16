package org.snapscript.core.execute;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
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
         String type = value.getString();
         
         return new Holder(name, type);
      }
      return new Holder(name);
   }
}