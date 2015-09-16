package org.snapscript.core;

import java.util.List;

public interface Module {  
   Context getContext();
   List<Function> getFunctions();
   Type getType(Class type);   
   Type getType(String name);
   Type addType(String name);   
   Scope getScope(); // scope here is confined to the module, it should inherit the callers scope
}