package org.snapscript.develop.complete;

import java.util.Set;

public class CompletionFilter {

   private final CompletionExpression  complete;
   private final String prefix;
   
   public CompletionFilter(CompletionExpression complete, String prefix) {
      this.complete = complete;
      this.prefix = prefix;
   }
   
   public boolean acceptToken(String text, String type) {
      if(text.startsWith(prefix)) {
         Set<String> types = complete.getTypes();
         return types.contains(type);
      }
      return false;
   }
}
