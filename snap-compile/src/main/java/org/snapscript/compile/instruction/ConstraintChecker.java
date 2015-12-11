package org.snapscript.compile.instruction;

import org.snapscript.core.InstanceChecker;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class ConstraintChecker {
   
   private final ConstraintExtractor extractor;
   private final InstanceChecker checker;
   
   public ConstraintChecker() {
      this.extractor = new ConstraintExtractor();
      this.checker = new InstanceChecker();
   }

   public boolean compatible(Scope scope, Object value, String name) throws Exception {
      if(name != null) {
         Module module = scope.getModule();
         Type type = module.getType(name);

         return compatible(scope, value, type);
      }
      return true;
   }
   
   // this might not work!!!
   public boolean compatible(Scope scope, Object value, Type type) throws Exception {
      if(type != null) {
         Type actual = extractor.extract(scope, value);

         if(type != actual) {
            if(actual == null) {
               Class real = type.getType();
               
               if(real != null) {
                  return !real.isPrimitive();
               }
               return true;
            }
            if(!checker.check(actual, type)) {
               return false;
            }
         }
      }
      return true;
   }
}
