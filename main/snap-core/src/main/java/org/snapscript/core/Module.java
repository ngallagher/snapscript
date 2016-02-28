package org.snapscript.core;

import java.io.InputStream;
import java.util.List;

public interface Module {
   Scope getScope();
   Context getContext();
   Type getType(Class type);   
   Type getType(String name);
   Type addType(String name);  
   Module addImport(String module);
   Type addImport(String module, String name);  
   Type addImport(String module, String name, String alias);  
   InputStream getResource(String path);
   List<Function> getFunctions();
   List<Type> getTypes();
   String getName();
}