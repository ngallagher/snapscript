package org.snapscript.interpret;

import java.util.List;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class ArrayIndex implements Evaluation {
   
   private final Argument argument;
   private final Array array;
  
   public ArrayIndex(Array array, Argument argument) {
      this.argument = argument;
      this.array = array;        
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value index = argument.evaluate(scope, null);
      Value value = array.evaluate(scope, left);
      List list = value.getValue();
      Integer number = index.getInteger();
      
      return new ListValue(list, number);
   }
}