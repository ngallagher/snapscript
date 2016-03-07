package org.snapscript.core;

import java.io.InputStream;
import java.util.List;

public interface Module {
   Scope getScope();
   Context getContext();
   Type getType(Class type);   
   Type getType(String name);
   Type addType(String name);
   Module getModule(String module);
   Module addModule(String module);
   Module addModule(String module, String name);
   Module addModule(String module, String name, String alias);
   Type addType(String module, String name);  
   Type addType(String module, String name, String alias);  
   InputStream getResource(String path);
   List<Function> getFunctions();
   List<Type> getTypes();
   String getPath();
   String getName();
}