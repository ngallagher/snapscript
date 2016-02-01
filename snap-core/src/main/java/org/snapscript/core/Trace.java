package org.snapscript.core;

public interface Trace {
   Class getInstruction();
   String getResource();
   int getLine();
   int getKey();
}
