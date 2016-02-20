package org.snapscript.compile.instruction;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.Statement;

public class ClosureInvocation implements Invocation<Object> {

   private final ParameterExtractor extractor;
   private final SignatureAligner aligner;
   private final AtomicBoolean compile;
   private final Statement statement;
   private final Scope outer;
   
   public ClosureInvocation(Signature signature, Statement statement, Scope outer) {
      this.extractor = new ParameterExtractor(signature);
      this.aligner = new SignatureAligner(signature);
      this.compile = new AtomicBoolean();
      this.statement = statement;
      this.outer = outer;
   }
   
   @Override
   public Result invoke(Scope scope, Object object, Object... list) throws Exception {
      Object[] arguments = aligner.align(list); 
      Scope inner = outer.getInner();
      
      if(arguments.length > 0) {
         extractor.extract(inner, arguments);
      }
      if(compile.compareAndSet(false, true)) {
         statement.compile(inner);
      }
      return statement.execute(inner);
   }
}
