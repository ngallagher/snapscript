package org.snapscript.core;

public class Trace {
   
   private final TraceType type;
   private final String resource;
   private final int line;
   
   public Trace(TraceType type, String resource, int line) {
      this.resource = resource;
      this.line = line;
      this.type = type;
   }

   public TraceType getType() {
      return type;
   }

   public String getResource() {
      return resource;
   }

   public int getLine() {
      return line;
   }
   
   @Override
   public String toString() {
      return String.format("%s:%s", resource, line);
   }
}
