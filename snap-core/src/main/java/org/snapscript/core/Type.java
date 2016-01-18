package org.snapscript.core;

import java.util.List;

public interface Type {
   List<Property> getProperties();
   List<Function> getFunctions();
   List<Type> getTypes();
   Module getModule();
   Class getType();
   Type getEntry();
   String getName();
}
