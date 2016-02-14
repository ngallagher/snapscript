package org.snapscript.compile.instruction.collection;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class Array implements Evaluation {
   
   private final ArrayConverter converter;
   private final Evaluation variable;
   
   public Array(Evaluation variable) {
      this.converter = new ArrayConverter();
      this.variable = variable;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      Value value = variable.evaluate(scope, left);
      Object list = value.getValue();
      Class type = list.getClass();
      
      if(type.isArray()) {
         list = converter.convert(list);
      }     
      return ValueType.getTransient(list);
   }  
   

}