package org.snapscript.compile.instruction.define;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.compile.instruction.ConstraintChecker;
import org.snapscript.core.InternalStateException;
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

public class StaticInvocation implements Invocation<Object> {

   private final ConstraintChecker checker;
   private final SignatureAligner aligner;
   private final AtomicBoolean compile;
   private final Signature signature;
   private final Statement statement;
   private final Scope inner;
   
   public StaticInvocation(Statement statement, Signature signature, Scope inner) {
      this.aligner = new SignatureAligner(signature);
      this.checker = new ConstraintChecker();
      this.compile = new AtomicBoolean();
      this.statement = statement;
      this.signature = signature;
      this.inner = inner;
   }
   
   @Override
   public Result invoke(Scope outer, Object object, Object... list) throws Exception {
      List<String> names = signature.getNames();
      List<Type> types = signature.getTypes();
      Object[] arguments = aligner.align(list); 
      Scope scope = inner.getInner();
      State state = scope.getState();
      
      for(int i = 0; i < arguments.length; i++) {
         Type require = types.get(i);
         String name = names.get(i);
         Object argument = arguments[i];
         
         if(!checker.compatible(outer, argument, require)) {
            throw new InternalStateException("Parameter '" + name + "' does not match constraint '" + require + "'");
         }
         Value reference = ValueType.getReference(argument, require);         
         state.addVariable(name, reference);
      }
      if(compile.compareAndSet(false, true)) {
         statement.compile(scope); // we need to run static initializer
      }
      return statement.execute(scope);
   }
}

