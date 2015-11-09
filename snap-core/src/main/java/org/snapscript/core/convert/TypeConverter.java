package org.snapscript.core.convert;

import org.snapscript.core.PrimitivePromoter;

public abstract class TypeConverter  {
   public static final int EXACT = 100;
   public static final int SIMILAR = 70;
   public static final int COMPATIBLE = 20;
   public static final int POSSIBLE = 10;
   public static final int INVALID = 0;
   
   protected final PrimitivePromoter promoter;
   
   protected TypeConverter() {
      this.promoter = new PrimitivePromoter();
   }
   
   protected Object convert(Class type, String value) throws Exception {
      Class actual = promoter.convert(type);

      try {
         if (actual == String.class) {
            return value;
         }
         if (actual == Integer.class) {
            return Integer.parseInt(value);
         }
         if (actual == Double.class) {
            return Double.parseDouble(value);
         }
         if (actual == Float.class) {
            return Float.parseFloat(value);
         }
         if (actual == Boolean.class) {
            return Boolean.parseBoolean(value);
         }
         if (actual == Byte.class) {
            return Byte.parseByte(value);
         }
         if (actual == Short.class) {
            return Short.parseShort(value);
         }
         if (actual == Long.class) {
            return Long.parseLong(value);
         }
         if (actual == Character.class) {
            return value.charAt(0);
         }
      } catch(Exception e) {
         throw new IllegalStateException("Could not convert '" + value + "' to " + actual, e);
      }
      return value;
   }
   
   public abstract int score(Object type) throws Exception;
   public abstract Object convert(Object value) throws Exception;
}
