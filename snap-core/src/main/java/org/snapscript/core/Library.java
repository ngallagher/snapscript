package org.snapscript.core;

public interface Library {
   void include(Scope scope) throws Exception; // the compile should be done asynchronously
}
