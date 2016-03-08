package org.snapscript.compile.instruction.collection;

import java.util.AbstractList;
import java.util.RandomAccess;

import org.snapscript.core.InternalArgumentException;

public abstract class PrimitiveArrayList<T> extends AbstractList<T> implements RandomAccess {

   public int length() {
      return size();
   }
   
   @Override
   public boolean add(T element) {
      throw new InternalArgumentException("Array cannot be resized");
   }
   
   @Override
   public void add(int index, T element) {
      throw new InternalArgumentException("Array cannot be resized");
   }

   @Override
   public boolean contains(Object o) {
      int index = indexOf(o);

      if (index == -1) {
         return false;
      }
      return true;

   }
}
