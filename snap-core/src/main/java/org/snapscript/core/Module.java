package org.snapscript.core;

import java.util.List;

public interface Module {
   Context getContext();
   Type getType(Class type);   
   Type getType(String name);
   Type addType(String name);  
   Type addImport(String name, String module); 
   List<Function> getFunctions();
   Scope getScope(); // scope here is confined to the module, it should inherit the callers scope
   String getName();
}