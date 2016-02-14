package org.snapscript.core.error;

import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceComparator;
import org.snapscript.core.Type;

public class ErrorGenerator implements ErrorHandler {
   
   private final ErrorTypeExtractor extractor;
   private final TraceComparator comparator;
   private final ErrorBuilder builder;
   
   public ErrorGenerator() {
      this.extractor = new ErrorTypeExtractor();
      this.comparator = new TraceComparator();
      this.builder = new ErrorBuilder();
   }
   
   @Override
   public Result throwError(Scope scope, Trace trace, Result result) {
      Object cause = result.getValue();
      
      if(!Throwable.class.isInstance(cause)) {
         Type type = extractor.extract(scope, cause);
         
         if(type != null) {
            Module module = type.getModule();
            String resource = module.getName();
            String name = type.getName();
            
            throw new InternalError(cause, trace, resource + "." + name + ": " + cause + "\n\tat " + trace);
         }
         throw new InternalError(cause, trace, cause + "\n\tat " + trace);
      }
      return throwError(scope, trace, (Throwable)cause);
   }

   @Override
   public Result throwError(Scope scope, Trace trace, Throwable cause) {
      String message = cause.getMessage();
      
      if(Error.class.isInstance(cause)) {
         Error error = (Error)cause;
         Object original = error.getOriginal();
         Trace origin = error.getOrigin();
         String chain = error.getChain();
         String stack = error.getStack();
         int result = comparator.compare(origin, trace);
         
         if(result != 0) {
            stack = stack + "\n\tat " + trace;  // new trace to add
         }
         throw new InternalError(original, trace, stack, chain);
      }
      return builder.throwError(scope, trace, cause, message);
   }
}
