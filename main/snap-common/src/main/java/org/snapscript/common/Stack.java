package org.snapscript.common;

import java.util.Iterator;

public interface Stack<T> {
   T pop();
   T peek();
   T get(int index);
   void push(T value);
   Iterator<T> iterator();
   boolean contains(T value);
   boolean isEmpty();
   int size();
   void clear();
}
