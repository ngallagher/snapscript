package org.snapscript.core.extend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateExtension {

   public DateExtension(){
      super();
   }
   
   public String format(Date date, String pattern) {
      return format(date, pattern, null);
   }
   
   public String format(Date date, String pattern, TimeZone zone) {
      DateFormat format = new SimpleDateFormat(pattern);
      
      if(zone != null) {
         format.setTimeZone(zone);
      }
      return format.format(date);
   }
}
