package org.snapscript.compile.instruction;

import static org.snapscript.core.convert.Score.INVALID;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.convert.ConstraintConverter;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.convert.Score;

public class ConstraintChecker {
   
   public ConstraintChecker() {
      super();
   }

   public boolean compatible(Scope scope, Object value, String name) throws Exception {
      if(name != null) {
         Module module = scope.getModule();
         Type type = module.getType(name);

         return compatible(scope, value, type);
      }
      return true;
   }

   public boolean compatible(Scope scope, Object value, Type type) throws Exception {
      if(type != null) {
         Module module = scope.getModule();
         Context context = module.getContext();
         ConstraintMatcher matcher = context.getMatcher();
         ConstraintConverter converter = matcher.match(type);
         Score score = converter.score(value);
         
         return score.compareTo(INVALID) > 0;
      }
      return true;
   }
}
