package org.snapscript.core.bind;

import org.snapscript.core.Function;
import org.snapscript.core.Signature;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.TypeLoader;

public class FunctionKeyBuilder {

   private final TypeExtractor extractor;
   
   public FunctionKeyBuilder(TypeLoader loader) {
      this.extractor = new TypeExtractor(loader);
   }
   
   public Object create(Object source, String name, Object... list) throws Exception {
      Object[] types = new Object[list.length];
      
      for(int i = 0; i < list.length; i++) {
         Object value = list[i];
         
         if(value != null) {
            if(Function.class.isInstance(value)) { // closure matching
               Function function = (Function)value;
               Signature signature = function.getSignature();
               
               types[i] = signature;
            } else {
               types[i] = extractor.extract(value);
            }
         }
      }
      return new FunctionKey(source, name, types);
   }
}
