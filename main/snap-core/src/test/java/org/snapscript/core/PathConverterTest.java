package org.snapscript.core;

import junit.framework.TestCase;

public class PathConverterTest extends TestCase {
   
   public void testPath() throws Exception {
      PathConverter parser = new PathConverter(".snap");
      
      assertEquals("game.tetris", parser.createModule("/game/tetris.snap"));
      assertEquals("game.tetris", parser.createModule("game/tetris.snap"));
      assertEquals("game.tetris", parser.createModule("game\\tetris.snap"));
      assertEquals("game.tetris", parser.createModule("\\game\\tetris.snap"));
      assertEquals("game.tetris", parser.createModule("game.tetris"));
      assertEquals("test", parser.createModule("test.snap"));
      assertEquals("test", parser.createModule("/test.snap"));
      assertEquals("test", parser.createModule("\\test.snap"));
      assertEquals("test", parser.createModule("test"));
      
      assertEquals("/test.snap", parser.createPath("test"));
      assertEquals("/game/tetris.snap", parser.createPath("game.tetris"));
      assertEquals("/test.snap", parser.createPath("/test.snap"));
   }

}
