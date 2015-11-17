package org.snapscript.core;

import java.util.List;

public interface Module {
   Scope getScope();
   Context getContext();
   Type getType(Class type);   
   Type getType(String name);
   Type addType(String name);  
   Type addImport(String name, String module); 
   List<Function> getFunctions();
   String getName();
}