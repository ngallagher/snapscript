package org.snapscript.core;

public interface Compilation {
   Object compile(Context context, String resource, int line) throws Exception;
}
