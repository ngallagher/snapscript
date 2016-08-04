package org.snapscript.code.define;

import org.snapscript.code.literal.TextLiteral;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class TraitName extends TypeName {
   
   public TraitName(TextLiteral literal) {
      super(literal);
   }
   
   @Override
   public Type getType(Scope scope) throws Exception{
      Value value = literal.evaluate(scope, null);
      String name = value.getValue();
      Module module = scope.getModule();
      
      return  module.getType(name);
   }  
}
