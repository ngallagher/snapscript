package org.snapscript.core;

import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.convert.ConstraintMatcher;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.error.ThreadStack;

public interface Context{
   ThreadStack getStack();
   ErrorHandler getHandler();
   ResourceManager getManager();
   ModuleRegistry getRegistry();
   ConstraintMatcher getMatcher();
   TraceInterceptor getInterceptor();
   ExpressionEvaluator getEvaluator();
   FunctionBinder getBinder();
   PackageLinker getLinker();
   TypeLoader getLoader();  
   
}