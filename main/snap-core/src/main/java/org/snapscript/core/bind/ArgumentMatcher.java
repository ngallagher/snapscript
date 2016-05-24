package org.snapscript.core.bind;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.core.Parameter;
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
      this.converters = new ConcurrentHashMap<Signature, ArgumentConverter>();
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
      List<Parameter> parameters = signature.getParameters();
      int size = parameters.size();
      
      if(size > 0) {
         ConstraintConverter[] converters = new ConstraintConverter[size];
         
         for(int i = 0; i < size - 1; i++) {
            Parameter parameter = parameters.get(i);
            Type type = parameter.getType();
            
            converters[i] = matcher.match(type);
         }
         Parameter parameter = parameters.get(size - 1);
         Type type = parameter.getType();
         
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
