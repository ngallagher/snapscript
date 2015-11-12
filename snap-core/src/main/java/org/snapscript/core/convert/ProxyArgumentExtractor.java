package org.snapscript.core.convert;

public class ProxyArgumentExtractor {

   private final ProxyExtractor extractor;
   private final Object[] empty;
   
   public ProxyArgumentExtractor() {
      this.extractor = new ProxyExtractor();
      this.empty = new Object[]{};
   }
   
   public Object[] extract(Object[] arguments) {
      if(arguments != null) {
         Object[] convert = new Object[arguments.length];
         
         for(int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            Object value = extractor.extract(argument);
            
            convert[i] = value;
         }
         return convert;
      }
      return empty;
   }
}
