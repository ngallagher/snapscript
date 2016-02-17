package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

import org.snapscript.core.TestType;
import org.snapscript.core.Type;

public class ByteConverterTest extends TestCase {

   public void testByte() throws Exception {
      Type type = new TestType(null, null, null, Byte.class);
      ByteConverter converter = new ByteConverter(type);
      
      assertEquals(converter.score((byte)11), ConstraintConverter.EXACT);
      assertEquals(converter.score(new BigDecimal("0.11")), ConstraintConverter.COMPATIBLE);
      assertEquals(converter.score(new AtomicLong(234L)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new AtomicInteger(222)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new Integer(33211)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score("0.12"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("-.012e+12"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score(null), ConstraintConverter.POSSIBLE);
      
      assertEquals(converter.convert(11.2d), new Byte((byte)11));
      assertEquals(converter.convert(new BigDecimal("0.11")), new Byte((byte)0));
      assertEquals(converter.convert(new AtomicLong(234L)), new Byte((byte)234));
      assertEquals(converter.convert(new AtomicInteger(222)), new Byte((byte)222));
      assertEquals(converter.convert(new Integer(33211)), new Byte((byte)33211));
      assertEquals(converter.convert("0.12"), new Byte((byte)0));
      assertEquals(converter.convert("-.012e+12"), new Byte((byte)0));
      assertEquals(converter.convert(null), null);
   }
   
   public void testPrimitiveDouble() throws Exception {
      Type type = new TestType(null, null, null, byte.class);
      ByteConverter converter = new ByteConverter(type);
      
      assertEquals(converter.score((byte)11), ConstraintConverter.EXACT);
      assertEquals(converter.score(new BigDecimal("0.11")), ConstraintConverter.COMPATIBLE);
      assertEquals(converter.score(new AtomicLong(234L)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new AtomicInteger(222)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new Integer(33211)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(null), ConstraintConverter.INVALID);
   }
}
