package org.snapscript.core;

public class SignatureMatcher {

   private ArgumentConverter converter;
   private ArgumentMatcher matcher;
   
   public SignatureMatcher(Signature signature, Module module) {
      this.matcher = new ArgumentMatcher(signature, module);
   }
   
   public ArgumentConverter getConverter() {
      if(converter == null) {
         try {
            converter = matcher.getConverter();
         } catch(Exception e) {
            throw new InternalStateException("Could not match signature", e);
         }
      }
      return converter;
   }
}
