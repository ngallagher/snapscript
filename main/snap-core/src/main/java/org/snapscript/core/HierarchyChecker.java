package org.snapscript.core;

import java.util.Set;

public class HierarchyChecker {

   private final TypeTraverser traverser;
   
   public HierarchyChecker() {
      this.traverser = new TypeTraverser();
   }
   
   public boolean check(Type actual, Type required) {
      Set<Type> list = traverser.traverse(actual);
      
      if(!list.isEmpty()) {
         return list.contains(required);
      }
      return false;
   }
   

}
