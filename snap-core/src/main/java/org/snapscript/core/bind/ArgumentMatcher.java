package org.snapscript.core.bind;

import java.util.List;
import java.util.Map;

import org.snapscript.common.LeastRecentlyUsedMap;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.convert.ConstraintConverter;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.convert.FixedArgumentConverter;
import org.snapscript.core.convert.NoArgumentConverter;
import org.snapscript.core.convert.VariableArgumentConverter;

public class ArgumentMatcher {

   private final Map<Signature, ArgumentConverter> converters;
   private final ConstraintMatcher matcher;
   
   public ArgumentMatcher(ConstraintMatcher matcher, TypeLoader loader) {
      this(matcher, loader, 50000);
   }
   
   public ArgumentMatcher(ConstraintMatcher matcher, TypeLoader loader, int capacity) {
      this.converters = new LeastRecentlyUsedMap<Signature, ArgumentConverter>(capacity);
      this.matcher = matcher;
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
      int size = types.size();
      
      if(size > 0) {
         ConstraintConverter[] converters = new ConstraintConverter[size];
         
         for(int i = 0; i < size - 1; i++) {
            Type type = types.get(i);
            converters[i] = matcher.match(type);
         }
         Type type = types.get(size - 1);
         
         if(type != null) {
            Type entry = type.getEntry();
            
            if(signature.isVariable()) {
               converters[size - 1] = matcher.match(entry);
            } else {
               converters[size - 1] = matcher.match(type);
            }
         } else {
            converters[size - 1] = matcher.match(type);
         }
         if(signature.isVariable()) {
            return new VariableArgumentConverter(converters);
         }
         return new FixedArgumentConverter(converters);
      }
      return new NoArgumentConverter();
   }
}
