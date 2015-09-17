package org.snapscript.core.execute;

import org.snapscript.parse.StringToken;

public class FullQualifier implements Qualifier {

   private final StringToken[] tokens;
   private final int count;

   public FullQualifier(StringToken... tokens) {
      this.count = tokens.length;
      this.tokens = tokens;
   }

   @Override
   public String getQualifier() {
      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < count; i++) {
         String value = tokens[i].getValue();

         if (i > 0) {
            builder.append(".");
         }
         builder.append(value);
      }
      return builder.toString();
   }

   @Override
   public String getLocation() {
      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < count - 1; i++) {
         StringToken token = tokens[i];
         String value = token.getValue();

         if (i > 0) {
            builder.append(".");
         }
         builder.append(value);
      }
      return builder.toString();
   }

   @Override
   public String getTarget() {
      if (count > 0) {
         StringToken token = tokens[count - 1];
         String value = token.getValue();

         return value;
      }
      return null;
   }
}
