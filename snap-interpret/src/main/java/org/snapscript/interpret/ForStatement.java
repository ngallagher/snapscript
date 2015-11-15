package org.snapscript.interpret;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class ForStatement extends Statement {

   private final ConditionalList evaluation;
   private final Statement declaration;
   private final Evaluation assignment;
   private final Statement statement;
   
   public ForStatement(Statement declaration, ConditionalList evaluation, Statement statement) {
      this(declaration, evaluation, null, statement);
   }
   
   public ForStatement(Statement declaration, ConditionalList evaluation, Evaluation assignment, Statement statement) {
      this.declaration = declaration;
      this.evaluation = evaluation;
      this.assignment = assignment;
      this.statement = statement;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Scope compound = scope.getScope();
      
      declaration.execute(compound);
      
      while(true) {
         Value result = evaluation.evaluate(compound, null);
         Boolean value = result.getBoolean();
         
         if(value.booleanValue()) {
            Result next = statement.execute(compound);
            ResultFlow type = next.getFlow();
            
            if (type == ResultFlow.RETURN || type == ResultFlow.THROW) {
               return next;
            }
            if(type == ResultFlow.BREAK) {
               return new Result();
            }
         } else {
            return new Result();
         } 
         if(assignment != null) {
            assignment.evaluate(compound, null);
         }
      }
   }
}