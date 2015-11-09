package org.snapscript.interpret.define;

import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class EnumList implements TypePart {
   
   private final EnumValue[] values;
   
   public EnumList(EnumValue... values){
      this.values = values;
   }

   @Override
   public Statement define(Scope scope, Statement statement, Type type) throws Exception {
      StatementCollector collector = new StatementCollector();
      int index = 0;
      
      for(EnumValue value : values) {
         value.define(scope, type, index++);
      }
      return collector;
   }   
}
