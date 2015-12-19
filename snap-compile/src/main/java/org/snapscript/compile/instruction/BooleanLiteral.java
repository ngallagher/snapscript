package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.parse.StringToken;

public class BooleanLiteral implements Evaluation {
   
   private StringToken token;
   private Boolean value;
   
   public BooleanLiteral(StringToken token) {
      this.token = token;
   }
 
   public Value evaluate(Scope scope, Object left) throws Exception{
      if(value == null) {
         String text = token.getValue();
         
         if(text == null) {
            throw new IllegalStateException("Boolean value is null");
         }
         value = Boolean.parseBoolean(text);
      }
      return ValueType.getTransient(value);
   }      
}