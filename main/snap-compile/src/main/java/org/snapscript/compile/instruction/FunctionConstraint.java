package org.snapscript.compile.instruction;

import static org.snapscript.core.ModifierType.ABSTRACT;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Function;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class FunctionConstraint implements Evaluation {

   private final ClosureBuilder builder;
   private final ParameterList list;
   private final Statement body;
   
   public FunctionConstraint(ParameterList list) {
      this.body = new NoOperation();
      this.builder = new ClosureBuilder(body);
      this.list = list;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Signature signature = list.create(scope);
      Function function = builder.create(signature, scope, ABSTRACT.mask);
      Type type = function.getType();
      
      return ValueType.getTransient(type);
   }
}
