package org.snapscript.compile.link;

public interface Qualifier {
   String[] getSegments();
   String getQualifier();
   String getLocation();
   String getTarget();
}
