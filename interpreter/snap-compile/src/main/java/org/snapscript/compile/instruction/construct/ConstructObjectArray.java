package org.snapscript.compile.instruction.construct;

import org.snapscript.compile.instruction.Argument;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.NameExtractor;
import org.snapscript.compile.instruction.collection.ArrayConverter;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ConstructObjectArray implements Evaluation {
   
   private final ArrayConverter converter;
   private final NameExtractor extractor;
   private final Argument[] arguments;

   public ConstructObjectArray(Evaluation type, Argument... arguments) {
      this.extractor = new NameExtractor(type);
      this.converter = new ArrayConverter();
      this.arguments = arguments;
   }      
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception { // this is rubbish
      String name = extractor.extract(scope);
      Module module = scope.getModule();
      Type type = module.getType(name);
      Class entry = type.getType();
      
      if(arguments.length > 0) {
         int[] dimensions = new int[] {0,0,0};
         
         for(int i = 0; i < arguments.length; i++){
            Argument argument = arguments[i];
            Value index = argument.evaluate(scope, left);
            Integer number = index.getInteger();
         
            dimensions[i] = number;
         }
         if(arguments.length == 1) {
            int size = dimensions[0];   
            Object array = converter.create(entry, size);
            
            return ValueType.getTransient(array);
         }
         if(arguments.length == 2) {
            int first = dimensions[0]; 
            int second = dimensions[1];
            Object array = converter.create(entry, first, second);
            
            return ValueType.getTransient(array);
         }
         if(arguments.length == 3) {
            int first = dimensions[0]; 
            int second = dimensions[1];
            int third = dimensions[2];
            Object array = converter.create(entry, first, second, third);
            
            return ValueType.getTransient(array);
         }
         throw new IllegalArgumentException("Maximum or three dimensions exceeded");
      }
      Object array = converter.create(entry, 0);
      return ValueType.getTransient(array);
   }
}