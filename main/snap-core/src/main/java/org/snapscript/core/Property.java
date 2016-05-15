package org.snapscript.core;

import java.util.List;

public interface Property<T> extends Any {
   List<Annotation> getAnnotations();
   Type getType(); // declaring type
   Type getConstraint();
   String getName();
   int getModifiers();
   Object getValue(T source);
   void setValue(T source, Object value);
}
