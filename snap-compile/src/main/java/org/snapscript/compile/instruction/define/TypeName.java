package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.Evaluation;
import org.snapscript.compile.instruction.TextLiteral;
import org.snapscript.core.Holder;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

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
         throw new IllegalStateException("Type '" + name + "' could not be resolved");
      }
      return new Holder(base);
   }  
}
