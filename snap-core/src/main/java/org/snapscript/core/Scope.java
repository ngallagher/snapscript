package org.snapscript.core;

public interface Scope {
   Type getType();
   Scope getScope();
   Module getModule();
   Context getContext();    
   State getState();
}
