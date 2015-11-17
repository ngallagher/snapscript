package org.snapscript.core;

import org.snapscript.core.bind.FunctionBinder;
import org.snapscript.core.resource.ResourceReader;

public interface Context{
   ResourceReader getReader();
   ModuleBuilder getBuilder();
   FunctionBinder getBinder();
   LibraryLinker getLinker();
   TypeLoader getLoader();  
   
}