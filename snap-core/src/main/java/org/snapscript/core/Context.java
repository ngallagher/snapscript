package org.snapscript.core;

import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.convert.ConstraintMatcher;

public interface Context{
   ResourceManager getManager();
   ModuleBuilder getBuilder();
   TraceAnalyzer getAnalyzer();
   ConstraintMatcher getMatcher();
   FunctionBinder getBinder();
   PackageLinker getLinker();
   TypeLoader getLoader();  
   
}