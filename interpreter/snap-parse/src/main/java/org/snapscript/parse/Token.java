package org.snapscript.parse;

public interface Token<T> {
   T getValue();
   Line getLine();
   int getType();
}
