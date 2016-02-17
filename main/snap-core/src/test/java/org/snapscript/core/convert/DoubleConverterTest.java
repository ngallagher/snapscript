package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

import org.snapscript.core.TestType;
import org.snapscript.core.Type;

public class DoubleConverterTest extends TestCase {
   
   public void testDouble() throws Exception {
      Type type = new TestType(null, null, null, Double.class);
      DoubleConverter converter = new DoubleConverter(type);
      
      assertEquals(converter.score(11.2d), ConstraintConverter.EXACT);
      assertEquals(converter.score(new BigDecimal("0.11")), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new AtomicLong(234L)), ConstraintConverter.COMPATIBLE);
      assertEquals(converter.score(new AtomicInteger(222)), ConstraintConverter.COMPATIBLE);
      assertEquals(converter.score(new Integer(33211)), ConstraintConverter.COMPATIBLE);
      assertEquals(converter.score("0.12"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("-.012e+12"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score(null), ConstraintConverter.POSSIBLE);
      
      assertEquals(converter.convert(11.2d), 11.2d);
      assertEquals(converter.convert(new BigDecimal("0.11")), 0.11d);
      assertEquals(converter.convert(new AtomicLong(234L)), 234.0d);
      assertEquals(converter.convert(new AtomicInteger(222)), 222.0d);
      assertEquals(converter.convert(new Integer(33211)), 33211.0d);
      assertEquals(converter.convert("0.12"), 0.12d);
      assertEquals(converter.convert("-.012e+12"), -.012e+12d);
      assertEquals(converter.convert(null), null);
   }
   
   public void testPrimitiveDouble() throws Exception {
      Type type = new TestType(null, null, null, double.class);
      DoubleConverter converter = new DoubleConverter(type);
      
      assertEquals(converter.score(11.2d), ConstraintConverter.EXACT);
      assertEquals(converter.score(new BigDecimal("0.11")), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new AtomicLong(234L)), ConstraintConverter.COMPATIBLE);
      assertEquals(converter.score("0.12"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("-.012e+12"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score(null), ConstraintConverter.INVALID);
   }

}
