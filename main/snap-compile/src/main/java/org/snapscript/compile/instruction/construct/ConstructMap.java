package org.snapscript.compile.instruction.construct;

import java.util.LinkedHashMap;
import java.util.Map;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.TraceEvaluation;
import org.snapscript.core.TraceType;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.parse.StringToken;

public class ConstructMap implements Compilation {
   
   private final Evaluation construct;
   
   public ConstructMap(StringToken token) {
      this(null, token);
   }
   
   public ConstructMap(MapEntryList list) {
      this(list, null);
   }
   
   public ConstructMap(MapEntryList list, StringToken token) {
      this.construct = new CompileResult(list);
   }
   
   @Override
   public Evaluation compile(Context context, String resource, int line) throws Exception {
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getConstruct(resource, line);
      
      return new TraceEvaluation(interceptor, construct, trace);
   }
   
   private static class CompileResult implements Evaluation {

      private final MapEntryList list;
      
      public CompileResult(MapEntryList list) {
         this.list = list;
      }   
      
      @Override
      public Value evaluate(Scope scope, Object context) throws Exception { // this is rubbish
         Map map = new LinkedHashMap();
         
         if(list != null) {
            return list.evaluate(scope, context);
         }
         return ValueType.getTransient(map);
      }
   }
}