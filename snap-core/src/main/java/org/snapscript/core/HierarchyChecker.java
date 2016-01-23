package org.snapscript.core;

import java.util.Set;

public class HierarchyChecker {

   private final HierarchyExtractor extractor;
   
   public HierarchyChecker() {
      this.extractor = new HierarchyExtractor();
   }
   
   public boolean check(Type actual, Type required) {
      Set<Type> list = extractor.extract(actual);
      
      if(!list.isEmpty()) {
         return list.contains(required);
      }
      return false;
   }
   

}
