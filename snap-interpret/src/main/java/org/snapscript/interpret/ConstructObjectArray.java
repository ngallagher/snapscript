package org.snapscript.interpret;

import org.snapscript.core.Holder;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class ConstructObjectArray implements Evaluation {
   
   private final ArrayConverter converter;
   private final Argument argument;
   private final TextLiteral type;
   
   public ConstructObjectArray(TextLiteral type) {
      this(type, null);         
   }
   
   public ConstructObjectArray(TextLiteral type, Argument argument) {
      this.converter = new ArrayConverter();
      this.argument = argument;
      this.type = type;
   }      
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception { // this is rubbish
      Value value = type.evaluate(scope, null);
      String name = value.getString();
      
      if(argument != null) {
         Value index = argument.evaluate(scope, left);
         Integer number = index.getInteger();
         Object array = converter.create(name, number);

         return new Holder(array);
      }
      Object array = converter.create(name, 0);
      return new Holder(array);
   }
}