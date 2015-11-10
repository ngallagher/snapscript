package org.snapscript.interpret.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class EnumList implements TypePart {
   
   private final EnumValue[] values;
   
   public EnumList(EnumValue... values){
      this.values = values;
   }

   @Override
   public Initializer define(Scope scope, Initializer statement, Type type) throws Exception {
      InitializerCollector collector = new InitializerCollector();
      int index = 0;
      
      for(EnumValue value : values) {
         value.define(scope, type, index++);
      }
      return collector;
   }   
}
