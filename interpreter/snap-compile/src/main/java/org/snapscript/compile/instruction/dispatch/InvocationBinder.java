package org.snapscript.compile.instruction.dispatch;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class InvocationBinder {

   public InvocationDispatcher bind(Scope scope, Object left) {
      if(left != null) {
         Class type = left.getClass();
         
         if(Scope.class.isInstance(left)) {
            return new ScopeDispatcher(scope, left);            
         }
         if(Module.class.isInstance(left)) {
            return new ModuleDispatcher(scope, left);
         }  
         if(Type.class.isInstance(left)) {
            return new TypeDispatcher(scope, left);
         }  
         if(type.isArray()) {
            return new ArrayDispatcher(scope, left);
         }
         return new ObjectDispatcher(scope, left);
      }
      Type type = scope.getType();
      
      if(type != null) {
         return new ScopeDispatcher(scope, scope);
      }
      return new LocalDispatcher(scope);      
   }
}