package org.snapscript.core.convert;

import org.snapscript.core.Type;

import junit.framework.TestCase;

public class CharacterConverterTest extends TestCase {

   public void testCharacter() throws Exception {
      Type type = new Type(null, null, null, Character.class);
      CharacterConverter converter = new CharacterConverter(type);
      
      assertEquals(converter.score('s'), ConstraintConverter.EXACT);
      assertEquals(converter.score("s"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("ss"), ConstraintConverter.INVALID);
      assertEquals(converter.score(null), ConstraintConverter.POSSIBLE);
      
      assertEquals(converter.convert('s'), 's');
      assertEquals(converter.convert("s"), 's');
      assertEquals(converter.convert(null), null);
   }
   
   public void testPrimitiveCharacter() throws Exception {
      Type type = new Type(null, null, null, char.class);
      CharacterConverter converter = new CharacterConverter(type);
      
      assertEquals(converter.score('s'), ConstraintConverter.EXACT);
      assertEquals(converter.score("s"), ConstraintConverter.POSSIBLE);
      assertEquals(converter.score("ss"), ConstraintConverter.INVALID);
      assertEquals(converter.score(null), ConstraintConverter.INVALID);
      
      assertEquals(converter.convert('s'), 's');
      assertEquals(converter.convert("s"), 's');
   }
}
