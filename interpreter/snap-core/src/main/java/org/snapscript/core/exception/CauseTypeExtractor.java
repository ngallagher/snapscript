package org.snapscript.core.exception;

import org.snapscript.core.ReferenceTypeExtractor;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;

public class CauseTypeExtractor {

   private final ReferenceTypeExtractor extractor;
   
   public CauseTypeExtractor() {
      this.extractor = new ReferenceTypeExtractor();
   }
   
   public Type extract(Scope scope, Object cause) {
      try {
         if(StackTraceException.class.isInstance(cause)) {
            StackTraceException error = (StackTraceException)cause;
            Object value = error.getOriginal();
            
            return extractor.extract(scope, value);
         }
         return extractor.extract(scope, cause);
      } catch(Exception e) {
         return null;
      }
   }
}
