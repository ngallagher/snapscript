package org.snapscript.core;

import org.snapscript.core.bind.FunctionBinder;

public interface Context{  
   Module getModule(); // this module should be disposable
   Module getModule(String name);
   Module addModule(String name);
   FunctionBinder getBinder();
   LibraryLinker getLinker();
   TypeLoader getLoader();  
   
}