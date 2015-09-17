package org.snapscript.parse;

public interface Token<T> {
   T getValue();
   int getType();
   int getLine();
}
