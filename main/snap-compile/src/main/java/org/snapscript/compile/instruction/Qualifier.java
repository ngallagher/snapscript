package org.snapscript.compile.instruction;

public interface Qualifier {
   String[] getSegments();
   String getQualifier();
   String getLocation();
   String getTarget();
}
