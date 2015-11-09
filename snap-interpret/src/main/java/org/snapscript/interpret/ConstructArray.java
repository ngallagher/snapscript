package org.snapscript.interpret;

import java.util.List;

import org.snapscript.core.Holder;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class ConstructArray implements Evaluation {
   
   private final ArrayConverter converter;
   private final ArgumentList list;
   private final TextLiteral type;
   
   public ConstructArray(TextLiteral type) {
      this(type, null);         
   }
   
   public ConstructArray(TextLiteral type, ArgumentList list) {
      this.converter = new ArrayConverter();
      this.type = type;
      this.list = list;
   }      
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception { // this is rubbish
      Value value = type.evaluate(scope, null);
      String name = value.getString();
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
         return new Holder(array);
      }
      Object array = converter.create(entry, 0);
      return new Holder(array);
   }
}