package org.snapscript.core.convert;

import org.snapscript.core.PrimitivePromoter;

public abstract class ConstraintConverter  {
   
   public static final int EXACT = 100;
   public static final int SIMILAR = 70;
   public static final int COMPATIBLE = 20;
   public static final int POSSIBLE = 10;
   public static final int INVALID = 0;
   
   protected final PrimitivePromoter promoter;
   
   protected ConstraintConverter() {
      this.promoter = new PrimitivePromoter();
   }
   
   protected Object convert(Class type, Object value) throws Exception {
      Class actual = promoter.convert(type);

      try {
         String text = String.valueOf(value);
         
         if (actual == String.class) {
            return text;
         }
         if (actual == Integer.class) {
            return Integer.parseInt(text);
         }
         if (actual == Double.class) {
            return Double.parseDouble(text);
         }
         if (actual == Float.class) {
            return Float.parseFloat(text);
         }
         if (actual == Boolean.class) {
            return Boolean.parseBoolean(text);
         }
         if (actual == Byte.class) {
            return Byte.parseByte(text);
         }
         if (actual == Short.class) {
            return Short.parseShort(text);
         }
         if (actual == Long.class) {
            return Long.parseLong(text);
         }
         if (actual == Character.class) {
            return text.charAt(0);
         }
      } catch(Exception e) {
         throw new IllegalStateException("Could not convert '" + value + "' to " + actual, e);
      }
      return value;
   }
   
   protected boolean compatible(Class type, Object value) throws Exception {
      try {
         convert(type, value);
      } catch(Exception e) {
         return false;
      }
      return true;
   }
   
   public abstract int score(Object type) throws Exception;
   public abstract Object convert(Object value) throws Exception;
}