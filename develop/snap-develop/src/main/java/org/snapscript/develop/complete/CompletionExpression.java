package org.snapscript.develop.complete;

import java.util.Set;

public class CompletionExpression {
   
   private final CompletionContext context;
   private final CompletionType constraint;
   private final Set<String> tokens;
   private final String complete;
   
   public CompletionExpression(String complete, CompletionType constraint, CompletionContext context, Set<String> tokens) {
      this.constraint = constraint;
      this.context = context;
      this.complete = complete;
      this.tokens = tokens;
   }
   
   public CompletionContext getContext(){
      return context;
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
