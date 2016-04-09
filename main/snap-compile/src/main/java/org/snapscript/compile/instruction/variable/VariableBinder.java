package org.snapscript.compile.instruction.variable;

import java.util.Collection;
import java.util.Map;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeTraverser;
import org.snapscript.core.instance.Instance;

public class VariableBinder {
   
   private final TypeTraverser traverser;
   
   public VariableBinder() {
      this.traverser = new TypeTraverser();
   }
   
   public ValueResolver bind(Scope scope, Object left, String name) {
      if(left != null) {
         Class type = left.getClass();
         
         if(Map.class.isInstance(left)) {
            return new MapResolver(name);
         }
         if(Module.class.isInstance(left)) {
            return new ModuleResolver(name);
         }
         if(Scope.class.isInstance(left)) {
            return new ScopeResolver(traverser, name);
         }
         if(Type.class.isInstance(left)) {
            return new TypeResolver(traverser, name);
         }
         if(Collection.class.isInstance(left)) {
            return new CollectionResolver(traverser, name);
         }
         if(type.isArray()) {
            return new ArrayResolver(traverser, name);
         }
         return new ObjectResolver(traverser, name);
      }
      if(Instance.class.isInstance(scope)) {
         return new InstanceResolver(name);
      }
      return new LocalResolver(name);
   }
}
