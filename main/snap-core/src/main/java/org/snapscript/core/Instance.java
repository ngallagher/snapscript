package org.snapscript.core;

public interface Instance extends Scope {
   Instance getInstance();
   void setInstance(Instance instance);
   Instance getInner(); 
   Instance getOuter();
}
