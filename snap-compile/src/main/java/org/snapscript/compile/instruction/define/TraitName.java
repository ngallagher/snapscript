package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.TextLiteral;
import org.snapscript.core.Transient;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class TraitName extends TypeName {
   
   public TraitName(TextLiteral literal) {
      super(literal);
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception{
      Value value = literal.evaluate(scope, left);
      String name = value.getValue();
      Module module = scope.getModule();
      Type base = module.addType(name);
      
      return new Transient(base);
   }  
}
