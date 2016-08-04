package org.snapscript.core.function;

public interface Accessor<T> {
   Object getValue(T source);
   void setValue(T source, Object value);
}
