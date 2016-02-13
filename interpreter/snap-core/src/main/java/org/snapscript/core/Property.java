package org.snapscript.core;

public interface Property<T> {
   Type getType();
   String getName();
   int getModifiers();
   Object getValue(T source);
   void setValue(T source, Object value);
}
