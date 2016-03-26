package org.snapscript.core;

public interface Instance extends Scope {
   Instance getInner(); 
   Instance getOuter();
   Scope getScope();
   int getDepth();
}
