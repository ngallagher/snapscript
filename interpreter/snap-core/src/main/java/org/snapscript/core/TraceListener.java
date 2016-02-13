package org.snapscript.core;

public interface TraceListener {
   void before(Scope scope, Trace trace);
   void after(Scope scope, Trace trace);
}
