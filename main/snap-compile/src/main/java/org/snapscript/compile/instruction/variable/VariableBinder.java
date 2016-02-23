package org.snapscript.compile.instruction.variable;

import java.util.Map;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeTraverser;

public class VariableBinder {
   
   private final TypeTraverser traverser;
   
   public VariableBinder() {
      this.traverser = new TypeTraverser();
   }
   
   public ValueResolver bind(Object left, String name) {
      if(left != null) {
         if(Scope.class.isInstance(left)) {
            return new ScopeResolver(name);
         }
         if(Map.class.isInstance(left)) {
            return new MapResolver(name);
         }
         if(Type.class.isInstance(left)) {
            return new TypeResolver(traverser, name);
         }
         return new ObjectResolver(traverser, name);
      }
      return new LocalResolver(name);
   }
}
