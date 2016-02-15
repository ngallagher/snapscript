package org.snapscript.core;

public enum TraceType {
   CONSTRUCT,
   INVOKE,
   NORMAL;
   
   public static Trace getConstruct(String resource, int line) {
      return new Trace(CONSTRUCT, resource, line);
   }
   
   public static Trace getInvoke(String resource, int line) {
      return new Trace(INVOKE, resource, line);
   }
   
   public static Trace getNormal(String resource, int line) {
      return new Trace(NORMAL, resource, line);
   }
}
