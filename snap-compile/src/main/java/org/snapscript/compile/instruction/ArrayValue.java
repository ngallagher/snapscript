package org.snapscript.compile.instruction;

import org.snapscript.core.Value;

public class ArrayValue extends Value {
   
   private final Object array;
   private final Integer index;
   private final Class type;
   
   public ArrayValue(Object array, Integer index) {
      this.type = array.getClass();
      this.array = array;
      this.index = index;
   }
   
   @Override
   public Class getType() {
      return type.getComponentType();
   }
   
   @Override
   public Object getValue(){
      return java.lang.reflect.Array.get(array, index);
   }
   
   @Override
   public void setValue(Object value){
      java.lang.reflect.Array.set(array, index, value);
   }       
}