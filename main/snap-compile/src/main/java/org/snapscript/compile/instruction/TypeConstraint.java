package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.core.Evaluation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class TypeConstraint implements Evaluation {
   
   private final NameExtractor extractor;
   
   public TypeConstraint(TextLiteral token) {
      this.extractor = new NameExtractor(token);
   }

   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      String name = extractor.extract(scope);
      Module module = scope.getModule();
      Type type = module.addType(name); // XXX is this safe?
      
      if(type == null) {
         throw new InternalStateException("Constraint '" + name +"' has not been imported");
      }
      return ValueType.getTransient(type);
   }

}
