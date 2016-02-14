package org.snapscript.compile.instruction.collection;

import java.util.List;

import org.snapscript.compile.instruction.Argument;
import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.core.InternalArgumentException;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.convert.ProxyWrapper;

public class ArrayIndex implements Evaluation {
   
   private final ListConverter converter;
   private final ProxyWrapper wrapper;
   private final Argument[] list;
   private final Argument first;
   private final Array array;
  
   public ArrayIndex(Array array, Argument first, Argument... list) {
      this.converter = new ListConverter();
      this.wrapper = new ProxyWrapper();
      this.array = array;        
      this.first = first;
      this.list = list;
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value index = first.evaluate(scope, null);
      Value value = array.evaluate(scope, left);
      Integer number = index.getInteger();
      List source = value.getValue();

      for(int i = 0; i < list.length; i++) {
         Argument argument = list[i];
         Object entry = source.get(number);
         int length = i + 1;
         
         if(!converter.accept(entry)) {
            throw new InternalArgumentException("Array contains only " + length + " dimensions");
         }
         Class type = entry.getClass();
         
         source = converter.convert(entry);
         index = argument.evaluate(scope, null);
         number = index.getInteger();
      }
      return new ListValue(wrapper, source, number);
   }
}