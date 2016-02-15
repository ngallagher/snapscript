package org.snapscript.compile.instruction.construct;

import static org.snapscript.core.TraceType.CONSTRUCT;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceAnalyzer;
import org.snapscript.core.TraceEvaluation;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.convert.ProxyWrapper;
import org.snapscript.parse.Line;
import org.snapscript.parse.StringToken;

public class ConstructList implements Compilation {
   
   private final Evaluation construct;
   
   public ConstructList(StringToken token) {
      this(null, token);
   }
   
   public ConstructList(ArgumentList arguments) {
      this(arguments, null);
   }
   
   public ConstructList(ArgumentList arguments, StringToken token) {
      this.construct = new CreateList(arguments);
   }
   
   @Override
   public Evaluation compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getNormal(resource, line);
      
      return new TraceEvaluation(analyzer, construct, trace);
   }
   
   private static class CreateList implements Evaluation {
      
      private final ArgumentList arguments;
      private final ProxyWrapper wrapper;
      
      public CreateList(ArgumentList arguments) {
         this.wrapper = new ProxyWrapper();
         this.arguments = arguments;
      }
      
      @Override
      public Value evaluate(Scope scope, Object context) throws Exception { // this is rubbish
         List result = new ArrayList();
         
         if(arguments != null) {
            Value reference = arguments.evaluate(scope, context);
            Object[] array = reference.getValue();
            
            for(Object value : array) {
               Object proxy = wrapper.toProxy(value);
               result.add(proxy);
            }         
         }   
         return ValueType.getTransient(result);
      }
   }
}