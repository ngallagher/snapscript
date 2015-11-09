package org.snapscript.interpret;

import java.util.List;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;

public class ArrayIndex implements Evaluation {
   
   private final ListConverter converter;
   private final Argument[] list;
   private final Argument first;
   private final Array array;
  
   public ArrayIndex(Array array, Argument first, Argument... list) {
      this.converter = new ListConverter();
      this.array = array;        
      this.first = first;
      this.list = list;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value index = first.evaluate(scope, null);
      Value value = array.evaluate(scope, left);
      List source = value.getValue();
      Integer number = index.getInteger();
      
      for(int i = 0; i < list.length; i++) {
         Argument argument = list[i];
         Object entry = source.get(number);
         int length = i + 1;
         
         if(!converter.accept(entry)) {
            throw new IllegalArgumentException("Array contains only " + length + " dimensions");
         }
         Class type = entry.getClass();
         
         source = converter.convert(entry);
         index = argument.evaluate(scope, null);
         number = index.getInteger();
      }
      return new ListValue(source, number);
   }
}