package org.snapscript.core;


public class TypeChecker {

   private final TypeMatcher matcher;
   
   public TypeChecker(TypeMatcher matcher) {
      this.matcher = matcher;
   }
   
   public int score(Type require, Object value) throws Exception {
      TypeConverter converter = matcher.match(require);
      
      if(converter != null) {
         return converter.score(value);
      }
      return -1;
   }
}
