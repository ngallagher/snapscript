package org.snapscript.compile.instruction;

import java.util.List;

import org.snapscript.core.InternalStateException;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class ParameterBuilder {
   
   private final ConstraintChecker checker;
   private final Signature signature;
   
   public ParameterBuilder(Signature signature) {
      this.checker = new ConstraintChecker();
      this.signature = signature;
   }

   public Value create(Scope scope, Object value, int index) throws Exception {
      List<String> names = signature.getNames();
      List<Type> types = signature.getTypes();
      Type type = types.get(index);
      String name = names.get(index);
      int length = names.size();
      
      if(index >= length -1) {
         if(signature.isVariable()) {
            Object[] list = (Object[])value;
            
            for(int i = 0; i < list.length; i++) {
               Object entry = list[i];
               
               if(!checker.compatible(scope, entry, type)) {
                  throw new InternalStateException("Parameter '" + name + "...' does not match constraint '" + type + "'");
               }
            }
            return ValueType.getReference(value, type);
         }
      }
      if(!checker.compatible(scope, value, type)) {
         throw new InternalStateException("Parameter '" + name + "' does not match constraint '" + type + "'");
      }
      return ValueType.getReference(value, type);         
   }
}
