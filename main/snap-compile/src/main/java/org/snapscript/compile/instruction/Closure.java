package org.snapscript.compile.instruction;

import static org.snapscript.core.Reserved.METHOD_CLOSURE;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Function;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class Closure implements Evaluation {
   
   private ParameterList parameters;
   private FunctionBuilder builder;
   private Statement closure;
   private Function function;
   
   public Closure(ParameterList parameters, Statement statement){  
      this.builder = new FunctionBuilder(statement);
      this.parameters = parameters;
   }  
   
   public Closure(ParameterList parameters, Expression expression){
      this(parameters, null, expression);
   }
   
   public Closure(ParameterList parameters, Statement statement, Expression expression){
      this.closure = new ClosureStatement(statement, expression);
      this.builder = new FunctionBuilder(closure);
      this.parameters = parameters;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      if(function == null) {
         Signature signature = parameters.create(scope);
         
         function = builder.create(signature, METHOD_CLOSURE);
      }
      return ValueType.getTransient(function);
   }
}