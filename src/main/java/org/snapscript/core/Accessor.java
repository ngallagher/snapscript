package org.snapscript.core;

public interface Accessor<T> {
   Object getValue(T source);
   void setValue(T source, Object value);
}
