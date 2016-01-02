package org.snapscript.ide;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionBuilder {

   public static String build(Exception cause) {
      StringWriter w = new StringWriter();
      PrintWriter p = new PrintWriter(w);
      cause.printStackTrace(p);
      p.flush();
      p.close();
      return w.toString();
      
   }
}
