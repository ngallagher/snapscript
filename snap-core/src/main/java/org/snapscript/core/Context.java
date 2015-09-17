package org.snapscript.core;


public interface Context{  
   Module getModule(); // this module should be disposable
   Module getModule(String name);
   Module addModule(String name);
   FunctionBinder getBinder();   
   TypeLoader getLoader();  
   LibraryLinker getLinker();
   
}