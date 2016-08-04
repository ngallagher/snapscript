package org.snapscript.compile.constraint;

import static org.snapscript.core.ModifierType.ABSTRACT;

import org.snapscript.compile.closure.ClosureBuilder;
import org.snapscript.compile.control.NoOperation;
import org.snapscript.compile.function.ParameterList;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;
import org.snapscript.core.function.Function;
import org.snapscript.core.function.Signature;

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
      Type type = function.getDefinition();
      
      return ValueType.getTransient(type);
   }
}
