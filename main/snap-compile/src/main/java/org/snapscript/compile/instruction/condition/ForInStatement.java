package org.snapscript.compile.instruction.condition;

import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.collection.Iteration;
import org.snapscript.compile.instruction.collection.IterationConverter;
import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TraceStatement;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ForInStatement implements Compilation {
   
   private final Statement loop;
   
   public ForInStatement(Evaluation identifier, Evaluation collection, Statement body) {
      this.loop = new Delegate(identifier, collection, body);
   }
   
   @Override
   public Statement compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceStatement(analyzer, loop, trace);
   }
   
   private static class Delegate extends Statement {
   
      private final IterationConverter converter;
      private final NameExtractor extractor;
      private final Evaluation collection;
      private final Statement body;
   
      public Delegate(Evaluation identifier, Evaluation collection, Statement body) {
         this.extractor = new NameExtractor(identifier);
         this.converter = new IterationConverter();
         this.collection = collection;
         this.body = body;
      }
   
      @Override
      public Result execute(Scope scope) throws Exception { 
         Value list = collection.evaluate(scope, null);
         String name = extractor.extract(scope);
         Object object = list.getValue();
         Iteration iteration = converter.convert(object);
         Iterable iterable = iteration.getIterable(scope);
         State state = scope.getState();
         
         for (Object entry : iterable) {
            Value variable = state.getValue(name);
            
            if(variable == null) {
               Value value = ValueType.getReference(entry);
               state.addVariable(name, value);
            } else {
               variable.setValue(entry);
            }
            Result result = body.execute(scope);   
   
            if (result.isReturn() || result.isThrow()) {
               return result;
            }
            if (result.isBreak()) {
               return ResultType.getNormal();
            }
         }    
         return ResultType.getNormal();
      }
   }
}