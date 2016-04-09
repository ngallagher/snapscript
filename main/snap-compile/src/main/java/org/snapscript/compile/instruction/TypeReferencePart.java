package org.snapscript.compile.instruction;

import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class TypeReferencePart implements Evaluation {

   private final NameExtractor extractor;

   public TypeReferencePart(Evaluation type) {
      this.extractor = new NameExtractor(type);
   }   
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Module module = scope.getModule();
      
      if(left != null) {
         return create(scope, (Module)left);
      }
      return create(scope, module);
   }
   
   private Value create(Scope scope, Module module) throws Exception {
      String name = extractor.extract(scope);
      Object result = module.getModule(name);
      
      if(result == null) {
         result = module.addType(name); // XXX this can cause an error in modules for constraints not yet defined
      }
      if(result == null) {
         throw new InternalStateException("No type found for " + name + " in '" + module + "'"); // class not found
      }
      return ValueType.getTransient(result);
   }

}
