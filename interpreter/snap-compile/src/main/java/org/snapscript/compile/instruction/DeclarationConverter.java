package org.snapscript.compile.instruction;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.convert.ConstraintConverter;
import org.snapscript.core.convert.ConstraintMatcher;

public class DeclarationConverter {

   private final ConstraintExtractor extractor;

   public DeclarationConverter(Constraint constraint) {      
      this.extractor = new ConstraintExtractor(constraint);
   }   

   public Value convert(Scope scope, Object value, String name) throws Exception {
      Type type = extractor.extract(scope);
      
      if(type != null) {
         Module module = scope.getModule();
         Context context = module.getContext();
         ConstraintMatcher matcher = context.getMatcher();
         ConstraintConverter converter = matcher.match(type);
         int score = converter.score(value);
         
         if(score == 0) {
            throw new IllegalStateException("Variable '" + name + "' does not match constraint '" + type + "'");
         }
         if(value != null) {
            value = converter.convert(value);
         }
      }
      return ValueType.getTransient(value, type);
   }
}
