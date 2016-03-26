package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ParameterExtractor;
import org.snapscript.core.Initializer;
import org.snapscript.core.Instance;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.Type;

public class NewInstanceInvocation implements Constructor {
   
   private final ParameterExtractor extractor;
   private final SignatureAligner aligner;
   private final Constructor constructor;
   private final Initializer factory;
   
   public NewInstanceInvocation(Signature signature, Initializer factory, Constructor constructor) {
      this.extractor = new ParameterExtractor(signature);
      this.aligner = new SignatureAligner(signature);
      this.constructor = constructor;
      this.factory = factory;
   }

   @Override
   public Result invoke(Scope scope, Instance object, Object... list) throws Exception {
      Type real = (Type)list[0];
      Object[] arguments = aligner.align(list); // combine variable arguments to a single array
      Instance inner = object.getInner();
      
      if(arguments.length > 0) {
         extractor.extract(inner, arguments);
      }
      Result result = factory.execute(inner, real);
      Instance instance = result.getValue();

      if(instance == null) {
         throw new InternalStateException("Instance could not be created");
      }
      return constructor.invoke(scope, instance, list);
   }
}
