package org.snapscript.core.error;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.snapscript.core.Bug;
import org.snapscript.core.Trace;

@Bug("Use java Error with ThrowError and ThrowObjectError ThrowExceptionError InternalError")
public interface Error {
   void printStackTrace();
   void printStackTrace(PrintStream stream);
   void printStackTrace(PrintWriter writer);
   String getDescription();
   String getStack();
   String getChain();
   Object getOriginal();
   Trace getOrigin();
}
