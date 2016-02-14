package org.snapscript.core.exception;

import static org.snapscript.core.Reserved.IMPORT_SNAPSCRIPT;

public class CauseFormatter {

   public String format(Throwable original) {
      StackTraceElement[] list = original.getStackTrace();
      
      if(list.length > 0) {
         StringBuilder builder = new StringBuilder();
         
         for(StackTraceElement trace : list) {
            String source = trace.getClassName();
            
            if(!source.startsWith(IMPORT_SNAPSCRIPT)) { // not really correct, stripping required elements!
               builder.append("\tat ");
               builder.append(trace);
               builder.append("\n");
            }
         } 
         return builder.toString();
      }
      return null;
   }
}
