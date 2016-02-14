package org.snapscript.core.exception;

import org.snapscript.core.InternalException;
import org.snapscript.core.Module;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Trace;
import org.snapscript.core.TraceComparator;
import org.snapscript.core.Type;

public class StackTraceUpdater {
   
   private final CauseTypeExtractor extractor;
   private final CauseFormatter formatter;
   private final TraceComparator comparator;
   private final Trace trace;
   
   public StackTraceUpdater(Trace trace) {
      this.extractor = new CauseTypeExtractor();
      this.formatter = new CauseFormatter();
      this.comparator = new TraceComparator();
      this.trace = trace;
   }
   
   public Result update(Scope scope, Result result) {
      Object cause = result.getValue();
      
      if(!Throwable.class.isInstance(cause)) {
         Type type = extractor.extract(scope, cause);
         
         if(type != null) {
            Module module = type.getModule();
            String resource = module.getName();
            String name = type.getName();
            
            throw new StackTraceException(cause, trace, resource + "." + name + ": " + cause + "\n\tat " + trace);
         }
         throw new StackTraceException(cause, trace, cause + "\n\tat " + trace);
      }
      return update(scope, (Throwable)cause);
   }

   public Result update(Scope scope, Throwable cause) {
      String message = cause.getMessage();
      
      if(StackTraceException.class.isInstance(cause)) {
         StackTraceException error = (StackTraceException)cause;
         Object original = error.getOriginal();
         Trace origin = error.getOrigin();
         String chain = error.getChain();
         String stack = error.getStack();
         int result = comparator.compare(origin, trace);
         
         if(result != 0) {
            stack = stack + "\n\tat " + trace;  // new trace to add
         }
         throw new StackTraceException(original, trace, stack, chain);
      }
      return update(scope, cause, message);
   }
   
   private Result update(Scope scope, Throwable cause, String message) {
      String other = formatter.format(cause); // how do we use this
      Object original = cause;
      
      if(!InternalException.class.isInstance(cause)) { // this is a genuine exception
         while(cause != null) {
            if(StackTraceException.class.isInstance(cause)) { 
               StackTraceException error = (StackTraceException)cause;
               String description = error.getDescription();
               Type type = extractor.extract(scope, original);
               
               if(type != null) {
                  Module module = type.getModule();
                  String resource = module.getName();
                  String name = type.getName();
                  
                  throw new StackTraceException(original, trace, resource + "." + name + ": " + message + "\n\tat " + trace, "Caused by: " + description);
               }
               throw new StackTraceException(original, trace, message + "\n\tat " + trace, "Caused by: " + description);
            }
            cause = cause.getCause(); 
         }
         throw new StackTraceException(original, trace, message + "\n\tat " + trace, other);
      }
      while(cause != null) {
         if(StackTraceException.class.isInstance(cause)) { 
            StackTraceException error = (StackTraceException)cause;
            Object previous = error.getOriginal();
            String stack = error.getStack();
            String chain = error.getChain();
            
            throw new StackTraceException(previous, trace, stack + "\n\tat " + trace, chain);
         }
         cause = cause.getCause(); 
      }
      throw new StackTraceException(original, trace, message + "\n\tat " + trace, other);
   }
}
