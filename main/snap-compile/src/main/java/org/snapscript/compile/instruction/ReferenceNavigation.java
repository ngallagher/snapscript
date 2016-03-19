package org.snapscript.compile.instruction;

import org.snapscript.core.Evaluation;
import org.snapscript.core.Scope;
import org.snapscript.core.Value;
import org.snapscript.parse.StringToken;

public class ReferenceNavigation implements Evaluation {
   
   private final ReferenceOperator operator;
   private final ReferencePart part;
   private final Evaluation next;
   
   public ReferenceNavigation(ReferencePart part) {
      this(part, null, null);
   }
   
   public ReferenceNavigation(ReferencePart part, StringToken operator, Evaluation next) {
      this.operator = ReferenceOperator.resolveOperator(operator);
      this.part = part;
      this.next = next;
   }
   
   @Override
   public Value evaluate(Scope scope, Object left) throws Exception {
      Value value = part.evaluate(scope, left);         
 
      if(next != null) {
         return operator.operate(scope, next, value);
      }
      return value;
   }      
}