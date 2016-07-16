package org.snapscript.core.instance;

import org.snapscript.core.Scope;

public interface Instance extends Scope {
   Instance getInstance();
   void setInstance(Instance instance);
   Instance getInner(); 
   Instance getOuter();
}
