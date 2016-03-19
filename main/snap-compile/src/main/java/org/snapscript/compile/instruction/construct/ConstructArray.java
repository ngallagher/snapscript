package org.snapscript.compile.instruction.construct;

import java.util.List;

import org.snapscript.compile.instruction.ArgumentList;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.collection.ArrayConverter;
import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceInterceptor;
import org.snapscript.core.TraceEvaluation;
import org.snapscript.core.TraceType;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ConstructArray implements Compilation {
   
   private final Evaluation construct;
   
   public ConstructArray(Evaluation type) {
      this(type, null);
   }
   
   public ConstructArray(Evaluation type, ArgumentList list) {
      this.construct = new CompileResult(type, list);
   }
   
   @Override
   public Evaluation compile(Module module, int line) throws Exception {
      Context context = module.getContext();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = TraceType.getConstruct(module, line);
      
      return new TraceEvaluation(interceptor, construct, trace);
   }
   
   private static class CompileResult implements Evaluation {
   
      private final ArrayConverter converter;
      private final Evaluation reference;
      private final ArgumentList list;
      
      public CompileResult(Evaluation reference, ArgumentList list) {
         this.converter = new ArrayConverter();
         this.reference = reference;
         this.list = list;
      }      
      
      @Override
      public Value evaluate(Scope scope, Object left) throws Exception { // this is rubbish
         Value value = reference.evaluate(scope, null);
         Type type = value.getValue();
         Class entry = type.getType();
         
         if(list != null) {
            Value reference = list.evaluate(scope, left);
            Object[] arguments = reference.getValue();
            int length = arguments.length;
            Object array = converter.create(entry, length);
            
            if(length > 0) {
               List list = converter.convert(array);
               
               for(int i = 0; i < length; i++) {
                  list.set(i, arguments[i]);
               } 
            }
            return ValueType.getTransient(array);
         }
         Object array = converter.create(entry, 0);
         return ValueType.getTransient(array);
      }
   }
}