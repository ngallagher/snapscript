package org.snapscript.compile.instruction;

import org.snapscript.core.Holder;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class ConstructObjectArray implements Evaluation {
   
   private final ArrayConverter converter;
   private final Argument[] arguments;
   private final TextLiteral type;

   public ConstructObjectArray(TextLiteral type, Argument... arguments) {
      this.converter = new ArrayConverter();
      this.arguments = arguments;
      this.type = type;
   }      
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception { // this is rubbish
      Value value = type.evaluate(scope, null);
      String name = value.getString();
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
            
            return new Holder(array);
         }
         if(arguments.length == 2) {
            int first = dimensions[0]; 
            int second = dimensions[1];
            Object array = converter.create(entry, first, second);
            
            return new Holder(array);
         }
         if(arguments.length == 3) {
            int first = dimensions[0]; 
            int second = dimensions[1];
            int third = dimensions[2];
            Object array = converter.create(entry, first, second, third);
            
            return new Holder(array);
         }
         throw new IllegalArgumentException("Maximum or three dimensions exceeded");
      }
      Object array = converter.create(entry, 0);
      return new Holder(array);
   }
}