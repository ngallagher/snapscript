package org.snapscript.core.convert;

import junit.framework.TestCase;

import org.snapscript.core.TestType;
import org.snapscript.core.Type;

public class CharacterConverterTest extends TestCase {

   public void testCharacter() throws Exception {
      Type type = new TestType(null, null, null, Character.class);
      CharacterConverter converter = new CharacterConverter(type);
      
      assertEquals(converter.score('s'), ConstraintConverter.EXACT);
      assertEquals(converter.score("s"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("ss"), ConstraintConverter.INVALID);
      assertEquals(converter.score((Object)null), ConstraintConverter.POSSIBLE);
      
      assertEquals(converter.convert('s'), 's');
      assertEquals(converter.convert("s"), 's');
      assertEquals(converter.convert((Object)null), null);
   }
   
   public void testPrimitiveCharacter() throws Exception {
      Type type = new TestType(null, null, null, char.class);
      CharacterConverter converter = new CharacterConverter(type);
      
      assertEquals(converter.score('s'), ConstraintConverter.EXACT);
      assertEquals(converter.score("s"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("ss"), ConstraintConverter.INVALID);
      assertEquals(converter.score((Object)null), ConstraintConverter.INVALID);
      
      assertEquals(converter.convert('s'), 's');
      assertEquals(converter.convert("s"), 's');
   }
}
