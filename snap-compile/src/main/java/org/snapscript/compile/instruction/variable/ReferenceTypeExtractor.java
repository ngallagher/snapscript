package org.snapscript.compile.instruction.variable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ReferenceTypeExtractor {

   private final ReferenceTypeConverter converter;
   private final Map<Class, Type> types;
   
   public ReferenceTypeExtractor() {
      this.types = new ConcurrentHashMap<Class, Type>();
      this.converter = new ReferenceTypeConverter();
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
