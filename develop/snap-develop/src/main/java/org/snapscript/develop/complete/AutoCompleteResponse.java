package org.snapscript.develop.complete;

import java.util.Map;

public class AutoCompleteResponse {

   private Map<String, String> tokens;
   
   public AutoCompleteResponse() {
      super();
   }

   public Map<String, String> getTokens() {
      return tokens;
   }

   public void setTokens(Map<String, String> tokens) {
      this.tokens = tokens;
   }
}
