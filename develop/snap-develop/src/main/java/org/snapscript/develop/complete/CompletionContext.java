package org.snapscript.develop.complete;

import java.util.Map;

public class CompletionContext {

   private final Map<String, String> tokens;
   private final CompletionType type;
   
   public CompletionContext(CompletionType type, Map<String, String> tokens) {
      this.tokens = tokens;
      this.type = type;
   }
   
   public Map<String, String> getTokens(){
      return tokens;
   }
   
   public CompletionType getType(){
      return type;
   }
}
