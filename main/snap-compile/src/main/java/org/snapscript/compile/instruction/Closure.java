package org.snapscript.compile.instruction;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Function;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class Closure implements Evaluation {
   
   private ClosureParameterList parameters;
   private ClosureBuilder builder;
   private AtomicBoolean compile;
   private Statement closure;
   
   public Closure(ClosureParameterList parameters, Statement statement){  
      this(parameters, statement, null);
   }  
   
   public Closure(ClosureParameterList parameters, Expression expression){
      this(parameters, null, expression);
   }
   
   public Closure(ClosureParameterList parameters, Statement statement, Expression expression){
      this.closure = new ClosureStatement(statement, expression);
      this.builder = new ClosureBuilder(closure);
      this.compile = new AtomicBoolean();
      this.parameters = parameters;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Signature signature = parameters.create(scope);
      Function function = builder.create(signature, scope); // creating new function each time
      
      if(compile.compareAndSet(false, true)) {
         closure.compile(scope);
      }
      return ValueType.getTransient(function);
   }
}