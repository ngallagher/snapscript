package org.snapscript.compile.instruction.define;

import org.snapscript.core.Initializer;
import org.snapscript.core.Type;

public class EnumList implements TypePart {
   
   private final EnumValue[] values;
   
   public EnumList(EnumValue... values){
      this.values = values;
   }

   @Override
   public Initializer compile(Initializer statement, Type type) throws Exception {
      InitializerCollector collector = new InitializerCollector();
      int index = 0;
      
      for(EnumValue value : values) {
         Initializer initializer = value.compile(type, index++);
         
         if(initializer != null) {
            collector.update(initializer);
         }
      }
      return collector;
   }   
}
