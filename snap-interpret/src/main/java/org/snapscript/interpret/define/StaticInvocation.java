package org.snapscript.interpret.define;

import java.util.List;

import org.snapscript.core.Invocation;
import org.snapscript.core.Reference;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.interpret.ConstraintChecker;

public class StaticInvocation implements Invocation<Object> {

   private final ConstraintChecker checker;
   private final SignatureAligner aligner;
   private final Signature signature;
   private final Statement statement;
   private final String function;
   private final Scope inner;
   
   public StaticInvocation(Statement statement, Signature signature, Scope inner, String function) {
      this.aligner = new SignatureAligner(signature);
      this.checker = new ConstraintChecker();
      this.statement = statement;
      this.signature = signature;
      this.function = function;
      this.inner = inner;
   }
   
   @Override
   public Result invoke(Scope scope, Object object, Object... list) throws Exception {
      List<String> names = signature.getNames();
      List<Type> types = signature.getTypes();
      Object[] arguments = aligner.align(list); // combine variable arguments to a single array
      Scope blah = inner.getScope();
      
      for(int i = 0; i < arguments.length; i++) {
         Type require = types.get(i);
         String name = names.get(i);
         Object argument = arguments[i];
         
         if(!checker.compatible(scope, argument, require)) {
            throw new IllegalStateException("Parameter '" + name + "' does not match '" + require + "' for '" + function + "'");
         }
         Reference reference = new Reference(argument);         
         blah.addVariable(name, reference);
      }
      return statement.execute(blah);
   }
}

