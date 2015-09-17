package org.snapscript.core.execute;

import org.snapscript.core.Reference;
import org.snapscript.core.Value;

public enum NumericConverter {
   DOUBLE {
      @Override
      public Value convert(Number reference) {
         Double value = reference.doubleValue();
         return new Reference(value);
      }
   },
   LONG {
      @Override
      public Value convert(Number reference) {
         Long value = reference.longValue();
         return new Reference(value);
      }
   },
   FLOAT {
      @Override
      public Value convert(Number reference) {
         Float value = reference.floatValue();
         return new Reference(value);
      }
   },
   INTEGER {
      @Override
      public Value convert(Number reference) {
         Integer value = reference.intValue();
         return new Reference(value);
      }
   };
   
   public abstract Value convert(Number value);
   
   public static NumericConverter resolveConverter(Number value) {
      Class type = value.getClass();
      
      if (Double.class == type) {
         return DOUBLE;
      }
      if (Long.class == type) {
         return LONG;
      }
      if (Float.class == type) {
         return FLOAT;
      }
      if (Integer.class == type) {
         return INTEGER;
      }
      return DOUBLE;
   }
   
   public static NumericConverter resolveConverter(Value left, Value right) {
      Class primary = left.getType();
      Class secondary = right.getType();

      if (Double.class == primary || Double.class == secondary) {
         return DOUBLE;
      }
      if (Long.class == primary || Long.class == secondary) {
         return LONG;
      }
      if (Float.class == primary || Float.class == secondary) {
         return FLOAT;
      }
      if (Integer.class == primary || Integer.class == secondary) {
         return INTEGER;
      }
      return DOUBLE;
   }
}