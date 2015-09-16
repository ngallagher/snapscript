package org.snapscript.core.define;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.execute.Statement;

public class EnumList implements TypePart {
   
   private final EnumValue[] values;
   
   public EnumList(EnumValue... values){
      this.values = values;
   }

   @Override
   public Statement define(Scope scope, Statement statement, Type type) throws Exception {
      int index = 0;
      
      for(EnumValue value : values) {
         value.define(scope, type, index++);
      }
      return null;
   }   
}
