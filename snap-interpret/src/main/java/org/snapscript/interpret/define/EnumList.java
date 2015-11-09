package org.snapscript.interpret.define;

import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

/**
 * Order of initialisation needs to be as follows:
 * 
 * 1) Initialize ENUM list
 * 2) Initialize statics
 * 
 * 
 * 
 * 
 */
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
         Statement entry = value.define(scope, type, index++);
         
         if(entry != null) {
            collector.update(entry);
         }
      }
      return collector;
   }   
}
