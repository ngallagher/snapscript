package org.snapscript.compile.instruction.variable;

import org.snapscript.core.Scope;
import org.snapscript.core.SuperScope;
import org.snapscript.core.Type;

public class ReferenceTypeConverter {
   
   public ReferenceTypeConverter() {
      super();
   }

   public Type convert(Object left) throws Exception {
      Class type = left.getClass();
      
      if(!Type.class.isInstance(left)) {
         if(SuperScope.class.isAssignableFrom(type)) {
            SuperScope reference = (SuperScope)left;
            return reference.getSuper();
         } 
         if(Scope.class.isAssignableFrom(type)) {
            Scope reference = (Scope)left;
            return reference.getType();
         } 
         return null;
      }
      return (Type)left;
   }
}
