package org.snapscript.core;

import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.resource.ResourceReader;

public interface Context{
   ResourceReader getReader();
   ModuleBuilder getBuilder();
   TraceAnalyzer getAnalyzer();
   FunctionBinder getBinder();
   PackageLinker getLinker();
   TypeLoader getLoader();  
   
}