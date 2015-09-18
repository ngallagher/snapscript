package org.snapscript.interpret;

import java.util.List;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ConstraintChecker {
   
   private final ConstraintExtractor extractor;
   
   public ConstraintChecker() {
      this.extractor = new ConstraintExtractor();
   }

   public boolean compatible(Scope scope, Object value, String name) throws Exception {
      if(name != null) {
         Module module = scope.getModule();
         Type require = module.getType(name);
         Type actual = extractor.extract(scope, value);

         if(require != actual) {
            List<Type> compatible = actual.getTypes();
            
            if(!compatible.contains(require)) {
               return false;
            }
         }
      }
      return true;
   }
   
   public boolean compatible(Scope scope, Object value, Type type) throws Exception {
      if(type != null) {
         Type actual = extractor.extract(scope, value);

         if(type != actual) {
            List<Type> compatible = actual.getTypes();
            
            if(!compatible.contains(type)) {
               return false;
            }
         }
      }
      return true;
   }
   

}
