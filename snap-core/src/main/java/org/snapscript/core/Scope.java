package org.snapscript.core;

public interface Scope {
   Type getType();
   Scope getScope();
   // module defines the imports and classes available in a scope/source file
   Module getModule();
   Context getContext();    
   State getState();
}
