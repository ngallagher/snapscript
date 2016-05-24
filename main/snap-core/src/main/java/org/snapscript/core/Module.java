package org.snapscript.core;

import java.io.InputStream;
import java.util.List;

public interface Module extends Any{
   Scope getScope();
   Context getContext();
   ImportManager getManager();
   Type getType(Class type);   
   Type getType(String name);
   Type addType(String name);
   Module getModule(String module); 
   InputStream getResource(String path);
   List<Annotation> getAnnotations();
   List<Function> getFunctions();
   List<Type> getTypes();
   String getPath();
   String getName();
}