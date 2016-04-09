package org.snapscript.compile.instruction.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.compile.instruction.ParameterExtractor;
import org.snapscript.core.Context;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.convert.ConstraintConverter;
import org.snapscript.core.convert.ConstraintMatcher;

public class StaticInvocation implements Invocation<Object> {

   private final ParameterExtractor extractor;
   private final SignatureAligner aligner;
   private final AtomicBoolean compile;
   private final Statement statement;
   private final Type constraint;
   private final Scope inner;
   
   public StaticInvocation(Signature signature, Statement statement, Scope inner, Type constraint) {
      this.extractor = new ParameterExtractor(signature);
      this.aligner = new SignatureAligner(signature);
      this.compile = new AtomicBoolean();
      this.constraint = constraint;
      this.statement = statement;
      this.inner = inner;
   }
   
   @Override
   public Result invoke(Scope outer, Object object, Object... list) throws Exception {
      Object[] arguments = aligner.align(list); 
      Context context = outer.getContext();
      Scope scope = inner.getInner();
      
      if(arguments.length > 0) {
         extractor.extract(scope, arguments);
      }
      if(compile.compareAndSet(false, true)) {
         statement.compile(scope); // we need to run static initializer
      }
      ConstraintMatcher matcher = context.getMatcher();
      ConstraintConverter converter = matcher.match(constraint);
      Result result = statement.execute(scope);
      Object value = result.getValue();
      
      if(value != null) {
         value = converter.convert(value);
      }
      return ResultType.getNormal(value);
   }
}

