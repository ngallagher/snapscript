package org.snapscript.core.convert;

public class ProxyArgumentExtractor {

   private final ProxyWrapper builder;
   private final Object[] empty;
   
   public ProxyArgumentExtractor(ProxyWrapper builder) {
      this.empty = new Object[]{};
      this.builder = builder;
   }
   
   public Object[] extract(Object[] arguments) {
      if(arguments != null) {
         Object[] convert = new Object[arguments.length];
         
         for(int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            Object value = builder.fromProxy(argument);
            
            convert[i] = value;
         }
         return convert;
      }
      return empty;
   }
}
