package org.snapscript.compile;

import org.snapscript.core.Executable;

public interface Compiler {
   Executable compile(String source) throws Exception;
   Executable compile(String source, boolean verbose) throws Exception;   
}
