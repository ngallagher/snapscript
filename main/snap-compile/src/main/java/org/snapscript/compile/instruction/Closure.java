package org.snapscript.compile.instruction;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Function;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class Closure implements Evaluation {
   
   private ParameterList parameters;
   private ClosureBuilder builder;
   private Statement closure;
   
   public Closure(ParameterList parameters, Statement statement){  
      this.builder = new ClosureBuilder(statement);
      this.parameters = parameters;
   }  
   
   public Closure(ParameterList parameters, Expression expression){
      this(parameters, null, expression);
   }
   
   public Closure(ParameterList parameters, Statement statement, Expression expression){
      this.closure = new ClosureStatement(statement, expression);
      this.builder = new ClosureBuilder(closure);
      this.parameters = parameters;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Signature signature = parameters.create(scope);
      Function function = builder.create(signature, scope); // creating new function each time
      
      return ValueType.getTransient(function);
   }
}