package org.snapscript.compile.instruction.define;

import java.util.List;

import org.snapscript.compile.instruction.ConstraintChecker;
import org.snapscript.core.Bug;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.State;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class InstanceInvocation implements Invocation<Scope> {

   private final ConstraintChecker checker;
   private final SignatureAligner aligner;
   private final Signature signature;
   private final Statement statement;
   
   public InstanceInvocation(Statement statement, Signature signature) {
      this.aligner = new SignatureAligner(signature);
      this.checker = new ConstraintChecker();
      this.statement = statement;
      this.signature = signature;
   }
   
   @Bug("This needs to be cleaned up")
   @Override
   public Result invoke(Scope scope, Scope object, Object... list) throws Exception {
      List<String> names = signature.getNames();
      List<Type> types = signature.getTypes();
      Object[] arguments = aligner.align(list); // combine variable arguments to a single array

      // create a class scope from the passed in object!!!!!
      if(object == null) {
         object=scope; // XXX tihs is for super only!!!
      }
      Scope outer = object.getOuter();
      Scope inner = outer.getInner(); // create a writable scope
      State state = inner.getState();
      
      for(int i = 0; i < arguments.length; i++) {
         Type require = types.get(i);
         String name = names.get(i);
         Object argument = arguments[i];
         
         if(!checker.compatible(scope, argument, require)) {
            throw new IllegalStateException("Parameter '" + name + "' does not match constraint '" + require + "'");
         }
         Value reference = ValueType.getReference(argument);         
         state.addVariable(name, reference);
      }
      return statement.execute(inner);
   }

}
