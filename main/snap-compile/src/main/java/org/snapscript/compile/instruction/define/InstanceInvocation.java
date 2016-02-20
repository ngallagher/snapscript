package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ParameterExtractor;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Invocation;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.Statement;

public class InstanceInvocation implements Invocation<Scope> {

   private final ParameterExtractor extractor;
   private final SignatureAligner aligner;
   private final Statement statement;
   
   public InstanceInvocation(Signature signature, Statement statement) {
      this.extractor = new ParameterExtractor(signature);
      this.aligner = new SignatureAligner(signature);
      this.statement = statement;
   }
   
   @Override
   public Result invoke(Scope scope, Scope instance, Object... list) throws Exception {
      if(statement == null) {
         throw new InternalStateException("Function is abstract");
      }
      Object[] arguments = aligner.align(list); 

      if(instance == null) {
         instance = scope; // this is for super
      }
      Scope outer = instance.getOuter();
      Scope inner = outer.getInner(); // create a writable scope
      
      if(arguments.length > 0) {
         extractor.extract(inner, arguments);
      }
      return statement.execute(inner);
   }

}
