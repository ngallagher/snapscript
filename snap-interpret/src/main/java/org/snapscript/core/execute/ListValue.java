package org.snapscript.core.execute;

import java.util.List;

import org.snapscript.core.Value;

public class ListValue extends Value {
   
   private final List list;
   private final Integer index;
   
   public ListValue(List list, Integer index) {
      this.list = list;
      this.index = index;
   }
   
   @Override
   public Class getType() {
      return Object.class;
   }
   
   @Override
   public Object getValue(){
      return list.get(index);
   }
   
   @Override
   public void setValue(Object value){
      int length = list.size();
      
      for(int i = length; i <= index; i++) {
         list.add(null);
      }
      list.set(index, value);
   }       
}