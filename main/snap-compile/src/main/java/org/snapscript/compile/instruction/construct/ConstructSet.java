package org.snapscript.compile.instruction.construct;

import java.util.LinkedHashSet;
import java.util.Set;

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
import org.snapscript.parse.StringToken;

public class ConstructSet implements Compilation {
   
   private final Evaluation construct;
  
   public ConstructSet(StringToken token) {
      this(null, token);
   }
   
   public ConstructSet(ArgumentList arguments) {
      this(arguments, null);
   }
   
   public ConstructSet(ArgumentList arguments, StringToken token) {
      this.construct = new Delegate(arguments);
   }
   
   @Override
   public Evaluation compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getConstruct(resource, line);
      
      return new TraceEvaluation(analyzer, construct, trace);
   }
   
   private static class Delegate implements Evaluation {

      private final ArgumentList arguments;
      private final ProxyWrapper wrapper;
      
      public Delegate(ArgumentList arguments) {
         this.wrapper = new ProxyWrapper();
         this.arguments = arguments;
      }   
      
      @Override
      public Value evaluate(Scope scope, Object context) throws Exception { // this is rubbish
         Set result = new LinkedHashSet();
         
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