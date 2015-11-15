package org.snapscript.core;

import org.snapscript.core.bind.FunctionBinder;

public interface Context{  
   ModuleBuilder getBuilder();
   FunctionBinder getBinder();
   LibraryLinker getLinker();
   TypeLoader getLoader();  
   
}