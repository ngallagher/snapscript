package org.snapscript.compile.instruction.variable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.SuperScope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;

public class ReferenceTypeExtractor {

   private final Map<Class, Type> types;
   
   public ReferenceTypeExtractor() {
      this.types = new ConcurrentHashMap<Class, Type>();
   }
   
   public Type extract(Scope scope, Object left) throws Exception {
      if(left != null) {
         Class type = left.getClass();
         Type match = types.get(type);
         
         if(match == null) {
            if(SuperScope.class.isAssignableFrom(type)) {
               SuperScope reference = (SuperScope)left;
               return reference.getSuper();
            } 
            if(Scope.class.isAssignableFrom(type)) {
               Scope reference = (Scope)left;
               return reference.getType();
            } 
            Module module = scope.getModule();
            Context context = module.getContext();
            TypeLoader loader = context.getLoader();
            Type actual = loader.loadType(type);
            
            if(actual != null) {
               types.put(type, actual);
            }
            return actual;
         }
         return match;
      }
      return null;
   }
}
