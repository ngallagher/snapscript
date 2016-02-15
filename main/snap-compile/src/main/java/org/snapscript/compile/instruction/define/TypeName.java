package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class TypeName implements Evaluation {
   
   protected final TextLiteral literal;
   
   public TypeName(TextLiteral literal) {
      this.literal = literal;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      Value value = literal.evaluate(scope, left);
      String name = value.getValue();
      Module module = scope.getModule();
      Type base = module.addType(name);
      
      if(base == null) {
         throw new InternalStateException("Type '" + name + "' could not be resolved");
      }
      return ValueType.getTransient(base);
   }  
}
