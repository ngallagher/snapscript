package org.snapscript.compile.instruction;

import org.snapscript.core.Trace;
import org.snapscript.parse.Line;

public class LineTrace implements Trace {

   private final String resource;
   private final Class type;
   private final int line;
   private final int key;
   
   public LineTrace(Object value, Line line, int key) {
      this.resource = line.getResource();
      this.line = line.getNumber();
      this.type = value.getClass();
      this.key = key;
   }
   
   @Override
   public Class getInstruction() {
      return type;
   }

   @Override
   public String getResource() {
      return resource;
   }

   @Override
   public int getLine() {
      return line;
   }

   @Override
   public int getKey() {
      return key;
   } 
}
