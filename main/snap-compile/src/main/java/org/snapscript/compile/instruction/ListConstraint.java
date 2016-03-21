package org.snapscript.compile.instruction;

import java.util.List;

import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.parse.StringToken;

public class ListConstraint implements Evaluation {

   private final StringToken token;
   
   public ListConstraint(StringToken token) {
      this.token = token;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Context context = scope.getContext();
      TypeLoader loader = context.getLoader();
      Type type = loader.loadType(List.class);
      
      return ValueType.getTransient(type);
   }
}