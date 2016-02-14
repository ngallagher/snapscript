package org.snapscript.core.error;

import org.snapscript.core.InternalException;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Trace;
import org.snapscript.core.Type;

public class ErrorBuilder {
   
   private final ErrorTypeExtractor extractor;
   private final StackTraceFormatter formatter;
   
   public ErrorBuilder() {
      this.extractor = new ErrorTypeExtractor();
      this.formatter = new StackTraceFormatter();
   }
   
   public Result throwError(Scope scope, Trace trace, Throwable cause, String message) {
      if(!InternalException.class.isInstance(cause)) { // this is a genuine exception
         return throwExternalError(scope, trace, cause, message);
      }
      return throwInternalError(scope, trace, cause, message);
   }
   
   private Result throwInternalError(Scope scope, Trace trace, Throwable cause, String message) {
      String other = formatter.format(cause); // how do we use this
      Object original = cause;
      
      while(cause != null) {
         if(Error.class.isInstance(cause)) { 
            Error error = (Error)cause;
            Object previous = error.getOriginal();
            String stack = error.getStack();
            String chain = error.getChain();
            
            throw new InternalError(previous, trace, stack + "\n\tat " + trace, chain);
         }
         cause = cause.getCause(); 
      }
      Type type = extractor.extract(scope, original);
      
      if(type != null) {
         Module module = type.getModule();
         String resource = module.getName();
         String name = type.getName();
         
         throw new InternalError(original, trace, resource + "." + name + ": " + message + "\n\tat " + trace, other);
      }
      throw new InternalError(original, trace, message + "\n\tat " + trace, other);
   }
   
   private Result throwExternalError(Scope scope, Trace trace, Throwable cause, String message) {
      String other = formatter.format(cause); // how do we use this
      Object original = cause;
      
      while(cause != null) {
         if(Error.class.isInstance(cause)) { 
            Error error = (Error)cause;
            String description = error.getDescription();
            Type type = extractor.extract(scope, original);
            
            if(type != null) {
               Module module = type.getModule();
               String resource = module.getName();
               String name = type.getName();
               
               throw new InternalError(original, trace, resource + "." + name + ": " + message + "\n\tat " + trace, "Caused by: " + description);
            }
            throw new InternalError(original, trace, message + "\n\tat " + trace, "Caused by: " + description);
         }
         cause = cause.getCause(); 
      }
      throw new InternalError(original, trace, message + "\n\tat " + trace, other);
   }
}
