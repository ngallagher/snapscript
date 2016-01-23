package org.snapscript.core;

import junit.framework.TestCase;

public class PathConverterTest extends TestCase {
   
   public void testPath() throws Exception {
      PathConverter parser = new PathConverter(".snap");
      
      assertEquals("game.tetris", parser.convert("/game/tetris.snap"));
      assertEquals("game.tetris", parser.convert("game/tetris.snap"));
      assertEquals("game.tetris", parser.convert("game\\tetris.snap"));
      assertEquals("game.tetris", parser.convert("\\game\\tetris.snap"));
      assertEquals("game.tetris", parser.convert("game.tetris"));
      assertEquals("test", parser.convert("test.snap"));
      assertEquals("test", parser.convert("/test.snap"));
      assertEquals("test", parser.convert("\\test.snap"));
      assertEquals("test", parser.convert("test"));
   }

}
