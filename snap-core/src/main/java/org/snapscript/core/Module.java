package org.snapscript.core;

import java.io.InputStream;
import java.util.List;

public interface Module {
   Scope getScope();
   Context getContext();
   Type getType(Class type);   
   Type getType(String name);
   Type addType(String name);  
   Type addImport(String name, String module); 
   InputStream getResource(String path);
   List<Function> getFunctions();
   String getName();
}