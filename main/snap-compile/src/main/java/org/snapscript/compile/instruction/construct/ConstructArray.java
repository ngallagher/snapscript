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
import org.snapscript.core.TraceAnalyzer;
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
      this.construct = new CreateArray(type, list);
   }
   
   @Override
   public Evaluation compile(Context context, String resource, int line) throws Exception {
      TraceAnalyzer analyzer = context.getAnalyzer();
      Trace trace = TraceType.getConstruct(resource, line);
      
      return new TraceEvaluation(analyzer, construct, trace);
   }
   
   private static class CreateArray implements Evaluation {
   
      private final ArrayConverter converter;
      private final NameExtractor extractor;
      private final ArgumentList list;
      
      public CreateArray(Evaluation type, ArgumentList list) {
         this.extractor = new NameExtractor(type);
         this.converter = new ArrayConverter();
         this.list = list;
      }      
      
      @Override
      public Value evaluate(Scope scope, Object left) throws Exception { // this is rubbish
         String name = extractor.extract(scope);
         Module module = scope.getModule();
         Type type = module.getType(name);
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