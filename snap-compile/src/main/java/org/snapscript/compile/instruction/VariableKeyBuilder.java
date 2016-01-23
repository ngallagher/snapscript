package org.snapscript.compile.instruction;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.SuperScope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class VariableKeyBuilder {

   public Object create(Scope scope, Object left, String name) throws Exception {
      if(left != null) {
         Module module = scope.getModule();
         Context context = module.getContext();
         TypeLoader loader = context.getLoader();
         Class type = left.getClass();
         
         if(SuperScope.class.isAssignableFrom(type)) {
            SuperScope reference = (SuperScope)left;
            return reference.getSuper();
         } 
         if(Scope.class.isAssignableFrom(type)) {
            Scope reference = (Scope)left;
            return reference.getType();
         } 
         Type actual = loader.loadType(type);
         
         if(actual != null) {
            return new VariableKey(name, actual);// this key is no good?
         }
      }
      return new VariableKey(name, null);
   }
}
