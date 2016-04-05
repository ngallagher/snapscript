package org.snapscript.develop.complete;

public class AutoCompleteRequest {

   private String resource;
   private String source;
   private String prefix;
   
   public AutoCompleteRequest() {
      super();
   }

   public String getResource() {
      return resource;
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   public String getSource() {
      return source;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public String getPrefix() {
      return prefix;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }
}
