package org.snapscript.core;

public interface Scope {
   Type getType();
   /*
    * 1) If called on a class then use the class scope only [after u get variables]
    * 2) If called in script use script scope only
    * 3) If called in module use module scope only
    */
   Scope getInner();
   Scope getOuter();
   Module getModule();
   Context getContext();    
   State getState();
}
