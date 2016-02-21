package org.snapscript.core.error;

import static org.snapscript.core.Reserved.IMPORT_SNAPSCRIPT;

import java.util.List;

import org.snapscript.common.Stack;

public class StackTraceBuilder {
   
   private final OriginTraceExtractor extractor;
   private final StackElementConverter builder;
   private final StackTraceElement[] empty;
   
   public StackTraceBuilder() {
      this.extractor = new OriginTraceExtractor();
      this.builder = new StackElementConverter();
      this.empty = new StackTraceElement[]{};
   }
   
   public StackTraceElement[] create(Stack stack) {
      return create(stack, null);
   }
   
   public StackTraceElement[] create(Stack stack, Throwable origin) {
      Thread thread = Thread.currentThread();
      List<StackTraceElement> list = extractor.extract(origin); // debug cause
      List<StackTraceElement> context = builder.create(stack); // script stack
      StackTraceElement[] actual = thread.getStackTrace(); // native stack
      
      for(StackTraceElement trace : context) {
         list.add(trace);
      }
      for(StackTraceElement trace : actual) {
         String source = trace.getClassName();
         
         if(!source.startsWith(IMPORT_SNAPSCRIPT)) { // not really correct, stripping required elements!
            list.add(trace);
         }
      } 
      return list.toArray(empty);
   }
}
