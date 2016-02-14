package org.snapscript.compile.instruction;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.error.ErrorCauseExtractor;

public class TryCatchStatement extends Statement {
   
   private final ErrorCauseExtractor extractor;
   private final ConstraintChecker checker;
   private final Statement statement;
   private final Parameter parameter;
   private final Statement handle;
   private final Statement finish;
   
   public TryCatchStatement(Statement statement, Statement finish) {
      this(statement, null, null, finish);
   }   
   
   public TryCatchStatement(Statement statement, Parameter parameter, Statement handle) {
      this(statement, parameter, handle, null);
   }   
   
   public TryCatchStatement(Statement statement, Parameter parameter, Statement handle, Statement finish) {
      this.extractor = new ErrorCauseExtractor();
      this.checker = new ConstraintChecker();
      this.statement = statement;
      this.parameter = parameter;  
      this.handle = handle;
      this.finish = finish;
   }    

   @Override
   public Result execute(Scope scope) throws Exception {
      Result result = handle(scope);
      
      try {
         if(result.isThrow()) {
            return handle(scope, result);            
         }   
      } finally {
         if(finish != null) {
            finish.execute(scope);
         }
      }
      return result;
   }
   
   private Result handle(Scope scope) throws Exception {
      try {
         return statement.execute(scope);
      } catch(Exception cause) {
         return ResultType.getThrow(cause);
      }
   }

   private Result handle(Scope scope, Result result) throws Exception {
      Object value = result.getValue();
      
      if(parameter != null) {
         Value reference = parameter.evaluate(scope, null);
         Type constraint = reference.getConstraint();
         String name = reference.getString();

         if(value != null) {
            if(constraint != null) {
               Object cause = extractor.extract(scope, value);
               
               if(!checker.compatible(scope, cause, constraint)) {
                  return result;
               }
            }
            Scope compound = scope.getInner();
            State state = compound.getState();
            Value constant = ValueType.getConstant(value);
            
            state.addConstant(name, constant);
               
            if(handle != null) {
               return handle.execute(compound);
            }
         }
      }
      return result;
   }

}
