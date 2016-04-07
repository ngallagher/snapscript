package org.snapscript.develop.complete;

import static org.snapscript.develop.complete.CompletionTokenClassifier.CLASS;
import static org.snapscript.develop.complete.CompletionTokenClassifier.CONSTANT;
import static org.snapscript.develop.complete.CompletionTokenClassifier.ENUMERATION;
import static org.snapscript.develop.complete.CompletionTokenClassifier.FUNCTION;
import static org.snapscript.develop.complete.CompletionTokenClassifier.TOKEN;
import static org.snapscript.develop.complete.CompletionTokenClassifier.TRAIT;
import static org.snapscript.develop.complete.CompletionTokenClassifier.VARIABLE;

public class CompleteFilter {

   private final String complete;
   private final String trim;
   private final String prefix;
   
   public CompleteFilter(String prefix, String complete) {
      this.trim = complete.trim();
      this.complete = complete;
      this.prefix = prefix;
   }
   
   public boolean acceptExternal(String text, String type) {
      if(text.startsWith(prefix)) {
         if(type.equals(FUNCTION)) {
            return trim.endsWith(".") && !complete.endsWith("extends ") && !complete.endsWith("with ");
         } else if(type.equals(VARIABLE)) {
            return trim.endsWith(".") && !complete.endsWith("extends ") && !complete.endsWith("with ");
         } else if(type.equals(CONSTANT)) {
            return trim.endsWith(".") && !complete.endsWith("extends ") && !complete.endsWith("with ");
         } else if(type.equals(CLASS)) {
            return trim.endsWith("(") || (!trim.endsWith(".") && !complete.endsWith("with "));
         } else if(type.equals(ENUMERATION)) {
            return trim.endsWith("(") || (!trim.endsWith(".") && !complete.endsWith("with ") && !complete.endsWith("extends ") && !complete.endsWith("new ")); 
         } else if(type.equals(TRAIT)) {
            return trim.endsWith("(") || (!trim.endsWith(".") && !complete.endsWith("new "));
         }
      }
      return false;
   }
   
   public boolean acceptInternal(String text, String type) {
      if(text.startsWith(prefix)) {
         if(type.equals(FUNCTION)) {
            return !complete.endsWith("new ") && !complete.endsWith("extends ") && !complete.endsWith("with ");
         } else if(type.equals(VARIABLE)) {
            return !complete.endsWith("new ") && !complete.endsWith("extends ") && !complete.endsWith("with ");
         } else if(type.equals(CONSTANT)) {
            return !complete.endsWith("new ") && !complete.endsWith("extends ") && !complete.endsWith("with ");
         } else if(type.equals(CLASS)) {
            return trim.endsWith("(") || (!trim.endsWith(".") && !complete.endsWith("with "));
         } else if(type.equals(ENUMERATION)) {
            return trim.endsWith("(") || (!trim.endsWith(".") && !complete.endsWith("with ") && !complete.endsWith("extends ") && !complete.endsWith("new ")); 
         } else if(type.equals(TRAIT)) {
            return trim.endsWith("(") || (!trim.endsWith(".") && !complete.endsWith("new "));
         } else if(type.equals(TOKEN)) {
            return true;
         }
      }
      return false;
   }
}
