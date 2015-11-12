package org.snapscript.interpret;

import java.util.List;

import org.snapscript.core.Value;
import org.snapscript.core.convert.ProxyBuilder;
import org.snapscript.core.convert.ProxyExtractor;

public class ListValue extends Value {
   
   private final ProxyExtractor extractor;
   private final ProxyBuilder builder;
   private final List list;
   private final Integer index;
   
   public ListValue(List list, Integer index) {
      this.extractor = new ProxyExtractor();
      this.builder = new ProxyBuilder();
      this.list = list;
      this.index = index;
   }
   
   @Override
   public Class getType() {
      return Object.class;
   }
   
   @Override
   public Object getValue(){
      Object value = list.get(index);
      
      if(value != null) {
         return extractor.extract(value);
      }
      return value;
   }
   
   @Override
   public void setValue(Object value){
      Object proxy = builder.create(value);
      int length = list.size();
      
      for(int i = length; i <= index; i++) {
         list.add(null);
      }
      list.set(index, proxy);
   }       
}