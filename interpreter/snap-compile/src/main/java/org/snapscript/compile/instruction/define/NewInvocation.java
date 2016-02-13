package org.snapscript.compile.instruction.define;

import static org.snapscript.core.Reserved.TYPE_CLASS;
import static org.snapscript.core.Reserved.TYPE_THIS;

import java.util.List;

import org.snapscript.compile.instruction.ConstraintChecker;
import org.snapscript.core.Bug;
import org.snapscript.core.Initializer;
import org.snapscript.core.InstanceScope;
import org.snapscript.core.Invocation;
import org.snapscript.core.Model;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.State;
import org.snapscript.core.Type;
import org.snapscript.core.Value;
import org.snapscript.core.ValueType;

public class NewInvocation implements Invocation<Scope> {
   
   private final ConstraintChecker checker;
   private final SignatureAligner aligner;
   private final Invocation constructor;
   private final Signature signature;
   private final Initializer factory;
   
   public NewInvocation(Signature signature, Initializer factory, Invocation constructor) {
      this.aligner = new SignatureAligner(signature);
      this.checker = new ConstraintChecker();
      this.constructor = constructor;
      this.signature = signature;
      this.factory = factory;
   }

   @Bug("This is rubbish and needs to be cleaned up")
   @Override
   public Result invoke(Scope scope, Scope object, Object... list) throws Exception {
      Type real = (Type)list[0];
      List<String> names = signature.getNames();
      List<Type> types = signature.getTypes();
      Object[] arguments = aligner.align(list); // combine variable arguments to a single array
      Scope inner = scope.getInner();
      State state = inner.getState();
      
      for(int i = 0; i < arguments.length; i++) {
         Type require = types.get(i);
         String name = names.get(i);
         Object argument = arguments[i];
         
         if(!checker.compatible(scope, argument, require)) {
            throw new IllegalStateException("Parameter '" + name + "' does not match constraint '" + require + "'");
         }
         Value reference = ValueType.getReference(argument, require);         
         state.addVariable(name, reference);
      }
      Result result = factory.execute(inner, real);
      Scope instance = result.getValue();
      
      if(instance == null) {
         throw new IllegalStateException("Instance could not be created");
      }
      return constructor.invoke(scope, instance, list);
   }
}
