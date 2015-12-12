package org.snapscript.compile.instruction;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import org.snapscript.core.Context;
import org.snapscript.core.Result;
import org.snapscript.core.Statement;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.Type;
import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.parse.Line;

public class InstructionBuilder {
   
   private final AtomicInteger counter;
   private final Context context;

   public InstructionBuilder(Context context) {
      this.counter = new AtomicInteger();
      this.context = context;
   }
   
   // line numbers not working!!
   public Object create(Type type, Object[] arguments, Line line) throws Exception {
      return create(type, arguments, line, false);
   }

   public Object create(Type type, Object[] arguments, Line line, boolean trace) throws Exception {
      FunctionBinder binder = context.getBinder();
      TraceAnalyzer analyzer = context.getAnalyzer();
      Callable<Result> callable = binder.bind(null, type, "new", arguments);
      
      if(callable == null) {
         throw new IllegalStateException("No constructor for " + type);
      }
      Result result = callable.call();
      Object value = result.getValue();
      
      if(trace) {
         Class instruction = value.getClass();
         int key = counter.getAndIncrement();
         
         if(Statement.class.isAssignableFrom(instruction)) {
            Statement statement = (Statement)value;
            return new TraceStatement(analyzer, statement, line, key);
         }
         if(Evaluation.class.isAssignableFrom(instruction)) {
            Evaluation evaluation = (Evaluation)value;
            return new TraceEvaluation(analyzer, evaluation, line, key);
         }
      }
      return value;
   }
}
