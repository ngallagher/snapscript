package org.snapscript.core;

public interface Instance extends Scope {
   Instance getInner(); 
   Instance getOuter();
   Instance getInstance();
   void setInstance(Instance instance);
   int getDepth();
}
