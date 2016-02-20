package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.compile.instruction.ParameterExtractor;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.Statement;

public class StaticInvocation implements Invocation<Object> {

   private final ParameterExtractor extractor;
   private final SignatureAligner aligner;
   private final AtomicBoolean compile;
   private final Statement statement;
   private final Scope inner;
   
   public StaticInvocation(Signature signature, Statement statement, Scope inner) {
      this.extractor = new ParameterExtractor(signature);
      this.aligner = new SignatureAligner(signature);
      this.compile = new AtomicBoolean();
      this.statement = statement;
      this.inner = inner;
   }
   
   @Override
   public Result invoke(Scope outer, Object object, Object... list) throws Exception {
      Object[] arguments = aligner.align(list); 
      Scope scope = inner.getInner();
      
      if(arguments.length > 0) {
         extractor.extract(scope, arguments);
      }
      if(compile.compareAndSet(false, true)) {
         statement.compile(scope); // we need to run static initializer
      }
      return statement.execute(scope);
   }
}

