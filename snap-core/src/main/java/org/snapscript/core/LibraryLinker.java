package org.snapscript.core;

public interface LibraryLinker {  
   Library link(String name, String source) throws Exception;
   Library link(String name, String source, String grammar) throws Exception;
}
