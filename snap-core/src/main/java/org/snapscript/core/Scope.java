package org.snapscript.core;

public interface Scope {
   Type getType();
   Scope getInner();
   Scope getOuter();
   Module getModule();
   Context getContext();    
   State getState();
}
