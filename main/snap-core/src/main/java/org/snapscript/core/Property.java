package org.snapscript.core;

public interface Property<T> extends Any {
   Type getType(); // declaring type
   Type getConstraint();
   String getName();
   int getModifiers();
   Object getValue(T source);
   void setValue(T source, Object value);
}
