package org.snapscript.core;

import java.util.List;

public interface Type extends Any {
   List<Annotation> getAnnotations();
   List<Property> getProperties();
   List<Function> getFunctions();
   List<Type> getTypes();
   Module getModule();
   Scope getScope();
   Class getType();
   Type getEntry();
   String getName();
   int getOrder();
}
