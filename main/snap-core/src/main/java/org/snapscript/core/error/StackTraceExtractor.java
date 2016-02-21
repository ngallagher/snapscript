package org.snapscript.core.error;

import static org.snapscript.core.Reserved.IMPORT_JAVA;
import static org.snapscript.core.Reserved.IMPORT_SNAPSCRIPT;

import java.util.ArrayList;
import java.util.List;

public class StackTraceExtractor {
   
   private static final int DEBUG_DEPTH = 0; // set to two to debug
   
   private final int depth;
   
   public StackTraceExtractor() {
      this(DEBUG_DEPTH);
   }
   
   public StackTraceExtractor(int depth) {
      this.depth = depth;
   }

   public List<StackTraceElement> extract(Throwable cause) {
      List<StackTraceElement> list = new ArrayList<StackTraceElement>();
   
      if(cause != null) {
         StackTraceElement[] elements = cause.getStackTrace();
         
         for(int i = 0; i < depth; i++) {
            StackTraceElement element = elements[i];
            String source = element.getClassName();
            
            if(source.startsWith(IMPORT_SNAPSCRIPT)) { 
               list.add(element);
            } else if(source.startsWith(IMPORT_JAVA)) {
               list.add(element);
            } else {
               return list;
            }
         } 
      }
      return list;
   }
}
