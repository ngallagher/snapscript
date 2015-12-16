package org.snapscript.core.convert;

import org.snapscript.core.PrimitiveParser;
import org.snapscript.core.PrimitivePromoter;

public abstract class ConstraintConverter  {
   
   public static final int EXACT = 100;
   public static final int SIMILAR = 70;
   public static final int COMPATIBLE = 20;
   public static final int POSSIBLE = 10;
   public static final int INVALID = 0;
   
   protected final PrimitivePromoter promoter;
   protected final PrimitiveParser parser;
   
   protected ConstraintConverter() {
      this.promoter = new PrimitivePromoter();
      this.parser = new PrimitiveParser();
   }
   
   protected Object convert(Class type, Object value) throws Exception {
      Class actual = promoter.convert(type);
      String text = String.valueOf(value);
      
      try {
         if (actual == String.class) {
            return text;
         }
         if (actual == Integer.class) {
            return parser.parseInteger(text);
         }
         if (actual == Double.class) {
            return parser.parseDouble(text);
         }
         if (actual == Float.class) {
            return parser.parseFloat(text);
         }
         if (actual == Boolean.class) {
            return parser.parseBoolean(text);
         }
         if (actual == Byte.class) {
            return parser.parseByte(text);
         }
         if (actual == Short.class) {
            return parser.parseShort(text);
         }
         if (actual == Long.class) {
            return parser.parseLong(text);
         }
         if (actual == Character.class) {
            return parser.parseCharacter(text);
         }
      } catch(Exception e) {
         throw new IllegalStateException("Could not convert '" + value + "' to " + actual, e);
      }
      return value;
   }
   
   public abstract int score(Object type) throws Exception;
   public abstract Object convert(Object value) throws Exception;
}
