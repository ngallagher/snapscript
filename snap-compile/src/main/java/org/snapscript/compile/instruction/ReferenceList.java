package org.snapscript.compile.instruction;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.Value;

public class ReferenceList implements Evaluation {
   
   private final ReferencePart part;
   private final ReferenceList list;
   
   public ReferenceList(ReferencePart part) {
      this(part, null);
   }
   
   public ReferenceList(ReferencePart part, ReferenceList list) {
      this.part = part;
      this.list = list;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value result = part.evaluate(scope, left);         
 
      if(list != null) {
         Object value = result.getValue();
         
         if(value == null) {
            throw new NullPointerException("Reference to a null object");
         }
         return list.evaluate(scope, value);
      }
      return result;
   }      
}