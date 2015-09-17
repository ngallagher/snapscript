package org.snapscript.core.execute;

import java.util.List;

import org.snapscript.core.CompoundScope;
import org.snapscript.core.Constant;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.ResultFlow;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class TryCatchStatement extends Statement {
   
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
      this.statement = statement;
      this.parameter = parameter;  
      this.handle = handle;
      this.finish = finish;
   }    

   @Override
   public Result execute(Scope scope) throws Exception {
      Result result = statement.execute(scope);
      ResultFlow type = result.getFlow();
      
      try {
         if(type == ResultFlow.THROW) {
            return handle(scope, result);            
         }
      } finally {
         if(finish != null) {
            finish.execute(scope);
         }
      }
      return result;
   }
   
   private Result handle(Scope scope, Result result) throws Exception {
      Object value = result.getValue();
      
      if(parameter != null) {
         Value reference = parameter.evaluate(scope, null);
         String constraint = reference.getConstraint();
         String name = reference.getString();

         if(value != null) {
            Module module = scope.getModule();
            Class type = value.getClass();
            Type actual = module.getType(type);
            
            if(constraint != null) {
               Type require = module.getType(constraint);
               
               if(require != actual) {
                  List<Type> compatible = actual.getTypes();
               
                  if(!compatible.contains(require)) {
                     return result;
                  }
               }
            }
            Scope compound = new CompoundScope(scope);
            Constant constant = new Constant(value, name);
            
            compound.addConstant(name, constant);
               
            if(handle != null) {
               return handle.execute(compound);
            }
         }
      }
      return result;
   }

}
