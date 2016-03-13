package org.snapscript.engine;

import org.simpleframework.xml.filter.Filter;

public class ProcessEngineFilter implements Filter {

   @Override
   public String replace(String text) {
      String value = System.getenv(text);
      
      if(value == null) {
         value = System.getProperty(text);
      }
      return text;
   }
}
