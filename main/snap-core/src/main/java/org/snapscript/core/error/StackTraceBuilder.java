package org.snapscript.core.error;

import static org.snapscript.core.Reserved.IMPORT_SNAPSCRIPT;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.common.Stack;

public class StackTraceBuilder {
   
   private final StackTraceElement[] empty;
   
   public StackTraceBuilder() {
      this.empty = new StackTraceElement[]{};
   }
   
   public StackTraceElement[] create(Stack stack) {
      List<StackTraceElement> list = new ArrayList<StackTraceElement>();
      StackElementIterator iterator = new StackElementIterator(stack);
      Exception exception = new Exception();
      
      while(iterator.hasNext()) {
         StackElement next = iterator.next();
         
         if(next != null) {
            StackTraceElement trace = next.build();
         
            if(trace != null) {
               list.add(trace);
            }
         }
      }
      StackTraceElement[] array = exception.getStackTrace();
      
      for(StackTraceElement trace : array) {
         String source = trace.getClassName();
         
         if(!source.startsWith(IMPORT_SNAPSCRIPT)) { // not really correct, stripping required elements!
            list.add(trace);
         }
      } 
      return list.toArray(empty);
   }

}
