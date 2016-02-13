package org.snapscript.core.handle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class HandleTypeExtractor {

   private final HandleTypeConverter converter;
   private final Map<Class, Type> types;
   
   public HandleTypeExtractor() {
      this.types = new ConcurrentHashMap<Class, Type>();
      this.converter = new HandleTypeConverter();
   }
   
   public Type extract(Scope scope, Object left) throws Exception {
      if(left != null) {
         Class type = left.getClass();
         Type match = types.get(type);
         
         if(match == null) {
            match = converter.convert(left);
         }
         if(match == null) {
            Module module = scope.getModule();
            Type actual = module.getType(type);
            
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
