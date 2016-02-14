package org.snapscript.core.error;

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class RuntimeError extends RuntimeException implements Error {

   @Override
   public void printStackTrace(PrintStream stream) {
      String description = getDescription();

      stream.println(description);
      stream.flush();
   }

   @Override
   public void printStackTrace(PrintWriter writer) {
      String description = getDescription();

      writer.println(description);
      writer.flush();
   }
}
