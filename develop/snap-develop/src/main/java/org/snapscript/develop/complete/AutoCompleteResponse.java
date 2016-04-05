package org.snapscript.develop.complete;

import java.util.Set;

public class AutoCompleteResponse {

   private Set<String> tokens;
   
   public AutoCompleteResponse() {
      super();
   }

   public Set<String> getTokens() {
      return tokens;
   }

   public void setTokens(Set<String> tokens) {
      this.tokens = tokens;
   }
}
