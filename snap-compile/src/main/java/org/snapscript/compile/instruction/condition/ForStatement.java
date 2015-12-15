package org.snapscript.compile.instruction.condition;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class ForStatement extends Statement {

   private final Evaluation evaluation;
   private final Statement declaration;
   private final Evaluation assignment;
   private final Statement statement;
   
   public ForStatement(Statement declaration, Evaluation evaluation, Statement statement) {
      this(declaration, evaluation, null, statement);
   }
   
   public ForStatement(Statement declaration, Evaluation evaluation, Evaluation assignment, Statement statement) {
      this.declaration = declaration;
      this.evaluation = evaluation;
      this.assignment = assignment;
      this.statement = statement;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Scope compound = scope.getInner();
      
      declaration.execute(compound);
      
      while(true) {
         Value result = evaluation.evaluate(compound, null);
         Boolean value = result.getBoolean();
         
         if(value.booleanValue()) {
            Result next = statement.execute(compound);
            ResultType type = next.getType();
            
            if (type.isReturn() || type.isThrow()) {
               return next;
            }
            if(type.isBreak()) {
               return ResultType.getNormal();
            }
         } else {
            return ResultType.getNormal();
         } 
         if(assignment != null) {
            assignment.evaluate(compound, null);
         }
      }
   }
}