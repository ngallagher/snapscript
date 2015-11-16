package org.snapscript.core;

import org.snapscript.common.io.ResourceReader;
import org.snapscript.core.bind.FunctionBinder;

public interface Context{
   ResourceReader getReader();
   ModuleBuilder getBuilder();
   FunctionBinder getBinder();
   LibraryLinker getLinker();
   TypeLoader getLoader();  
   
}