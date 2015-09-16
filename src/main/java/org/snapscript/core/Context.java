package org.snapscript.core;

import org.snapscript.core.convert.FunctionBinder;
import org.snapscript.core.execute.LibraryLinker;

public interface Context{  
   Module getModule(); // this module should be disposable
   Module getModule(String name);
   Module addModule(String name);
   FunctionBinder getBinder();   
   TypeLoader getLoader();  
   LibraryLinker getLinker();
   
}