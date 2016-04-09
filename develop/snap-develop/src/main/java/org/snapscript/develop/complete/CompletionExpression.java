package org.snapscript.develop.complete;

import java.util.Set;

public class CompletionExpression {
   
   private final CompletionType constraint;
   private final Set<String> tokens;
   private final String complete;
   
   public CompletionExpression(String complete, CompletionType constraint, Set<String> tokens) {
      this.constraint = constraint;
      this.complete = complete;
      this.tokens = tokens;
   }
   
   public CompletionType getConstraint() {
      return constraint;
   }
   
   public Set<String> getTypes() {
      return tokens;
   }
   
   public String getExpression() {
      return complete;
   }
   
   @Override
   public String toString() {
      return String.format("%s -> %s", complete, constraint);
   }

}
