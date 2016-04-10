package org.snapscript.compile.instruction;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;

public class SwitchStatement extends Statement {

   private final Evaluation condition;
   private final Case[] cases;
   
   public SwitchStatement(Evaluation condition, Case... cases) {
      this.condition = condition;
      this.cases = cases;
   }
   
   @Override
   public Result compile(Scope scope) throws Exception {
      Result last = ResultType.getNormal();
      
      for(int i = 0; i < cases.length; i++){
         Statement statement = cases[i].getStatement();
         Result result = statement.compile(scope);
         
         if(!result.isNormal()){
            return result;
         }
         last = result;
      }
      return last;
   }
   
   @Override
   public Result execute(Scope scope) throws Exception {
      Value value = condition.evaluate(scope, null);
      Object left = value.getValue();
      
      for(int i = 0; i < cases.length; i++){
         Evaluation evaluation = cases[i].getEvaluation();
         
         if(evaluation == null) {
            Statement statement = cases[i].getStatement();
            Result result = statement.execute(scope);
            
            if(result.isBreak()) {
               return ResultType.getNormal();
            }
            if(!result.isNormal()) {
               return result;      
            }
            return ResultType.getNormal();
         }
         Value other = evaluation.evaluate(scope, null);
         Object right = other.getValue();

         if(left.equals(right)) {
            for(int j = i; j < cases.length; j++) {
               Statement statement = cases[j].getStatement();
               Result result = statement.execute(scope);

               if(result.isBreak()) {
                  return ResultType.getNormal();
               }
               if(!result.isNormal()) {
                  return result;      
               }
            }   
         }  
      }
      return ResultType.getNormal();
   }
}
