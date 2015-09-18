package org.snapscript.core.bind;

import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.TypeLoader;


public class FunctionKeyBuilder {

   private final TypeExtractor extractor;
   
   public FunctionKeyBuilder(TypeLoader loader) {
      this.extractor = new TypeExtractor(loader);
   }
   
   public Object create(Object source, String name, Object... list) throws Exception {
      Type[] types = new Type[list.length];
      
      for(int i = 0; i < list.length; i++) {
         Object value = list[i];
         Type type = extractor.extract(value);
         
         types[i] = type;
      }
      return new FunctionKey(source, name, types);
   }
}
