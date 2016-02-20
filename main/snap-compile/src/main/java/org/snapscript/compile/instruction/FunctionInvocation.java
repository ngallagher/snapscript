package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.dispatch.InvocationBinder;
import org.snapscript.compile.instruction.dispatch.InvocationDispatcher;
import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceEvaluation;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;

public class FunctionInvocation implements Compilation {
   
   private final Evaluation invocation;
   
   public FunctionInvocation(Evaluation function) {
      this(function, null);
   }
   
   public FunctionInvocation(Evaluation function, ArgumentList list) {
      this.invocation = new CompileResult(function, list);
   }
   
   @Override
   public Evaluation compile(Context context, String resource, int line) throws Exception {
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getInvoke(resource, line);
      
      return new TraceEvaluation(interceptor, invocation, trace);
   }
   
   private static class CompileResult implements Evaluation {
   
      private final InvocationBinder dispatcher;
      private final NameExtractor extractor;
      private final ArgumentList list;
      
      public CompileResult(Evaluation function, ArgumentList list) {
         this.extractor = new NameExtractor(function);
         this.dispatcher = new InvocationBinder();
         this.list = list;
      }
      
      public Value evaluate(Scope scope, Object left) throws Exception {
         InvocationDispatcher handler = dispatcher.bind(scope, left);
         String name = extractor.extract(scope);      
         
         if(list != null) {
            Value array = list.evaluate(scope, null); // arguments have no left hand side
            Object[] arguments = array.getValue();
            
            return handler.dispatch(name, arguments);
         }
         return handler.dispatch(name); 
      }
   }
}