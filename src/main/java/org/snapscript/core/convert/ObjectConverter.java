package org.snapscript.core.convert;

import java.util.List;

import org.snapscript.core.Type;

public class ObjectConverter extends TypeConverter {
   
   private final TypeExtractor extractor;
   private final Type type;
   
   public ObjectConverter(TypeExtractor extractor, Type type) {
      this.extractor = extractor;
      this.type = type;
   }

   @Override
   public int score(Object value) throws Exception {
      Type match = extractor.extract(value);
      
      if(match != null) {
         if(match.equals(type)) {
            return EXACT;
         }
         List<Type> types = match.getTypes();
         
         if(types.contains(type)) { // here we are checking the class hierarchy
            return SIMILAR;
         }
         return INVALID;
      }
      return EXACT;
   }
   
   @Override
   public Object convert(Object object) {
      return object;
   }
}