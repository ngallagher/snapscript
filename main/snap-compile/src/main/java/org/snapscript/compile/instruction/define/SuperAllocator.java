package org.snapscript.compile.instruction.define;

import org.snapscript.compile.instruction.ParameterExtractor;
import org.snapscript.core.Initializer;
import org.snapscript.core.Instance;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Signature;
import org.snapscript.core.SignatureAligner;
import org.snapscript.core.Type;

public class SuperAllocator implements Allocator {
   
   private final ParameterExtractor extractor;
   private final SignatureAligner aligner;
   private final Initializer initializer;
   private final Allocator allocator;
   
   public SuperAllocator(Signature signature, Initializer initializer, Allocator allocator) {
      this.extractor = new ParameterExtractor(signature);
      this.aligner = new SignatureAligner(signature);
      this.initializer = initializer;
      this.allocator = allocator;
   }

   @Override
   public Instance allocate(Scope scope, Instance object, Object... list) throws Exception {
      Type real = (Type)list[0];
      Object[] arguments = aligner.align(list); // combine variable arguments to a single array
      Instance inner = object.getInner();
      
      if(arguments.length > 0) {
         extractor.extract(inner, arguments);
      }
      Result result = initializer.execute(inner, real);
      Instance base = result.getValue();
      
      return allocator.allocate(scope, base, list);
   }
}
