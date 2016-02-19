package org.snapscript.core.error;

import java.util.Iterator;

import org.snapscript.common.Stack;
import org.snapscript.core.Function;
import org.snapscript.core.Trace;

public class StackElementIterator {
   
   private final Iterator iterator;
   
   public StackElementIterator(Stack stack) {
      this.iterator = stack.iterator();
   }
   
   public boolean hasNext() {
      return iterator.hasNext();
   }
   
   public StackElement next() {
      while(iterator.hasNext()) {
         Object value = iterator.next();
         
         if(Trace.class.isInstance(value)) {
            Trace trace = (Trace)value;
            
            while(iterator.hasNext()) {
               Object next = iterator.next();
               
               if(Function.class.isInstance(next)) {
                  return new StackElement(trace, (Function)next);
               }
            }
            return new StackElement(trace);
         }
      }
      return null;
   }
   

}