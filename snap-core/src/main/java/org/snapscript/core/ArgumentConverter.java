package org.snapscript.core;

public interface ArgumentConverter { 
   int score(Object... list) throws Exception;
   Object[] convert(Object... list) throws Exception;
}
