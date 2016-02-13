package org.snapscript.core;

import java.util.List;

public class SuperExtractor {
   
   @Bug("Should this be in TypeExtractor")
   public Type extractor(Type type) {
      List<Type> types = type.getTypes();
      
      for(Type base : types) {
         return base;
      }
      return null;
   }

}
