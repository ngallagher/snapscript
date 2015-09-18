package org.snapscript.interpret;

import java.util.List;

import org.snapscript.core.Invocation;
import org.snapscript.core.Reference;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;

public class StatementInvocation implements Invocation<Object> {

   private final ConstraintChecker checker;
   private final SignatureAligner aligner;
   private final Signature signature;
   private final Statement statement;
   
   public StatementInvocation(Statement statement, Signature signature) {
      this.aligner = new SignatureAligner(signature);
      this.checker = new ConstraintChecker();
      this.statement = statement;
      this.signature = signature;
   }
   
   @Override
   public Result invoke(Scope scope, Object object, Object... list) throws Exception {
      List<String> names = signature.getNames();
      List<Type> types = signature.getTypes();
      Object[] arguments = aligner.align(list); // combine variable arguments to a single array
      Scope inner = scope.getScope();
      
      for(int i = 0; i < arguments.length; i++) {
         Type require = types.get(i);
         String name = names.get(i);
         Object argument = arguments[i];
         
         if(!checker.compatible(scope, argument, require)) {
            throw new IllegalStateException("Parameter '" + name + "' does not match constraint '" + require + "'");
         }
         Reference reference = new Reference(argument);         
         inner.addVariable(name, reference);
      }
      return statement.execute(inner);
   }

}
