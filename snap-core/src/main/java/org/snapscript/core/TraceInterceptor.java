package org.snapscript.core;

public interface TraceInterceptor {
   void before(Scope scope, Object instruction, int line);
   void after(Scope scope, Object instruction, int line);
}
