package org.snapscript.develop.complete;

public class CompletionToken {

   public static final String CLASS = "class";
   public static final String TRAIT = "trait";
   public static final String MODULE = "module";
   public static final String FUNCTION = "function";
   public static final String VARIABLE = "var";
   public static final String CONSTANT = "const";
   public static final String ENUMERATION = "enum";
   public static final String STRING = "string";
   public static final String TOKEN = "token";
   
   private final String constraint;
   private final String name;
   private final String type;
   
   public CompletionToken(String name, String type, String constraint) {
      this.constraint = constraint;
      this.name = name;
      this.type = type;
   }

   public String getConstraint() {
      return constraint;
   }

   public String getName() {
      return name;
   }

   public String getType() {
      return type;
   }
}
