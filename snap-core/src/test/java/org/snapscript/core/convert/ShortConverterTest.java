package org.snapscript.core.convert;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

import org.snapscript.core.Type;
import org.snapscript.core.index.ClassType;

public class ShortConverterTest extends TestCase {

   public void testByte() throws Exception {
      Type type = new ClassType(null, null, null, Short.class);
      ShortConverter converter = new ShortConverter(type);
      
      assertEquals(converter.score((short)11), ConstraintConverter.EXACT);
      assertEquals(converter.score(new BigDecimal("0.11")), ConstraintConverter.COMPATIBLE);
      assertEquals(converter.score(new AtomicLong(234L)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new AtomicInteger(222)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new Integer(33211)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score("0.12"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("-.012e+12"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score(null), ConstraintConverter.POSSIBLE);
      
      assertEquals(converter.convert(11.2d), new Short((short)11));
      assertEquals(converter.convert(new BigDecimal("0.11")), new Short((short)0));
      assertEquals(converter.convert(new AtomicLong(234L)), new Short((short)234));
      assertEquals(converter.convert(new AtomicInteger(222)), new Short((short)222));
      assertEquals(converter.convert(new Integer(33211)), new Short((short)33211));
      assertEquals(converter.convert("0.12"), new Short((short)0));
      assertEquals(converter.convert("-.012e+12"), new Short((short)0));
      assertEquals(converter.convert(null), null);
   }
   
   public void testPrimitiveDouble() throws Exception {
      Type type = new ClassType(null, null, null, short.class);
      ShortConverter converter = new ShortConverter(type);
      
      assertEquals(converter.score((short)11), ConstraintConverter.EXACT);
      assertEquals(converter.score(new BigDecimal("0.11")), ConstraintConverter.COMPATIBLE);
      assertEquals(converter.score(new AtomicLong(234L)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new AtomicInteger(222)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(new Integer(33211)), ConstraintConverter.SIMILAR);
      assertEquals(converter.score(null), ConstraintConverter.INVALID);
   }
}
