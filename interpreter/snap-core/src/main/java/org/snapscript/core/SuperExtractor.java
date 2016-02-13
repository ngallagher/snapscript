package org.snapscript.core;

import java.util.List;

public class SuperExtractor {
   
   public Type extractor(Type type) {
      List<Type> types = type.getTypes();
      
      for(Type base : types) {
         return base;
      }
      return null;
   }

}
