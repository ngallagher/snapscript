package org.snapscript.compile.instruction;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Transient;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.convert.ConstraintConverter;
import org.snapscript.core.convert.ConstraintMatcher;

public class DeclarationConverter {

   private final Constraint constraint;

   public DeclarationConverter(Constraint constraint) {      
      this.constraint = constraint;
   }   

   public Value convert(Scope scope, Object value, String name) throws Exception {
      if(constraint != null) {
         Value qualifier = constraint.evaluate(scope, null);
         String alias = qualifier.getString();
         Module module = scope.getModule();
         Type type = module.getType(alias);
         
         if(type == null) {
            throw new IllegalStateException("Constraint '" + alias +"' has not been imported");
         }
         Context context = module.getContext();
         ConstraintMatcher matcher = context.getMatcher();
         ConstraintConverter converter = matcher.match(type);
         int score = converter.score(value);
         
         if(score == 0) {
            throw new IllegalStateException("Variable '" + name + "' does not match constraint '" + alias + "'");
         }
         if(value != null) {
            value = converter.convert(value);
         }
         return new Transient(value, type);
      }
      return new Transient(value, null);
   }
}
