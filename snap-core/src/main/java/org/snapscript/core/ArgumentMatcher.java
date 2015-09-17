package org.snapscript.core;

import java.util.List;
import java.util.Map;

import org.snapscript.common.LeastRecentlyUsedMap;
import org.snapscript.core.Signature;

public class ArgumentMatcher {

   private final Map<Signature, ArgumentConverter> converters;
   private final TypeMatcher matcher;
   
   public ArgumentMatcher(TypeLoader loader) {
      this(loader, 50000);
   }
   
   public ArgumentMatcher(TypeLoader loader, int capacity) {
      this.converters = new LeastRecentlyUsedMap<Signature, ArgumentConverter>(capacity);
      this.matcher = new TypeMatcher(loader);
   }
   
   public ArgumentConverter match(Signature signature) throws Exception {
      ArgumentConverter converter = converters.get(signature);
      
      if(converter == null) {
         converter = resolve(signature);
         converters.put(signature, converter);
      }
      return converter;
   }
   
   private ArgumentConverter resolve(Signature signature) throws Exception {
      List<Type> types = signature.getTypes();
      int modifiers = signature.getModifiers();
      int size = types.size();
      
      if(size > 0) {
         TypeConverter[] converters = new TypeConverter[size];
         
         for(int i = 0; i < size - 1; i++) {
            Type type = types.get(i);
            converters[i] = matcher.match(type);
         }
         Type type = types.get(size - 1);
         
         if(type != null) {
            Type entry = type.getEntry();
            
            if((modifiers & 0x00000080) != 0) {
               converters[size - 1] = matcher.match(entry);
            } else {
               converters[size - 1] = matcher.match(type);
            }
         } else {
            converters[size - 1] = matcher.match(type);
         }
         if((modifiers & 0x00000080) != 0) {
            return new VariableArgumentConverter(converters);
         }
         return new FixedArgumentConverter(converters);
      }
      return new NoArgumentConverter();
   }
}
