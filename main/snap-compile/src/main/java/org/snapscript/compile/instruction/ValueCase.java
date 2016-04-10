package org.snapscript.compile.instruction;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Statement;

public class ValueCase implements Case {

   private final Evaluation evaluation;
   private final Statement statement;
   
   public ValueCase(Evaluation evaluation, Statement... statements) {
      this.statement = new CompoundStatement(statements);
      this.evaluation = evaluation;
   }
   
   @Override
   public Evaluation getEvaluation(){
      return evaluation;
   }
   
   @Override
   public Statement getStatement(){
      return statement;
   }
}
