package org.snapscript.compile.instruction;

import org.snapscript.core.Bug;
import org.snapscript.parse.StringToken;

@Bug("Do we really need this?, maybe for import static??")
public class WildQualifier implements Qualifier {

   private final StringToken[] tokens;
   private final int count;

   public WildQualifier(StringToken... tokens) {
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

      for (int i = 0; i < count; i++) {
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
      return null;
   }
}
