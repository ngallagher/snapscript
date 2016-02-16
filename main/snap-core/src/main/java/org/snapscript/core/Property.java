package org.snapscript.core;

public interface Property<T> {
   Type getType(); // declaring type
   String getName();
   int getModifiers();
   Object getValue(T source);
   void setValue(T source, Object value);
}
