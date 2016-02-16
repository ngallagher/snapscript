package org.snapscript.core;

import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.convert.ConstraintMatcher;

public interface Context{
   ResourceManager getManager();
   ModuleRegistry getRegistry();
   TraceAnalyzer getAnalyzer();
   ConstraintMatcher getMatcher();
   ExpressionEvaluator getEvaluator();
   FunctionBinder getBinder();
   PackageLinker getLinker();
   TypeLoader getLoader();  
   
}