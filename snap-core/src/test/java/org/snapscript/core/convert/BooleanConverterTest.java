package org.snapscript.core.convert;

import junit.framework.TestCase;

import org.snapscript.core.Type;

public class BooleanConverterTest extends TestCase {

   public void testBoolean() throws Exception {
      Type type = new Type(null, null, null, Boolean.class);
      BooleanConverter converter = new BooleanConverter(type);
      
      assertEquals(converter.score(true), ConstraintConverter.EXACT);
      assertEquals(converter.score(Boolean.TRUE), ConstraintConverter.EXACT);
      assertEquals(converter.score(Boolean.FALSE), ConstraintConverter.EXACT);
      assertEquals(converter.score("true"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("false"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("TRUE"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("FALSE"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("yes"), ConstraintConverter.INVALID);
      assertEquals(converter.score(null), ConstraintConverter.POSSIBLE);
      
      assertEquals(converter.convert(true), Boolean.TRUE);
      assertEquals(converter.convert(false), Boolean.FALSE);
      assertEquals(converter.convert("true"), Boolean.TRUE);
      assertEquals(converter.convert("false"), Boolean.FALSE);
      assertEquals(converter.convert("TRUE"), Boolean.TRUE);
      assertEquals(converter.convert("FALSE"), Boolean.FALSE);
      assertEquals(converter.convert(null), null);
   }
   
   public void testPrimitiveBoolean() throws Exception {
      Type type = new Type(null, null, null, boolean.class);
      BooleanConverter converter = new BooleanConverter(type);
      
      assertEquals(converter.score(true), ConstraintConverter.EXACT);
      assertEquals(converter.score(Boolean.TRUE), ConstraintConverter.EXACT);
      assertEquals(converter.score(Boolean.FALSE), ConstraintConverter.EXACT);
      assertEquals(converter.score("true"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("false"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("TRUE"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("FALSE"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("yes"), ConstraintConverter.INVALID);
      assertEquals(converter.score(null), ConstraintConverter.INVALID);
   }
}
